<script setup>
import CommonLayout from '@/components/CommonLayout.vue';
import ProductSlider from '@/components/ProductSlider.vue';
const products = [
  {
    title: 'The Giver',
    author: 'Lois Lowry',
    price: '$4.19',
    image: 'https://example.com/giver.jpg'
  },
  {
    title: 'The Silent Patient',
    author: 'Alex Michaelides',
    price: '$5.19',
    image: 'https://example.com/silent.jpg'
  },
  {
    title: 'Atomic Habits',
    author: 'James Clear',
    price: '$9.39',
    image: 'https://example.com/habits.jpg'
  }
]
</script>

<template>
  <CommonLayout>
    <ProductSlider :products="products" title="Bestsellers" subtitle="Something"/>
  </CommonLayout> 
</template>
