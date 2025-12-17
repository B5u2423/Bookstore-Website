<script setup>
import { formatPriceVNLocale } from '@/utils/utils'
const props = defineProps({
  product: Object,
})
</script>

<template>

  <v-card
    density="compact"
    variant="flat"
    min-width="100%"
    class="pa-1"
  >

    <div class="d-flex">

      <div>

        <v-img
          cover
          :src="product.image"
          width="75"
          height="75"
        ></v-img>

      </div>

      <div class="d-flex flex-column justify-center">

        <v-card-title class="pt-0">{{ product.title }}</v-card-title>

        <v-card-subtitle>
           {{ formatPriceVNLocale(product.price) }} x {{ product.quantity }} = {{
            formatPriceVNLocale(product.price * product.quantity)
          }} VND
        </v-card-subtitle>

      </div>

    </div>

  </v-card>

</template>

