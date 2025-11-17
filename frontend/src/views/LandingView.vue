<script setup>
import CommonLayout from '@/components/CommonLayout.vue'
import ProductSlider from '@/components/products/ProductSlider.vue'
import ProductGrid from '@/components/products/ProductGrid.vue'
import Carousel from '@/components/common/Carousel.vue'
import { useBookStore } from '@/stores/book-store'
import { onMounted } from 'vue'

const bookStore = useBookStore()

onMounted(() => {
  bookStore.fetchLandingPageBooks()
})

const products = []
</script>

<template>
   <CommonLayout
    > <!-- <HelloWorld /> --> <Carousel /> <ProductSlider
      :products="bookStore.bestSellersBooks"
      title="Bestsellers"
      subtitle="Something"
    /> <ProductGrid
      :products="bookStore.featuredBooks"
      title="Showcase"
      subtitle="Show some books"
    /> </CommonLayout
  >
</template>

