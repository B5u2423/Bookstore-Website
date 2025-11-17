<script setup>
import HeaderSection from '@/components/common/HeaderSection.vue'
import FooterSection from '@/components/common/FooterSection.vue'
import BreadCrumbs from './common/BreadCrumbs.vue'

const props = defineProps(['prop'])
</script>

<template>
   <HeaderSection /> <BreadCrumbs :prop="prop" v-if="prop?.items.length > 0"></BreadCrumbs>
  <v-container fluid class="pa-4"
    > <v-main height="90%"> <slot></slot> </v-main> </v-container
  > <FooterSection />
</template>

