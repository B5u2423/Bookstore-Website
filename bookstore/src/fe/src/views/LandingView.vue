<script setup>
import CommonLayout from '@/components/CommonLayout.vue';
import ProductSlider from '@/components/products/ProductSlider.vue';
import ProductGrid from '@/components/products/ProductGrid.vue';
import Carousel from '@/components/common/Carousel.vue';
// Testing Hello World
// import HelloWorld from '@/components/HelloWorld.vue';

const products = [
  { id: 1, title: 'The Giver', author: 'Lois Lowry', price: '$4.19', image: 'https://example.com/giver.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 2, title: 'The Silent Patient', author: 'Alex Michaelides', price: '$5.19', image: 'https://example.com/silent.jpg' },
  { id: 3, title: 'Atomic Habits', author: 'James Clear', price: '$9.39', image: 'https://example.com/habits.jpg' }
]
</script>

<template>
  <CommonLayout>
    <!-- <HelloWorld /> -->
    <Carousel />
    <ProductSlider :products="products" title="Bestsellers" subtitle="Something"/>
    <ProductGrid :products="products" title="Showcase" subtitle="Show some books"/>
  </CommonLayout> 
</template>
