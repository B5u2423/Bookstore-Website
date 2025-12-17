<script setup>
import { BookService } from '@/api/book-api'
import { useCartStore } from '@/stores/cart-store'
import { formatPriceVNLocale } from '@/utils/utils'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth-store.js'
import { CartService } from '@/api/cart-api.js'

const cartStore = useCartStore()
const authStore = useAuthStore()

const route = useRoute()
const quantity = ref(1)
const book = ref({
  author: '',
  description: '',
  id: '',
  imageUrl: '',
  inStock: '',
  isbn: '',
  pageCount: '',
  price: '',
  publishYear: '',
  publisher: '',
  title: '',
  urlSlug: '',
})

async function loadBookDetail() {
  try {
    book.value = await BookService.fetchBookById(route.params.id)
  } catch (error) {
    console.error('Error fetching book', error)
  }
  // or const response = await fetch(`/api/books/${route.params.id}/${route.params.slug}`)

  // Optional: Validate that the loaded book's slug matches the URL slug
  // if (book.value.urlSlug !== route.params.slug) {
  //   // Handle mismatch - maybe redirect to 404 or correct URL
  // }
}

onMounted(() => {
  loadBookDetail()
})

function handleAddToCart() {
  cartStore.addItemToLocalCart({
    id: book.value.id,
    title: book.value.title,
    author: book.value.author,
    price: book.value.price,
    slug: book.value.urlSlug,
    image: book.value.imageUrl,
    quantity: quantity.value,
  })
  // call the endpoint directly
  if (authStore.isAuthenticated) {
    try {
      const res = CartService.addToCart(authStore.accessToken, {
        bookId: book.value.id,
        quantity: quantity.value,
      })
    } catch (error) {
      console.error('Error adding new book when already logged in')
    }
  }
}
</script>

<template>

  <v-container class="mb-8">

    <v-row>

      <v-col
        md="4"
        class="bg-white"
      >

        <v-img
          cover
          :src="book.imageUrl"
        />

      </v-col>

      <v-col
        md="6"
        offset="1"
        class="bg-white"
      >

        <v-row>

          <v-col md="7">

            <v-card
              variant="flat"
              class="bg-white"
            >

              <v-card-title> {{ book.title }}</v-card-title>

              <v-card-subtitle>

                <div>Tác giả: {{ book.author }}</div>

              </v-card-subtitle>

              <v-card-text>

                <v-card
                  variant="flat"
                  class="bg-transparent"
                >

                  <v-card-title class="bg-red">
                     {{ formatPriceVNLocale(book.price) }} VND
                  </v-card-title>

                  <v-card-text class="mt-3">

                    <v-list class="pa-0">

                      <v-list-item> ISBN: {{ book.isbn }} </v-list-item>

                      <v-list-item> Nhà xuất bản: {{ book.publisher }} </v-list-item>

                      <v-list-item> Năm xuất bản: {{ book.publishYear }} </v-list-item>

                      <v-list-item> Số lượng trong kho: {{ book.inStock }} </v-list-item>

                    </v-list>

                  </v-card-text>

                </v-card>

              </v-card-text>

            </v-card>

          </v-col>

          <v-col md="5">

            <v-card
              variant="flat"
              class="bg-transparent"
            >

              <v-card-text>

                <div class="d-flex align-center">

                  <v-btn
                    size="small"
                    variant="text"
                    icon="mdi-minus"
                    @click="quantity > 1 ? quantity-- : 1"
                  ></v-btn>

                  <v-text-field
                    v-model="quantity"
                    type="number"
                    style="width: 60px"
                    :min="1"
                    density="compact"
                    hide-details
                    class="mx-2"
                  ></v-text-field>

                  <v-btn
                    size="small"
                    variant="text"
                    icon="mdi-plus"
                    @click="quantity++"
                  ></v-btn>

                </div>

              </v-card-text>

              <v-card-actions>

                <v-btn
                  class="bg-red"
                  @click="handleAddToCart"
                >
                   Thêm vào giỏ hàng
                </v-btn>

              </v-card-actions>

            </v-card>

          </v-col>

        </v-row>

      </v-col>

    </v-row>

    <v-row class="bg-white mt-8">

      <v-sheet class="mt-4 pa-4"> {{ book.description }} </v-sheet>

    </v-row>

  </v-container>

</template>

