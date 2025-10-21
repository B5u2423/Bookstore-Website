<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const msg = ref('')

onMounted(
  async () => {
    const response = await axios.get('/api/greetings/hello')
    msg.value = response.data
  }
)
</script>

<template>
   <h1>{{ msg }}</h1>
</template>