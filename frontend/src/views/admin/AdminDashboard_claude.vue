<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { Chart, registerables } from 'chart.js'
Chart.register(...registerables)

// ─── date helpers ────────────────────────────────────────────────────────────
const today = () => new Date().toISOString().split('T')[0]
const daysAgo = (n) => {
  const d = new Date()
  d.setDate(d.getDate() - n)
  return d.toISOString().split('T')[0]
}

const quickRanges = [
  { label: '7d', from: () => daysAgo(7), to: today },
  { label: '30d', from: () => daysAgo(30), to: today },
  { label: '90d', from: () => daysAgo(90), to: today },
  { label: '1y', from: () => daysAgo(365), to: today },
  { label: 'All', from: () => '2000-01-01', to: today },
]

const dateFrom = ref(daysAgo(90))
const dateTo = ref(today())
const activeRange = ref('90d')
const loading = ref(false)

// chart instances
let revenueChart = null,
  categoryChart = null,
  statusChart = null

// ─── colour palettes ──────────────────────────────────────────────────────────
const donutColors = [
  '#378ADD',
  '#1D9E75',
  '#D85A30',
  '#D4537E',
  '#BA7517',
  '#7F77DD',
  '#E24B4A',
  '#888780',
]
const statusColors = {
  completed: '#1D9E75',
  paid: '#378ADD',
  pending: '#BA7517',
  cancelled: '#E24B4A',
  processing: '#7F77DD',
  shipped: '#0F6E56',
  refunded: '#D85A30',
}

// ─── data state ──────────────────────────────────────────────────────────────
const revenueRows = ref([]) // [{period, revenue}]
const categoryData = ref([]) // [{label, value}]
const statusData = ref([]) // [{label, count, revenue}]
const kpiRaw = ref({ totalRevenue: 0, totalOrders: 0, avgOrderValue: 0, totalItems: 0 })

const categoryTotal = computed(() => categoryData.value.reduce((s, r) => s + r.value, 0))
const statusTotal = computed(() => statusData.value.reduce((s, r) => s + r.count, 0))

const kpis = computed(() => [
  { label: 'Total revenue', value: kpiRaw.value.totalRevenue, format: (v) => fmtCurrency(v) },
  { label: 'Orders', value: kpiRaw.value.totalOrders, format: (v) => v.toLocaleString() },
  { label: 'Avg order value', value: kpiRaw.value.avgOrderValue, format: (v) => fmtCurrency(v) },
  { label: 'Items sold', value: kpiRaw.value.totalItems, format: (v) => v.toLocaleString() },
])

// ─── formatters ──────────────────────────────────────────────────────────────
const fmt = (v) =>
  v == null
    ? '—'
    : v.toLocaleString('vi-VN', { style: 'currency', currency: 'VND', maximumFractionDigits: 0 })
const fmtCurrency = (v) => {
  if (v >= 1_000_000_000) return (v / 1_000_000_000).toFixed(1) + 'B ₫'
  if (v >= 1_000_000) return (v / 1_000_000).toFixed(1) + 'M ₫'
  return v.toLocaleString('vi-VN') + ' ₫'
}
const pct = (v, total) => (total ? ((v / total) * 100).toFixed(1) + '%' : '—')

// ─── API calls ───────────────────────────────────────────────────────────────
// Replace BASE_URL with your Spring Boot host, e.g. http://localhost:8080
const BASE_URL = '/api/dashboard'

async function fetchAll() {
  loading.value = true
  destroyCharts()
  const params = `?from=${dateFrom.value}&to=${dateTo.value}`
  try {
    const [kpiRes, revRes, catRes, stRes] = await Promise.all([
      fetch(`${BASE_URL}/kpis${params}`).then((r) => r.json()),
      fetch(`${BASE_URL}/revenue-over-time${params}`).then((r) => r.json()),
      fetch(`${BASE_URL}/sales-by-category${params}`).then((r) => r.json()),
      fetch(`${BASE_URL}/order-status${params}`).then((r) => r.json()),
    ])
    kpiRaw.value = kpiRes
    revenueRows.value = revRes // [{period: '2024-01', revenue: 123456}]
    categoryData.value = catRes // [{label: 'Fiction', value: 99000}]
    statusData.value = stRes // [{label: 'completed', count: 42, revenue: 999}]
  } catch (e) {
    console.error('Dashboard fetch failed:', e)
    // demo fallback so the UI isn't blank during local development
    loadDemoData()
  } finally {
    loading.value = false
    await nextTick()
    buildCharts()
  }
}

