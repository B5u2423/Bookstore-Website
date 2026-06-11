<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { AdminService } from '@/api/admin-api'
import DoughnutChart from '@/components/charts/DoughnutChart.vue'
import LineChart from '@/components/charts/LineChart.vue'

const analytics = ref(null)
const loading = ref(false)
const selectedPeriod = ref('today')
const customStart = ref('')
const customEnd = ref('')
const showDropdown = ref(false)

const periodOptions = [
  { value: 'today', label: 'Hôm nay', range: 'TODAY', days: 1 },
  { value: 'last7', label: '7 ngày qua', range: 'LAST_7_DAYS', days: 7 },
  { value: 'last30', label: '30 ngày qua', range: 'LAST_30_DAYS', days: 30 },
  { value: 'this_week', label: 'Tuần này', range: 'THIS_WEEK' },
  { value: 'this_month', label: 'Tháng này', range: 'THIS_MONTH' },
  { value: 'custom', label: 'Tùy chọn ngày', range: 'CUSTOM' },
]

const selectedLabel = computed(() => {
  if (selectedPeriod.value === 'custom' && customStart.value && customEnd.value) {
    return `${customStart.value} → ${customEnd.value}`
  }
  return periodOptions.find((p) => p.value === selectedPeriod.value)?.label ?? ''
})

function toVNDate(d) {
  return d.toLocaleDateString('sv-SE', { timeZone: 'Asia/Ho_Chi_Minh' })
}

// watch(selectedPeriod, () => {
//   getAnalytics()
// })

async function selectPeriod(value) {
  selectedPeriod.value = value
  if (value !== 'custom') {
    showDropdown.value = false
    const res = await getAnalytics(buildParams(value))
  }
}

function buildParams(value) {
  const now = new Date()
  const today = toVNDate(now)

  if (value === 'today') return { range: 'TODAY', startDate: today, endDate: today }
  if (value === 'last7') {
    const s = new Date(now)
    s.setDate(s.getDate() - 6)
    return { range: 'LAST_7_DAYS', startDate: toVNDate(s), endDate: today }
  }
  if (value === 'last30') {
    const s = new Date(now)
    s.setDate(s.getDate() - 29)
    return { range: 'LAST_30_DAYS', startDate: toVNDate(s), endDate: today }
  }
  if (value === 'this_week') {
    const day = now.getDay() || 7
    const s = new Date(now)
    s.setDate(s.getDate() - day + 1)
    return { range: 'THIS_WEEK', startDate: toVNDate(s), endDate: today }
  }
  if (value === 'this_month') {
    const s = new Date(now.getFullYear(), now.getMonth(), 1)
    return { range: 'THIS_MONTH', startDate: toVNDate(s), endDate: today }
  }
  if (value === 'custom') {
    return { range: 'CUSTOM', startDate: customStart.value, endDate: customEnd.value }
  }
}

async function applyCustomRange() {
  if (!customStart.value || !customEnd.value) return
  showDropdown.value = false
  await getAnalytics(buildParams('custom'))
}

const periodsMeta = {
  today: { compareLabel: 'so với hôm qua', range: 'TODAY' },
  last7: { compareLabel: '', range: 'LAST_7_DAYS' },
  last30: { compareLabel: '', range: 'LAST_30_DAYS' },
  this_week: { compareLabel: 'so với tuần trước', range: 'LAST_7_DAYS' },
  this_month: { compareLabel: 'so với tháng trước', range: 'LAST_30_DAYS' },
  custom: { compareLabel: '', range: 'CUSTOM' },
}

async function getAnalytics(params) {
  try {
    loading.value = true
    analytics.value = await AdminService.getDashboardAnalytics(params)
  } finally {
    loading.value = false
  }
}

// and update onMounted:
onMounted(() => getAnalytics(buildParams('today')))

const compareLabel = computed(() => periodsMeta[selectedPeriod.value].compareLabel)

const currentCards = computed(() => {
  const a = analytics.value
  return [
    {
      key: 'revenue',
      label: 'Tổng doanh thu',
      value: a?.totalRevenue ? `₫${a.totalRevenue.toLocaleString('vi-VN')}` : '--',
      raw: a?.totalRevenue ?? 0,
      prev: a?.prevTotalRevenue ?? 0,
    },
    {
      key: 'orders',
      label: 'Tổng đơn hàng',
      value: a?.totalOrdersCount?.toLocaleString('vi-VN') ?? '--',
      raw: a?.totalOrdersCount ?? 0,
      prev: a?.prevTotalOrdersCount ?? 0,
    },
    {
      key: 'aov',
      label: 'Giá trị TB đơn hàng',
      value: a?.aov ? `₫${a.aov.toLocaleString('vi-VN')}` : '--',
      raw: a?.aov ?? 0,
      prev: a?.prevAov ?? 0,
    },
    {
      key: 'products',
      label: 'Sản phẩm đã bán',
      value: a?.totalItemsSold?.toLocaleString('vi-VN') ?? '--',
      raw: a?.totalItemsSold ?? 0,
      prev: a?.prevTotalItemsSold ?? 0,
    },
  ]
})

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

