<script setup>
import { ref, onMounted, computed } from 'vue'
import { AdminService } from '@/api/admin-api'
import BarChart from '@/components/charts/BarChart.vue'
import DoughnutChart from '@/components/charts/DoughnutChart.vue'
import LineChart from '@/components/charts/LineChart.vue'

const metrics = ref(null)
const loading = ref(true)

async function getMetrics() {
  try {
    loading.value = true
    metrics.value = await AdminService.getDashboardMetrics()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getMetrics()
})

const defaultRevenue = {
  totalRevenue: 0,
  revenueToday: 0,
  revenueThisMonth: 0,
  averageOrderValue: 0,
}

const defaultUsers = {
  totalUsers: 0,
  customersCount: 0,
  staffCount: 0,
  adminsCount: 0,
  newUsersToday: 0,
  newUsersThisMonth: 0,
}

const defaultInventory = {
  totalBooks: 0,
  activeBooks: 0,
  outOfStockBooks: 0,
  lowStockBooks: 0,
  topSellingBooks: [],
  leastSellingBooks: [],
}

const defaultCatalog = {
  booksWithoutCategories: 0,
  booksWithoutCollections: 0,
  booksWithoutStock: 0,
  booksWithoutCoverImage: 0,
  booksAddedThisMonth: 0,
  inactiveBooks: 0,
  totalCategories: 0,
  totalCollections: 0,
  booksPerCategory: 0,
}

const defaultEngagement = {
  activeCarts: 0,
  cartAbandonmentRate: 0,
  ordersPerCustomer: 0,
}

const defaultOrders = {
  totalOrders: 0,
  ordersToday: 0,
  ordersThisMonth: 0,
  pendingOrders: 0,
  completedOrders: 0,
  cancelledOrders: 0,
}

const revenue = computed(() => metrics.value?.revenueMetricsDTO ?? defaultRevenue)

const users = computed(() => metrics.value?.userMetricsDTO ?? defaultUsers)

const inventory = computed(() => metrics.value?.inventoryMetricsDTO ?? defaultInventory)

const catalog = computed(() => metrics.value?.catalogHealthDTO ?? defaultCatalog)

const engagement = computed(() => metrics.value?.engagementMetricsDTO ?? defaultEngagement)

const orders = computed(() => metrics.value?.orderMetricsDTO ?? defaultOrders)

const formatCurrency = (value) =>
  value ? new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value) : '0'

const chartData = {
  labels: ['Jan', 'Feb', 'Mar', 'Apr'],
  datasets: [
    {
      label: 'Revenue',
      data: [1200, 1900, 1500, 2400],
    },
  ],
}

const chartOptions = {
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
</script>

<template>

  <v-container fluid>

    <v-progress-linear
      v-if="loading"
      indeterminate
      color="primary"
    />

    <template v-if="metrics">

      <!-- ===== SUMMARY CARDS ===== -->

      <v-row>

        <v-col
          cols="12"
          md="3"
        >

          <v-card height="100%">

            <v-card-title>💰 Doanh thu</v-card-title>

            <v-card-text>

              <div>
                 Tổng:
                <strong>{{ formatCurrency(revenue.totalRevenue) }}</strong>

              </div>

              <div>Hôm nay: {{ formatCurrency(revenue.revenueToday) }}</div>

              <div>Tháng này: {{ formatCurrency(revenue.revenueThisMonth) }}</div>

              <div>AOV: {{ formatCurrency(revenue.averageOrderValue) }}</div>

            </v-card-text>

          </v-card>

        </v-col>

        <v-col
          cols="12"
          md="3"
        >

          <v-card height="100%">

            <v-card-title>📦 Đơn hàng</v-card-title>

            <v-card-text>

              <div>
                 Tổng:
                <strong>{{ orders.totalOrders }}</strong>

              </div>

              <div>Hôm nay: {{ orders.ordersToday }}</div>

              <div>Tháng này: {{ orders.ordersThisMonth }}</div>

              <div>Chờ xử lý: {{ orders.pendingOrders }}</div>

            </v-card-text>

          </v-card>

        </v-col>

        <v-col
          cols="12"
          md="3"
        >

          <v-card height="100%">

            <v-card-title>👥 Người dùng</v-card-title>

            <v-card-text>

              <div>
                 Tổng:
                <strong>{{ users.totalUsers }}</strong>

              </div>

              <div>Khách hàng: {{ users.customersCount }}</div>

              <div>Nhân viên: {{ users.staffCount }}</div>

              <div>Admin: {{ users.adminsCount }}</div>

              <div>Mới hôm nay: {{ users.newUsersToday }}</div>

            </v-card-text>

          </v-card>

        </v-col>

        <v-col
          cols="12"
          md="3"
        >

          <v-card height="100%">

            <v-card-title>📚 Kho sách</v-card-title>

            <v-card-text>

              <div>
                 Tổng sách:
                <strong>{{ inventory.totalBooks }}</strong>

              </div>

              <div>Đang bán: {{ inventory.activeBooks }}</div>

              <div>Hết hàng: {{ inventory.outOfStockBooks }}</div>

              <div>Sắp hết: {{ inventory.lowStockBooks }}</div>

            </v-card-text>

          </v-card>

        </v-col>

      </v-row>

      <!-- ===== CATALOG HEALTH ===== -->

      <v-row>

        <v-col cols="12">

          <v-card>

            <v-card-title>🧩 Tình trạng danh mục</v-card-title>

            <v-card-text>

              <v-row>

                <v-col
                  cols="6"
                  md="3"
                >
                   Không danh mục: {{ catalog.booksWithoutCategories }}
                </v-col>

                <v-col
                  cols="6"
                  md="3"
                >
                   Không bộ sưu tập: {{ catalog.booksWithoutCollections }}
                </v-col>

                <v-col
                  cols="6"
                  md="3"
                >
                   Không tồn kho: {{ catalog.booksWithoutStock }}
                </v-col>

                <v-col
                  cols="6"
                  md="3"
                >
                   Không ảnh bìa: {{ catalog.booksWithoutCoverImage }}
                </v-col>

                <v-col
                  cols="6"
                  md="3"
                >
                   Sách mới tháng này: {{ catalog.booksAddedThisMonth }}
                </v-col>

                <v-col
                  cols="6"
                  md="3"
                >
                   Sách ngưng bán: {{ catalog.inactiveBooks }}
                </v-col>

                <v-col
                  cols="6"
                  md="3"
                >
                   Danh mục: {{ catalog.totalCategories }}
                </v-col>

                <v-col
                  cols="6"
                  md="3"
                >
                   Bộ sưu tập: {{ catalog.totalCollections }}
                </v-col>

              </v-row>

            </v-card-text>

          </v-card>

        </v-col>

      </v-row>

      <v-row>

        <v-col cols="6">

          <v-card>

            <v-card-title>Doughnut Chart</v-card-title>

            <v-card-text>

              <div style="height: 400px">

                <doughnut-chart
                  :data="chartData"
                  :options="chartOptions"
                />

              </div>

            </v-card-text>

          </v-card>

        </v-col>

        <v-col cols="6">

          <v-card>

            <v-card-title>Doughnut Chart</v-card-title>

            <v-card-text>

              <div style="height: 400px">

                <doughnut-chart
                  :data="chartData"
                  :options="chartOptions"
                />

              </div>

            </v-card-text>

          </v-card>

        </v-col>

      </v-row>

    </template>


  </v-container>

</template>

