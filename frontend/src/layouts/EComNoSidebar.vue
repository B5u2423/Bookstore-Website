<script setup>
import NavBar from '@/components/ecom/NavBar.vue'
import FooterSection from '@/components/ecom/Footer.vue'
</script>

<template>

  <NavBar />

  <v-main class="bg-light-green-lighten-5 mt-3">

    <router-view></router-view>

  </v-main>

  <FooterSection />

</template>
