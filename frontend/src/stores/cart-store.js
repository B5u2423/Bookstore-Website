import { CartService } from '@/api/cart-api'
import { defineStore } from 'pinia'
import { computed, ref, watch } from 'vue'
import { useAuthStore } from './auth-store'
import Book from '@/views/admin/Book.vue'
import { BookService } from '@/api/book-api.js'

export const useCartStore = defineStore(
  'cart',
  () => {
    // states
    const activeCart = ref([])
    // computed
    const cartItemsCount = computed(() => activeCart.value.length)
    const totalAmount = computed(() =>
      activeCart.value.reduce((total, item) => {
        return total + item.price * item.quantity
      }, 0),
    )

    // actions
    function removeItemFromCart({ itemId: id }) {
      activeCart.value = activeCart.value.filter((item) => item.id !== id)
    }

    function addItemToLocalCart(item) {
      if (!item.quantity || item.quantity <= 0) return
      const addedItem = activeCart.value.find((i) => i.id === item.id)
      if (addedItem) {
        addedItem.quantity += item.quantity
      } else {
        activeCart.value.push(item)
      }
    }

    async function syncCartWithBackEnd({ token: accessToken }) {
      // if FE cart is not empty
      if (activeCart.value.length > 0) {
        const removeAllRes = await CartService.removeAllItemsFromCart(accessToken)
        const res = activeCart.value.map((item) =>
          CartService.addToCart(accessToken, { bookId: item.id, quantity: item.quantity }),
        )
      }
      const response = await CartService.getUsersActiveCart(accessToken)
      const { id, user, items, cartStatus } = response
      // short circuit if the item list is empty
      if (!items) return
      // mapper
      items.map((item) => {
        addItemToLocalCart({
          id: item.book.id,
          title: item.book.title,
          author: item.book.author,
          price: item.book.price,
          slug: item.book.urlSlug,
          image: item.book.imageUrl,
          quantity: item.quantity,
        })
      })
    }

    function reset() {
      // remove all items from the local frontend cart
      activeCart.value.length = 0
    }

    return {
      cartItemsCount,
      activeCart,
      totalAmount,
      removeItemFromCart,
      addItemToLocalCart,
      syncCartWithBackEnd,
      reset,
    }
  },
  {
    persist: {
      storage: localStorage,
      paths: ['activeCart'],
    },
  },
)
