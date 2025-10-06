<template>
    <v-toolbar color="grey" density="comfortable">
      <v-container>
        <v-btn
          v-for="item in buttons" :key="item.name"
          :to="item.path"
        > {{ item.name }}</v-btn>
      </v-container>
    </v-toolbar>
</template>

<script setup>
const buttons = [
  { name: 'All Categories', path: '/' },
  { name: 'All Books', path: '/' },
  { name: 'Deals', path: '/' },
]
</script>