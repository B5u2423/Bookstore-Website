<script setup>
const props = defineProps({
  snackbar: Object
})
</script>

<template>
    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      timeout="3000"
      location="top"
    >
       {{ snackbar.message }}
    </v-snackbar>
</template>