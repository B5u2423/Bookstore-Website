<script setup>
import HeaderSection from '@/components/common/HeaderSection.vue'
import FooterSection from '@/components/common/FooterSection.vue'
import BreadCrumbs from './common/BreadCrumbs.vue';

const props = defineProps(['items'])
</script>

<template>
  <HeaderSection />
  <BreadCrumbs :items="items" v-if="items?.length > 0"></BreadCrumbs>
  <slot></slot>
  <FooterSection />
</template>