function loadDemoData() {
  kpiRaw.value = {
    totalRevenue: 487_300_000,
    totalOrders: 1243,
    avgOrderValue: 392_000,
    totalItems: 3876,
  }
  revenueRows.value = [
    '2024-04',
    '2024-05',
    '2024-06',
    '2024-07',
    '2024-08',
    '2024-09',
    '2024-10',
    '2024-11',
    '2024-12',
    '2025-01',
    '2025-02',
    '2025-03',
  ].map((p, i) => ({ period: p, revenue: 25_000_000 + Math.sin(i) * 8_000_000 + i * 2_000_000 }))
  categoryData.value = [
    { label: 'Fiction', value: 142_000_000 },
    { label: 'Non-fiction', value: 98_000_000 },
    { label: 'Science', value: 74_000_000 },
    { label: 'Children', value: 51_000_000 },
    { label: 'Self-help', value: 38_000_000 },
    { label: 'History', value: 29_000_000 },
    { label: 'Other', value: 55_300_000 },
  ]
  statusData.value = [
    { label: 'completed', count: 782 },
    { label: 'shipped', count: 198 },
    { label: 'processing', count: 121 },
    { label: 'pending', count: 87 },
    { label: 'cancelled', count: 55 },
  ]
}

// ─── chart builders ───────────────────────────────────────────────────────────
function destroyCharts() {
  revenueChart?.destroy()
  revenueChart = null
  categoryChart?.destroy()
  categoryChart = null
  statusChart?.destroy()
  statusChart = null
}

function buildCharts() {
  buildRevenue()
  buildCategory()
  buildStatus()
}

function buildRevenue() {
  const ctx = document.getElementById('revenueChart')
  if (!ctx) return
  const labels = revenueRows.value.map((r) => r.period)
  const data = revenueRows.value.map((r) => r.revenue)
  revenueChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels,
      datasets: [
        {
          label: 'Revenue',
          data,
          borderColor: '#378ADD',
          backgroundColor: 'rgba(55,138,221,0.08)',
          borderWidth: 2,
          pointRadius: 3,
          pointHoverRadius: 5,
          fill: true,
          tension: 0.35,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (ctx) => ' ' + fmtCurrency(ctx.parsed.y),
          },
        },
      },
      scales: {
        x: {
          grid: { display: false },
          ticks: { font: { size: 11 }, maxTicksLimit: 12, autoSkip: true },
        },
        y: {
          border: { display: false },
          ticks: {
            font: { size: 11 },
            callback: (v) => fmtCurrency(v),
          },
        },
      },
    },
  })
}

function buildDonut(id, labels, data, colors) {
  const ctx = document.getElementById(id)
  if (!ctx) return null
  return new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels,
      datasets: [
        {
          data,
          backgroundColor: colors,
          borderWidth: 2,
          borderColor: '#ffffff',
          hoverOffset: 8,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '68%',
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: (ctx) => ' ' + ctx.label + ': ' + ctx.parsed.toLocaleString(),
          },
        },
      },
    },
  })
}

function buildCategory() {
  const labels = categoryData.value.map((r) => r.label)
  const data = categoryData.value.map((r) => r.value)
  const colors = donutColors.slice(0, labels.length)
  categoryChart = buildDonut('categoryChart', labels, data, colors)
}

function buildStatus() {
  const items = statusData.value
  const labels = items.map((r) => r.label)
  const data = items.map((r) => r.count)
  const colors = labels.map((l, i) => statusColors[l] || donutColors[i % donutColors.length])
  statusChart = buildDonut('statusChart', labels, data, colors)
}

// ─── quick range ─────────────────────────────────────────────────────────────
function applyQuickRange(r) {
  dateFrom.value = r.from()
  dateTo.value = r.to()
  activeRange.value = r.label
  fetchAll()
}

onMounted(() => fetchAll())
</script>

