import { defineStore } from 'pinia'
import { computed, ref, watch } from 'vue'

export const useCartStore = defineStore('cart', () => {
  // states
  const activeCart = ref([])

  // computed
  const cartItemsCount = computed(() => activeCart.value.length)

  // actions
  function addToCart() {
    activeCart.value.push({ name: 'Hello', title: 'World' })
    console.log(activeCart.value)
  }

  return {
    cartItemsCount,
    activeCart,
    addToCart,
  }
}, {
  persist: {
    storage: localStorage,
    paths: ['activeCart']
  }
})
