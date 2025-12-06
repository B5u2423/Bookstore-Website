<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth-store'
import { useCartStore } from '@/stores/cart-store'
import { useUserProfileStore } from '@/stores/user-profile-store'

const router = useRouter()
const authStore = useAuthStore()
const cartStore = useCartStore()
const userProfileStore = useUserProfileStore()

const goToHomePage = () => {
  router.push('/')
}

async function handleLogout() {
  // do logout
  try {
    await authStore.logout()
    // reset the local cart
    cartStore.reset()
    // redirect to homepage
    router.push('/')
  } catch (error) {
    console.error(error)
  }
}
</script>

<template>

  <v-app-bar
    density="default"
    class="px-16"
  >

    <v-app-bar-title class="display-inline">

      <v-responsive
        max-width="333"
        max-height="39"
      >

        <h2
          class="cursor-pointer"
          @click="goToHomePage"
        >
           BookShelf
        </h2>

        <!-- <v-img
          src="https://theme.hstatic.net/200000845405/1001223012/14/logo.png?v=471"
          class="cursor-pointer"
          @click="goToHomePage"
        ></v-img> -->

      </v-responsive>

    </v-app-bar-title>

    <v-text-field
      max-width="666"
      placeholder="Search"
      variant="outlined"
      rounded
      density="comfortable"
      class="custom-search mr-10"
      clearable
      prepend-inner-icon="mdi-magnify"
      hide-details
    />

    <template v-slot:append>

      <template v-if="!authStore.isAuthenticated">

        <v-btn
          :active="false"
          :to="{ name: 'register' }"
        >
           Đăng ký
        </v-btn>

        <v-btn
          :active="false"
          :to="{ name: 'login' }"
        >
           Đăng nhập
        </v-btn>

      </template>

      <!-- IF LOGGED IN: AVATAR + MENU -->

      <v-menu
        location="bottom"
        v-else
      >

        <template v-slot:activator="{ props }">

          <v-btn
            color="primary"
            v-bind="props"
            prepend-icon="mdi-account"
          >
             {{ userProfileStore.userInfo.lastName }} {{ userProfileStore.userInfo.firstName }}
          </v-btn>

        </template>

        <v-list>

          <v-list-item :to="{ name: 'user-info' }">

            <v-list-item-title>

              <v-icon
                class="mx-2"
                icon="mdi-account"
              ></v-icon>
               Thông tin cá nhân
            </v-list-item-title>

          </v-list-item>

          <v-list-item @click="handleLogout">

            <v-list-item-title>

              <v-icon
                class="mx-2"
                icon="mdi-logout"
              ></v-icon>
               Đăng xuất
            </v-list-item-title>

          </v-list-item>

        </v-list>

      </v-menu>

      <v-badge
        class="mt-n1"
        :offset-x="6"
        :offset-y="6"
        location="top right"
        color="warning"
        :content="cartStore.cartItemsCount"
      >

        <v-btn
          :to="{ name: 'cart' }"
          icon="mdi-cart"
        ></v-btn>

      </v-badge>

      <v-btn icon="mdi-bell"></v-btn>

    </template>

  </v-app-bar>

  <!-- <v-app-bar
    class="bg-blue px-16"
    scroll-behavior="fully-hide"
    scroll-threshold="30"
    height="50"
  >

    <div class="ml-16">

      <v-btn>Sách </v-btn>

      <v-btn>Nổi bật</v-btn>

      <v-btn>Khuyến mãi</v-btn>

    </div>

    <v-spacer></v-spacer>

    <div class="mr-16 d-inline-flex align-center justify-center">

      <v-icon
        class="mx-1"
        icon="mdi-phone"
      ></v-icon>

      <h3>Hotline: 0923550726</h3>

    </div>

  </v-app-bar> -->

</template>

