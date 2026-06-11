<script setup>
import { ref, onMounted, computed } from 'vue'
import { AdminService } from '@/api/admin-api'
import DoughnutChart from '@/components/charts/DoughnutChart.vue'
import LineChart from '@/components/charts/LineChart.vue'
import BarChart from '@/components/charts/BarChart.vue'

const metrics = ref(true)
const loading = ref(false)
const chartHeight = ref('400px')

async function getMetrics() {
  try {
    loading.value = true
    metrics.value = await AdminService.getDashboardMetrics()
  } finally {
    loading.value = false
  }
}

onMounted(() => {})

const formatCurrency = (value) =>
  value ? new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value) : '0'

const selectedPeriod = ref('today')

const periods = {
  today: {
    compareLabel: 'so với hôm qua',
    cards: [
      { key: 'revenue', label: 'Tổng doanh thu', value: '₫4,320,000', raw: 4320000, prev: 3800000 },
      { key: 'orders', label: 'Tổng đơn hàng', value: '187', raw: 187, prev: 162 },
      { key: 'aov', label: 'Giá trị TB đơn hàng', value: '₫23,100', raw: 23100, prev: 23457 },
      { key: 'products', label: 'Sản phẩm đã bán', value: '412', raw: 412, prev: 390 },
    ],
  },
  week: {
    compareLabel: 'so với tuần trước',
    cards: [
      {
        key: 'revenue',
        label: 'Tổng doanh thu',
        value: '₫18,750,000',
        raw: 18750000,
        prev: 16200000,
      },
      { key: 'orders', label: 'Tổng đơn hàng', value: '821', raw: 821, prev: 744 },
      { key: 'aov', label: 'Giá trị TB đơn hàng', value: '₫22,840', raw: 22840, prev: 21774 },
      { key: 'products', label: 'Sản phẩm đã bán', value: '1,934', raw: 1934, prev: 1810 },
    ],
  },
  month: {
    compareLabel: 'so với tháng trước',
    cards: [
      {
        key: 'revenue',
        label: 'Tổng doanh thu',
        value: '₫74,200,000',
        raw: 74200000,
        prev: 67100000,
      },
      { key: 'orders', label: 'Tổng đơn hàng', value: '3,241', raw: 3241, prev: 2988 },
      { key: 'aov', label: 'Giá trị TB đơn hàng', value: '₫22,894', raw: 22894, prev: 22453 },
      { key: 'products', label: 'Sản phẩm đã bán', value: '7,812', raw: 7812, prev: 7204 },
    ],
  },
}

const currentCards = computed(() => periods[selectedPeriod.value].cards)
const compareLabel = computed(() => periods[selectedPeriod.value].compareLabel)

function deltaPercent(kpi) {
  if (!kpi.prev) return 0
  return Math.round(((kpi.raw - kpi.prev) / kpi.prev) * 100)
}

function deltaIcon(kpi) {
  const d = deltaPercent(kpi)
  return d > 0 ? 'mdi-trending-up' : d < 0 ? 'mdi-trending-down' : 'mdi-minus'
}

function deltaClass(kpi) {
  const d = deltaPercent(kpi)
  return d > 0 ? 'text-success' : d < 0 ? 'text-error' : 'text-medium-emphasis'
}

// revenue chart
const revenueLineData = {
  labels: ['Jan', 'Feb', 'Mar', 'Apr'],
  datasets: [
    {
      label: 'Revenue',
      data: [1200, 1900, 1500, 2400],
    },
  ],
}

const revenueLineOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top',
    },
    title: {
      display: true,
      text: 'Monthly Revenue',
    },
  },
}
const orderLegend = [
  { label: 'Paid', color: '#185FA5', count: 210, pct: 62 },
  { label: 'Pending', color: '#EF9F27', count: 85, pct: 25 },
  { label: 'Cancelled', color: '#E24B4A', count: 45, pct: 13 },
]

const orderDoughData = {
  labels: ['Paid', 'Pending', 'Cancelled'],
  datasets: [
    {
      data: [210, 85, 45],
      backgroundColor: ['#185FA5', '#EF9F27', '#E24B4A'],
      borderColor: '#ffffff',
      borderWidth: 3,
      hoverOffset: 6,
    },
  ],
}

const orderDoughOptions = {
  responsive: true,
  maintainAspectRatio: false,
  cutout: '68%',
  plugins: { legend: { display: false } },
}

const cateBarData = {
  labels: ['Fiction', 'Self-help', 'Science', 'History', 'Children', 'Business'],
  datasets: [
    {
      label: 'Books sold',
      data: [142, 118, 95, 74, 63, 58],
      backgroundColor: '#185FA5',
      hoverBackgroundColor: '#378ADD',
      borderRadius: 5,
      borderSkipped: false,
    },
  ],
}

