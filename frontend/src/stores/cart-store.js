import { CartService } from '@/api/cart-api'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

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
      }, 0)
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

    async function syncCartWithBackEnd() {
      if (activeCart.value.length > 0) {
        // if FE cart is not empty: FE cart has higher priority
        const cartItems = activeCart.value.map(item => ({
          bookId: item.id,
          quantity: item.quantity,
        }))
        const res = await CartService.syncCartWithBackEnd(cartItems)
        console.log(res)
        return {
          status: res.status ?? 0,
          data: res.data ?? 'Lỗi không xác định',
        }
      } else {
        // if FE cart is empty: fetch from API
        const response = await CartService.getUsersActiveCart()
        const { items } = response
        // short circuit if the item list is empty
        if (!items) return
        // mapper
        items.map((item) => {
          addItemToLocalCart({
            id: item.bookId,
            title: item.bookTitle,
            author: item.bookAuthor,
            price: item.bookPrice,
            urlSlug: item.bookSlug,
            image: item.bookImage,
            quantity: item.quantity,
          })
        })
        return {
          status: 200,
          data: 'fetch',
        }
      }
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
