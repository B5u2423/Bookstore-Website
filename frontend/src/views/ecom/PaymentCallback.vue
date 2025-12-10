<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<template>

  <v-card>

    <v-card-text>

      <template v-if="route.query.vnp_ResponseCode === '00'"> ok </template>

      <template v-else> not ok </template>

    </v-card-text>

    <v-card-actions class="justify-center align-center">

      <v-btn>Tiếp tục mua sắm</v-btn>

    </v-card-actions>

  </v-card>

</template>
