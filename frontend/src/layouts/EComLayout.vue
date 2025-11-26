<script setup>
import NavBar from '@/components/ecom/NavBar.vue'
import FooterSection from '@/components/ecom/Footer.vue'
import SideBar from '@/components/ecom/SideBar.vue'
</script>

<template>

  <NavBar />

  <v-main class="bg-light-green-lighten-5 mt-3">

    <v-container class="bg-transparent">

      <v-row>

        <v-col
          cols="12"
          md="3"
        >

          <SideBar />

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

  <FooterSection />

</template>

