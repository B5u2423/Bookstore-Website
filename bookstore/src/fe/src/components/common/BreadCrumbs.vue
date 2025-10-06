<script setup>
const props = defineProps(['items'])
</script>

<template>
    <v-container fill-height fluid>
      <v-breadcrumbs :items="items">
        <template v-slot:divider>
          <v-icon icon="mdi-chevron-right"></v-icon>
        </template>
      </v-breadcrumbs>
    </v-container>
</template>