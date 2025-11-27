import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCartStore = defineStore('cart', () => {
  const activeCart = ref([])

  function addToCart() {
    activeCart.value.push({ name: 'Hello', title: 'World' })
    console.log(activeCart.value)
  }

  return {
    activeCart,
    addToCart,
  }
})
