<script setup>

const props = defineProps(['title', 'subtitle', 'products'])
</script>

<template>
  <v-container>
    <h2>{{ title }}</h2>
    <p>{{ subtitle }}</p>
    <v-slide-group show-arrows>
      <v-slide-group-item
        v-for="(product, index) in products"
        :key="index"
      >
        <v-card class="ma-2" width="200" hover>
          <v-img
            :src="product.image"
            height="250"
            cover
          ></v-img>
          
          <v-card-title>{{ product.title }}</v-card-title>
          <v-card-subtitle>{{ product.author }}</v-card-subtitle>
          <v-card-text>
            From: <strong>{{ product.price }}</strong>
          </v-card-text>
        </v-card>
      </v-slide-group-item>
    </v-slide-group>
  </v-container>
</template>