<script setup>
import FooterSection from '@/components/ecom/Footer.vue'
import NavBar from '@/components/ecom/NavBar.vue'
import SideBar from '@/components/ecom/SideBar.vue'
</script>

<template>
  <nav-bar />

  <v-main class="bg-light-green-lighten-5 mt-3">
    <v-container
      class="mb-8 bg-transparent"
      min-width="1400"
    >
      <v-row>
        <v-col
          cols="12"
          md="3"
        >
          <side-bar />
        </v-col>

        <v-col
          cols="12"
          md="9"
        >
          <router-view></router-view>
        </v-col>
      </v-row>
    </v-container>
  </v-main>

  <footer-section />
</template>
