<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth-store'

const router = useRouter()
const authStore = useAuthStore()

const goToHomePage = () => {
  router.push({ name: 'landing' })
}

const goToCart = () => {
  router.push({ name: 'cart' })
}

async function handleLogout() {
  await authStore.logout()
  router.push('/')
}
</script>

<template>
  <v-toolbar class="bg-white ma-0">
    <v-container class="d-flex align-center">
        <!-- Brand icon -->
        <v-img  
          class="cursor-pointer"
          @click="goToHomePage"
          src="https://cdn.vuetifyjs.com/docs/images/brand-kit/v-logo-circle.svg" 
          alt="HomePage" 
          max-width="120"></v-img>

        <!-- Search bar -->
        <v-text-field
          append-inner-icon="mdi-magnify"
          placeholder="Search by title, author, or ISBN"
          hide-details
          variant="outlined"
          class="mx-5 flex-grow-1"
        />

        <!-- Icon buttons -->
        <v-btn prepend-icon="mdi-heart" stacked size="small">
          MY LIST
        </v-btn>

        <v-btn stacked size="small" @click="goToCart">
          <v-badge color="success" content="0">
            <v-icon icon="mdi-cart"></v-icon>
          </v-badge>
          CART
        </v-btn>

        <!-- Authentication section -->
        <template v-if="authStore.isAuthenticated">
          <!-- Authenticated user menu -->
          <v-menu>
            <template v-slot:activator="{ props }">
              <v-btn v-bind="props" text size="small">
                {{ authStore.user?.firstName || 'USER' }}
                <v-icon size="small">mdi-chevron-down</v-icon>
              </v-btn>
            </template>
            
            <v-list>
              <v-list-item 
                v-if="authStore.isCustomer"
                :to="'/account'" 
                prepend-icon="mdi-account-circle"
              >
                <v-list-item-title>Tài khoản của tôi</v-list-item-title>
              </v-list-item>
              
              <v-list-item 
                @click="handleLogout" 
                prepend-icon="mdi-logout"
              >
                <v-list-item-title>Đăng xuất</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </template>
        
        <template v-else>
          <!-- Guest user buttons -->
          <v-btn text size="small"
            :to="{ name: 'user-register' }">
            REGISTER
          </v-btn> |

          <v-btn text size="small"
            :to="{ name: 'user-login' }">
            LOGIN
          </v-btn>
        </template>
      </v-container>
  </v-toolbar>
</template>