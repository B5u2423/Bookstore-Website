import { defineStore } from 'pinia'
import { computed, ref, watch } from 'vue'

export const useCartStore = defineStore('cart', () => {
  // states
  const activeCart = ref([])

  // temp cart on local storage
  const saved = localStorage.getItem('cart')
  if (saved) activeCart.value = JSON.parse(saved)

  // computed
  const cartItemsCount = computed(() => activeCart.value.length)

  watch(
    activeCart,
    () => {
      localStorage.setItem('cart', JSON.stringify(activeCart.value))
    },
    { deep: true },
  )

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
})
