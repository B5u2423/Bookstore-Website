<script setup>
import FooterSection from '@/components/ecom/Footer.vue'
import NavBar from '@/components/ecom/NavBar.vue'
import SideBar from '@/components/ecom/SideBar.vue'
</script>

<template>
  <nav-bar />

  <div class="ecom-layout">
    <v-main class="mt-3" style="background-color: var(--paper);">
      <v-container min-width="1400">
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
  </div>

  <footer-section />
</template>

<style scoped>
.ecom-layout {
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;
}
</style>
