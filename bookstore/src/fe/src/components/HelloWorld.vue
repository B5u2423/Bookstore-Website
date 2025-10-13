<script setup>
import { ref, onMounted } from 'vue';

const msg = ref('')

onMounted(
  async () => {
    const response = await fetch('/api/greetings/hello')
    msg.value = await response.text()
  }
)
</script>

<template>
   <h1>{{ msg }}</h1>
</template>