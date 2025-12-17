<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<template>

  <v-card>

    <v-card-text>

      <template v-if="route.query.vnp_ResponseCode === '00'"> ONLINE </template>

      <template v-else>NOT OK</template>

    </v-card-text>

    <v-card-actions class="justify-center align-center">

      <v-btn :to="{ name: 'landing' }">Tiếp tục mua sắm</v-btn>

    </v-card-actions>

  </v-card>

</template>

