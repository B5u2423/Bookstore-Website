<script setup>
import CommonLayout from '@/components/CommonLayout.vue'

import { ref } from 'vue'

const tab = ref('description')
const rating = ref(4)
</script>

<template>
   <CommonLayout
    > <v-container class="py-8"
      > <v-row
        > <!-- Left: Book cover --> <v-col cols="12" md="4"
          > <v-img
            src="https://example.com/the-will-to-change.jpg"
            alt="The Will to Change: Men, Masculinity, and Love"
            class="rounded-lg"
            height="400"
            cover
          ></v-img
          > </v-col
        > <!-- Right: Book info --> <v-col cols="12" md="8"
          >
          <h2 class="font-weight-bold mb-1"> Book title </h2>

          <div class="text-grey mb-2">
             <span>Tác giả:</span> <strong>Đang cập nhật</strong> | <span>Thể loại:</span>
            <strong>Nonfiction</strong>
          </div>
           <v-rating v-model="rating" color="amber" half-increments readonly></v-rating> <v-card
            variant="flat"
            class="mt-4 pa-4 bg-grey-lighten-4 d-flex align-center justify-space-between"
            >
            <div class="text-h6 font-weight-bold">270.000 VND</div>
             <v-btn color="grey" variant="flat" disabled>Hết hàng</v-btn> </v-card
          > <v-divider class="my-4"></v-divider> <v-list density="compact"
            > <v-list-item>Publisher: </v-list-item> <v-list-item>Publication date: </v-list-item>
            <v-list-item>Edition: </v-list-item> <v-list-item>Language: </v-list-item> <v-list-item
              >Print length: </v-list-item
            > <v-list-item>ISBN-10: </v-list-item> <v-list-item>ISBN-13: </v-list-item> <v-list-item
              >Dimensions: </v-list-item
            > </v-list
          > </v-col
        > </v-row
      > <!-- Tabs for Description, Info, Reviews --> <v-tabs v-model="tab" class="mt-8"
        > <v-tab value="description">Mô tả</v-tab> <v-tab value="info">Thông tin</v-tab> <v-tab
          value="reviews"
          >Đánh giá</v-tab
        > </v-tabs
      > <v-tabs-window v-model="tab"
        > <!-- Description --> <v-tabs-window-item value="description"
          >
          <p class="mt-4">
             Lorem ipsum dolor, sit amet consectetur adipisicing elit. Vitae labore mollitia
            dolorem? Amet repellat tenetur voluptates, provident voluptate, sed fuga atque sapiente
            reprehenderit sit voluptatibus? Iusto possimus a aut rem.
          </p>
           </v-tabs-window-item
        > <!-- Info --> <v-tabs-window-item value="info"
          >
          <p class="mt-4">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur, maxime harum numquam
            eligendi voluptates illo delectus porro architecto iure recusandae cum saepe nam sunt
            alias dolorem assumenda similique ea minus?
          </p>
           </v-tabs-window-item
        > <!-- Reviews --> <v-tabs-window-item value="reviews"
          >
          <p class="mt-4">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Repellat quibusdam eos, aperiam
            quisquam expedita corrupti error vero quod ex facilis sed, dolorem saepe possimus minima
            facere voluptatibus, aliquid vel commodi.
          </p>
           </v-tabs-window-item
        > </v-tabs-window
      > </v-container
    > </CommonLayout
  >
</template>

<style scoped>
.text-grey {
  color: #666;
}
</style>