// line chart - revenue
const revenueLineData = computed(
() => {
  const a = analytics.value
  return {
  labels: a?.revenueChartData?.labels ?? ['N/A'],
  datasets: [
    {
      type: 'line',
      label: 'Doanh thu',
      data: a?.revenueChartData?.revenue ?? [0],
      yAxisID: 'y',
    },
    {
      type: 'bar',
      label: 'Số đơn hàng',
      data: a?.revenueChartData.orders ?? [0],
      yAxisID: 'y1',
    },
  ],
}
}
)


const revenueLineOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'top',
    },
    title: {
      display: true,
      text: 'Monthly Revenue',
    },
  },
  scales: {
    x: {
      ticks: {
        maxTicksLimit: 6,
      },
    },
    y: {
      type: 'linear',
      position: 'left',
      beginAtZero: true,
    },

    y1: {
      type: 'linear',
      position: 'right',
      // prevents duplicate grid lines
      grid: {
        drawOnChartArea: false,
      },
      beginAtZero: true,
    },
  },
}

// doughnut chart - order status
const orderDoughData = computed(() => {
  const a = analytics.value
  return {
    labels: ['Đã thanh toán', 'Chờ xử lý', 'Đã hủy'],
    datasets: [
      {
        data: [a?.paidOrders ?? 0, a?.pendingOrders ?? 0, a?.cancelledOrders ?? 0],
        backgroundColor: ['#185FA5', '#EF9F27', '#E24B4A'],
        borderColor: '#ffffff',
        borderWidth: 3,
        hoverOffset: 6,
      },
    ],
  }
})

const orderDoughOptions = {
  responsive: true,
  maintainAspectRatio: false,
  cutout: '68%',
  plugins: { legend: { display: true } },
}
</script>

<template>

  <v-container fluid>

    <v-progress-linear
      v-if="loading"
      indeterminate
      color="primary"
    />

    <template v-if="analytics">

      <div>

        <!-- Period selector -->

        <div class="d-flex align-center justify-space-between flex-wrap ga-2 mb-4">

          <span class="text-caption text-medium-emphasis">Xem theo</span>

          <v-menu
            v-model="showDropdown"
            :close-on-content-click="false"
            location="bottom start"
          >

            <template #activator="{ props }">

              <v-btn
                v-bind="props"
                variant="outlined"
                size="small"
                append-icon="mdi-chevron-down"
              >
                 {{ selectedLabel }}
              </v-btn>

            </template>

            <v-list
              density="compact"
              min-width="220"
            >

              <v-list-item
                v-for="opt in periodOptions.filter((p) => p.value !== 'custom')"
                :key="opt.value"
                :active="selectedPeriod === opt.value"
                @click="selectPeriod(opt.value)"
              >
                 {{ opt.label }}
              </v-list-item>

              <v-divider />

              <!-- Custom range -->

              <v-list-item
                :active="selectedPeriod === 'custom'"
                @click="selectedPeriod = 'custom'"
              >
                 Tùy chọn ngày
              </v-list-item>

              <template v-if="selectedPeriod === 'custom'">

                <div class="pa-3 d-flex flex-column ga-2">

                  <v-text-field
                    v-model="customStart"
                    label="Từ ngày"
                    type="date"
                    density="compact"
                    hide-details
                  />

                  <v-text-field
                    v-model="customEnd"
                    label="Đến ngày"
                    type="date"
                    density="compact"
                    hide-details
                  />

                  <v-btn
                    size="small"
                    color="primary"
                    block
                    @click="applyCustomRange"
                  >
                     Áp dụng
                  </v-btn>

                </div>

              </template>

            </v-list>

          </v-menu>

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

        <!-- Line Revenue -->
        <v-row>
          <v-col cols="12" md="12">
            <v-card flat>

              <v-card-title class="text-subtitle-1 font-weight-medium pt-4 px-4">
                Biểu đồ Doanh thu - Đơn hàng
              </v-card-title>

              <v-card-subtitle class="px-4">
                Phân phối trong khoảng thời gian
              </v-card-subtitle>


              <div
                style="
                  height: 400px;
                  position: relative;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                "
              >
                <line-chart
                  :data="revenueLineData"
                  :option="revenueLineOptions"
                />

              </div>

            </v-card>
          </v-col>
        </v-row>

        <!-- Doughnut Order Status and Bar Categories Trend -->
        <v-row>

          <v-col
            cols="12"
            md="6"
            sm="12"
          >

            <v-card flat>

              <v-card-title class="text-subtitle-1 font-weight-medium pt-4 px-4">
                 Biểu đồ Trạng thái
              </v-card-title>

              <v-card-subtitle class="px-4">Phân phối các đơn theo trạng thái</v-card-subtitle>

              <div
                style="
                  height: 400px;
                  position: relative;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                "
              >

                <doughnut-chart
                  :data="orderDoughData"
                  :option="orderDoughOptions"
                />

              </div>

            </v-card>

          </v-col>

          <v-col
            cols="12"
            md="6"
            sm="12"
          >

          </v-col>

        </v-row>

      </div>

    </template>

  </v-container>

</template>

