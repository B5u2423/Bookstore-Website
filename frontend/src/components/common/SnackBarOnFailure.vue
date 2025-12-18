<script setup>
const props = defineProps({
  message: String,
  show: Boolean,
})

</script>

<template>

  <v-snackbar
    :model-value="show"
    color="error"
    timeout="3000"
    location="top"
  >
     {{ message }}
  </v-snackbar>

</template>