const cateBarOptions = {
  responsive: true,
  maintainAspectRatio: false,
  indexAxis: 'y',
  plugins: { legend: { display: false } },
  scales: {
    x: {
      grid: { color: 'rgba(0,0,0,0.07)' },
      ticks: { font: { size: 11 } },
      border: { display: false },
    },
    y: { grid: { display: false }, ticks: { font: { size: 12 } }, border: { display: false } },
  },
}
</script>

<template>

  <v-container fluid>

    <v-progress-linear
      v-if="loading"
      indeterminate
      color="primary"
    />

    <template v-if="metrics">

      <div>

        <!-- Period selector -->

        <div class="d-flex align-center justify-space-between flex-wrap ga-2 mb-4">

          <span class="text-caption text-medium-emphasis">Xem theo</span>

          <v-btn-toggle
            v-model="selectedPeriod"
            density="compact"
            variant="outlined"
            divided
            mandatory
          >

            <v-btn
              value="today"
              size="small"
            >
               Hôm nay
            </v-btn>

            <v-btn
              value="week"
              size="small"
            >
               Tuần này
            </v-btn>

            <v-btn
              value="month"
              size="small"
            >
               Tháng này
            </v-btn>

          </v-btn-toggle>

        </div>

        <!-- KPI cards -->

        <v-row dense>

          <v-col
            v-for="kpi in currentCards"
            :key="kpi.key"
            cols="12"
            sm="6"
            md="3"
          >

            <v-card
              flat
              rounded="lg"
              variant="outlined"
              class="pa-4"
            >

              <div class="text-caption text-medium-emphasis font-weight-medium text-uppercase mb-2">
                 {{ kpi.label }}
              </div>

              <div class="text-h5 font-weight-medium text-high-emphasis"> {{ kpi.value }} </div>

              <div
                class="d-flex align-center ga-1 mt-2 text-caption font-weight-medium"
                :class="deltaClass(kpi)"
              >

                <v-icon size="14">{{ deltaIcon(kpi) }}</v-icon>
                 {{ Math.abs(deltaPercent(kpi)) }}%
              </div>

              <div class="text-caption text-disabled mt-1"> {{ compareLabel }} </div>

            </v-card>

          </v-col>

        </v-row>

      </div>

      <!-- revenue line chart -->

      <v-row>

        <v-col cols="12">

          <v-card>

            <v-card-text>

              <line-chart
                :data="revenueLineData"
                :options="revenueLineOptions"
              />

            </v-card-text>

          </v-card>

        </v-col>

      </v-row>

      <!-- order status doughnut and categories bar chart  -->

      <v-row>

        <v-col
          cols="12"
          md="6"
        >

          <v-card
            rounded="lg"
            border
          >

            <v-card-title class="text-subtitle-1 font-weight-medium pt-4 px-4">
               Order status
            </v-card-title>

            <v-card-subtitle class="px-4">Distribution of all orders</v-card-subtitle>

            <v-card-text>

              <div
                style="
                  position: relative;
                  height: 200px;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                "
              >

                <doughnut-chart
                  :data="orderDoughData"
                  :options="orderDoughOptions"
                />

                <div style="position: absolute; text-align: center; pointer-events: none">

                  <div class="text-h6 font-weight-medium">340</div>

                  <div class="text-caption text-medium-emphasis">total orders</div>

                </div>

              </div>

              <div class="d-flex flex-wrap ga-3 mt-3">

                <div
                  v-for="item in orderLegend"
                  :key="item.label"
                  class="d-flex align-center ga-2"
                >

                  <div
                    :style="{
                      background: item.color,
                      width: '10px',
                      height: '10px',
                      borderRadius: '50%',
                    }"
                  />

                  <span class="text-caption text-medium-emphasis">
                     {{ item.label }} — {{ item.count }}
                  </span>

                </div>

              </div>

              <v-row
                dense
                class="mt-2"
              >

                <v-col
                  v-for="item in orderLegend"
                  :key="item.label"
                  cols="4"
                >

                  <div class="text-center pa-2 rounded bg-surface-variant">

                    <div
                      class="text-subtitle-1 font-weight-medium"
                      :style="{ color: item.color }"
                    >
                       {{ item.pct }}%
                    </div>

                    <div class="text-caption text-medium-emphasis">
                       {{ item.label.toLowerCase() }}
                    </div>

                  </div>

                </v-col>

              </v-row>

            </v-card-text>

          </v-card>

        </v-col>

        <v-col
          cols="12"
          md="6"
        >

          <v-card
            rounded="lg"
            border
          >

            <v-card-title class="text-subtitle-1 font-weight-medium pt-4 px-4">
               Trending categories
            </v-card-title>

            <v-card-subtitle class="px-4">Books sold by genre this month</v-card-subtitle>

            <v-card-text>

              <div style="height: 240px">

                <bar-chart
                  :data="cateBarData"
                  :options="cateBarOptions"
                />

              </div>

            </v-card-text>

          </v-card>

        </v-col>

      </v-row>

    </template>

  </v-container>

</template>

