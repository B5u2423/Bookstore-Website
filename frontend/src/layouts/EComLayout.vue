<script setup>
import NavBar from '@/components/ecom/NavBar.vue'
import FooterSection from '@/components/ecom/Footer.vue'
</script>

<template>
   <NavBar /> <v-main class="bg-light-green-lighten-5 mt-3"
    > <v-container class="bg-transparent"
      > <v-row
        > <v-col sm="3"> <v-sheet elevation="1">This is the category side bar</v-sheet> </v-col> <v-col> <router-view></router-view> </v-col>
        </v-row
      > </v-container
    > </v-main
  > <FooterSection />
</template>

