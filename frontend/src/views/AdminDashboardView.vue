<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth-store';
import { useRouter } from 'vue-router';

const router = useRouter();
const authStore = useAuthStore();

const stats = ref({
  totalUsers: 0,
  totalBooks: 0,
  totalOrders: 0,
  revenue: 0
});

onMounted(async () => {
  // Check if user is admin
  if (!authStore.isAdmin) {
    router.push('/login');
    return;
  }
  
  // Fetch dashboard statistics
  await fetchDashboardStats();
});

async function fetchDashboardStats() {
  // TODO: Implement API calls to get dashboard statistics
  // For now, using mock data
  stats.value = {
    totalUsers: 125,
    totalBooks: 450,
    totalOrders: 89,
    revenue: 15750000
  };
}

function formatCurrency(amount) {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(amount);
}

function handleLogout() {
  authStore.logout();
  router.push('/admin/login');
}
</script>

<template>
  <v-app>
    <!-- Navigation Bar -->
    <v-app-bar color="primary" dark>
      <v-app-bar-title>
        <v-icon class="mr-2">mdi-view-dashboard</v-icon>
        Quản Trị Hệ Thống
      </v-app-bar-title>
      
      <v-spacer></v-spacer>
      
      <v-menu>
        <template v-slot:activator="{ props }">
          <v-btn icon v-bind="props">
            <v-icon>mdi-account-circle</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item>
            <v-list-item-title>{{ authStore.user?.firstName }} {{ authStore.user?.lastName }}</v-list-item-title>
            <v-list-item-subtitle>{{ authStore.user?.email }}</v-list-item-subtitle>
          </v-list-item>
          <v-divider></v-divider>
          <v-list-item @click="handleLogout">
            <v-list-item-title>
              <v-icon class="mr-2">mdi-logout</v-icon>
              Đăng xuất
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>

    <!-- Main Content -->
    <v-main>
      <v-container fluid>
        <v-row class="mb-4">
          <v-col>
            <h1 class="text-h4 font-weight-bold">Bảng Điều Khiển</h1>
            <p class="text-subtitle-1 text-grey-darken-1">Chào mừng quay trở lại, {{ authStore.user?.firstName }}!</p>
          </v-col>
        </v-row>

        <!-- Statistics Cards -->
        <v-row>
          <v-col cols="12" sm="6" md="3">
            <v-card color="blue-lighten-1" dark>
              <v-card-text>
                <div class="d-flex align-center">
                  <v-icon size="40" class="mr-4">mdi-account-group</v-icon>
                  <div>
                    <div class="text-h5 font-weight-bold">{{ stats.totalUsers }}</div>
                    <div class="text-subtitle-2">Tổng Người Dùng</div>
                  </div>
                </div>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" md="3">
            <v-card color="green-lighten-1" dark>
              <v-card-text>
                <div class="d-flex align-center">
                  <v-icon size="40" class="mr-4">mdi-book</v-icon>
                  <div>
                    <div class="text-h5 font-weight-bold">{{ stats.totalBooks }}</div>
                    <div class="text-subtitle-2">Tổng Sách</div>
                  </div>
                </div>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" md="3">
            <v-card color="orange-lighten-1" dark>
              <v-card-text>
                <div class="d-flex align-center">
                  <v-icon size="40" class="mr-4">mdi-cart</v-icon>
                  <div>
                    <div class="text-h5 font-weight-bold">{{ stats.totalOrders }}</div>
                    <div class="text-subtitle-2">Tổng Đơn Hàng</div>
                  </div>
                </div>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" md="3">
            <v-card color="purple-lighten-1" dark>
              <v-card-text>
                <div class="d-flex align-center">
                  <v-icon size="40" class="mr-4">mdi-currency-usd</v-icon>
                  <div>
                    <div class="text-h6 font-weight-bold">{{ formatCurrency(stats.revenue) }}</div>
                    <div class="text-subtitle-2">Doanh Thu</div>
                  </div>
                </div>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>

        <!-- Quick Actions -->
        <v-row class="mt-6">
          <v-col>
            <h2 class="text-h5 mb-4">Thao Tác Nhanh</h2>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="12" md="6" lg="4">
            <v-card class="pa-4 text-center" hover>
              <v-icon size="60" color="primary" class="mb-3">mdi-account-plus</v-icon>
              <v-card-title class="justify-center">Quản Lý Người Dùng</v-card-title>
              <v-card-text>
                Thêm, sửa, xóa và quản lý tài khoản người dùng
              </v-card-text>
              <v-card-actions class="justify-center">
                <v-btn color="primary" variant="elevated">
                  Quản Lý
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>

          <v-col cols="12" md="6" lg="4">
            <v-card class="pa-4 text-center" hover>
              <v-icon size="60" color="green" class="mb-3">mdi-book-plus</v-icon>
              <v-card-title class="justify-center">Quản Lý Sách</v-card-title>
              <v-card-text>
                Thêm sách mới, cập nhật thông tin và quản lý kho
              </v-card-text>
              <v-card-actions class="justify-center">
                <v-btn 
                  color="green" 
                  variant="elevated"
                  @click="router.push('/admin/books/add')"
                >
                  Thêm Sách Mới
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>

          <v-col cols="12" md="6" lg="4">
            <v-card class="pa-4 text-center" hover>
              <v-icon size="60" color="orange" class="mb-3">mdi-clipboard-list</v-icon>
              <v-card-title class="justify-center">Quản Lý Đơn Hàng</v-card-title>
              <v-card-text>
                Xem và xử lý các đơn hàng từ khách hàng
              </v-card-text>
              <v-card-actions class="justify-center">
                <v-btn color="orange" variant="elevated">
                  Quản Lý
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>

        <!-- Recent Activity -->
        <v-row class="mt-6">
          <v-col>
            <v-card>
              <v-card-title class="d-flex align-center">
                <v-icon class="mr-2">mdi-clock-outline</v-icon>
                Hoạt Động Gần Đây
              </v-card-title>
              <v-card-text>
                <v-list>
                  <v-list-item>
                    <v-list-item-title>Người dùng mới đăng ký: user@example.com</v-list-item-title>
                    <v-list-item-subtitle>5 phút trước</v-list-item-subtitle>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>Đơn hàng mới #12345 được tạo</v-list-item-title>
                    <v-list-item-subtitle>15 phút trước</v-list-item-subtitle>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>Sách "Tên Sách Mới" được thêm vào kho</v-list-item-title>
                    <v-list-item-subtitle>1 giờ trước</v-list-item-subtitle>
                  </v-list-item>
                </v-list>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>