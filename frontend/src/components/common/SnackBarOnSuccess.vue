<script setup>
const props = defineProps({
  message: String,
  show: Boolean,
})
const emit = defineEmits(['update:show'])
</script>

<template>

  <v-snackbar
    :model-value="show"
    @update:model-value="emit('update:show', $event)"
    color="success"
    timeout="3000"
    location="top"
  >
     {{ message }}
  </v-snackbar>

</template>

