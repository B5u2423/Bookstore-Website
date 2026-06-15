<script setup>
import FooterSection from '@/components/ecom/Footer.vue'
import NavBar from '@/components/ecom/NavBar.vue'
</script>

<template>
  <nav-bar />

  <v-main class="mt-3">
    <router-view></router-view>
  </v-main>

  <footer-section />
</template>
