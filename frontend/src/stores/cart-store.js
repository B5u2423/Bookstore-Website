import { defineStore } from 'pinia'
import { computed, ref, watch } from 'vue'

export const useCartStore = defineStore(
  'cart',
  () => {
    // states
    const activeCart = ref([
      // {
      //   id: 1,
      //   slug: 'title-in-kebab',
      //   image:
      //     'https://raw.githubusercontent.com/gwenf/vuetify-responsive/master/public/img/products/product-1.jpg',
      //   title: 'Nintendo Switch',
      //   author: 'Author A',
      //   price: 240000,
      //   quantity: 1,
      // },
      // {
      //   id: 2,
      //   slug: 'title-in-kebab',
      //   image:
      //     'https://raw.githubusercontent.com/gwenf/vuetify-responsive/master/public/img/products/product-1.jpg',
      //   title: 'Nintendo Switch',
      //   author: 'Author A',
      //   price: 300000,
      //   quantity: 2,
      // },
    ])

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

    return {
      cartItemsCount,
      activeCart,
      totalAmount,
      removeItemFromCart,
    }
  },
  {
    persist: {
      storage: sessionStorage,
      paths: ['activeCart'],
    },
  },
)