<template>

  <v-app>

    <v-app-bar
      flat
      border="b"
      color="surface"
    >

      <v-app-bar-title>

        <span class="text-h6 font-weight-medium">Sales Analytics</span>

      </v-app-bar-title>

      <template #append>

        <v-btn-group
          variant="outlined"
          density="compact"
          class="mr-2"
        >

          <v-btn
            v-for="r in quickRanges"
            :key="r.label"
            :color="activeRange === r.label ? 'primary' : undefined"
            :variant="activeRange === r.label ? 'flat' : 'outlined'"
            size="small"
            @click="applyQuickRange(r)"
          >
            {{ r.label }}
          </v-btn>

        </v-btn-group>

        <v-text-field
          v-model="dateFrom"
          type="date"
          density="compact"
          variant="outlined"
          hide-details
          style="max-width: 150px"
          class="mr-2"
          @change=" activeRange = 'Custom' fetchAll() "
        />

        <span class="text-body-2 text-medium-emphasis mr-2">→</span>

        <v-text-field
          v-model="dateTo"
          type="date"
          density="compact"
          variant="outlined"
          hide-details
          style="max-width: 150px"
          class="mr-3"
          @change="
            activeRange = 'Custom'
            fetchAll()
          "
        />

        <v-btn
          icon="mdi-refresh"
          variant="text"
          :loading="loading"
          @click="fetchAll"
        />

      </template>

    </v-app-bar>

    <v-main>

      <v-container
        fluid
        class="pa-6"
      >

        <!-- KPI row -->

        <v-row
          dense
          class="mb-4"
        >

          <v-col
            v-for="kpi in kpis"
            :key="kpi.label"
            cols="12"
            sm="6"
            md="3"
          >

            <v-card
              variant="tonal"
              rounded="lg"
              height="100"
            >

              <v-card-text class="d-flex flex-column justify-space-between h-100 pa-4">

                <span class="text-caption text-medium-emphasis text-uppercase tracking-wide">
                  {{ kpi.label }}
                </span>

                <span class="text-h5 font-weight-medium">

                  <template v-if="loading">—</template>

                  <template v-else>{{ kpi.format(kpi.value) }}</template>

                </span>

              </v-card-text>

            </v-card>

          </v-col>

        </v-row>

        <!-- Revenue line chart -->

        <v-row class="mb-4">

          <v-col cols="12">

            <v-card
              variant="outlined"
              rounded="lg"
            >

              <v-card-title class="text-body-1 font-weight-medium pa-4 pb-0">
                Revenue over time
              </v-card-title>

              <v-card-text>

                <div
                  v-if="loading"
                  class="d-flex align-center justify-center"
                  style="height: 280px"
                >

                  <v-progress-circular
                    indeterminate
                    color="primary"
                  />

                </div>

                <div
                  v-else
                  style="position: relative; height: 280px"
                >

                  <canvas id="revenueChart" />

                </div>

              </v-card-text>

            </v-card>

          </v-col>

        </v-row>

        <!-- Donuts row -->

        <v-row>

          <v-col
            cols="12"
            md="6"
          >

            <v-card
              variant="outlined"
              rounded="lg"
              height="340"
            >

              <v-card-title class="text-body-1 font-weight-medium pa-4 pb-0">
                Sales by category
              </v-card-title>

              <v-card-text>

                <div
                  v-if="loading"
                  class="d-flex align-center justify-center"
                  style="height: 260px"
                >

                  <v-progress-circular
                    indeterminate
                    color="primary"
                  />

                </div>

                <div
                  v-else
                  class="d-flex align-center"
                  style="gap: 24px"
                >

                  <div style="position: relative; width: 180px; height: 180px; flex-shrink: 0">

                    <canvas id="categoryChart" />

                  </div>

                  <div style="flex: 1; font-size: 12px">

                    <div
                      v-for="(item, i) in categoryData"
                      :key="item.label"
                      class="d-flex align-center mb-2"
                      style="gap: 8px"
                    >

                      <span
                        :style="`width:10px;height:10px;border-radius:2px;background:${donutColors[i % donutColors.length]};flex-shrink:0`"
                      />

                      <span
                        class="text-body-2 text-truncate"
                        style="flex: 1"
                      >
                        {{ item.label }}
                      </span>

                      <span class="text-body-2 text-medium-emphasis">{{ fmt(item.value) }}</span>

                      <span class="text-caption text-medium-emphasis">
                        {{ pct(item.value, categoryTotal) }}
                      </span>

                    </div>

                  </div>

                </div>

              </v-card-text>

            </v-card>

          </v-col>

          <v-col
            cols="12"
            md="6"
          >

            <v-card
              variant="outlined"
              rounded="lg"
              height="340"
            >

              <v-card-title class="text-body-1 font-weight-medium pa-4 pb-0">
                Order status breakdown
              </v-card-title>

              <v-card-text>

                <div
                  v-if="loading"
                  class="d-flex align-center justify-center"
                  style="height: 260px"
                >

                  <v-progress-circular
                    indeterminate
                    color="primary"
                  />

                </div>

                <div
                  v-else
                  class="d-flex align-center"
                  style="gap: 24px"
                >

                  <div style="position: relative; width: 180px; height: 180px; flex-shrink: 0">

                    <canvas id="statusChart" />

                  </div>

                  <div style="flex: 1; font-size: 12px">

                    <div
                      v-for="(item, i) in statusData"
                      :key="item.label"
                      class="d-flex align-center mb-2"
                      style="gap: 8px"
                    >

                      <span
                        :style="`width:10px;height:10px;border-radius:2px;background:${statusColors[item.label] || donutColors[i % donutColors.length]};flex-shrink:0`"
                      />

                      <span
                        class="text-body-2 text-truncate"
                        style="flex: 1"
                      >
                        {{ item.label }}
                      </span>

                      <span class="text-body-2 text-medium-emphasis">{{ item.count }} orders</span>

                      <span class="text-caption text-medium-emphasis">
                        {{ pct(item.count, statusTotal) }}
                      </span>

                    </div>

                  </div>

                </div>

              </v-card-text>

            </v-card>

          </v-col>

        </v-row>

      </v-container>

    </v-main>

  </v-app>

</template>

