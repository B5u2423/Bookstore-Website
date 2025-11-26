<script setup>
const categories = [
  { title: 'Somehting category' },
  { title: 'Click Me' },
  { title: 'Click Me' },
  { title: 'Click Me 2' },
]
</script>

<template>

  <v-sheet
    elevation="4"
    class="pa-0 align-center justify-center text-center mx-auto mb-6"
  >
     Danh mục
    <v-list>

      <v-list-item
        v-for="(category, i) in categories"
        :key="i"
        :title="category.title"
      >

        <template v-slot:append>

          <v-icon
            icon="mdi-menu-right"
            size="small"
          ></v-icon>

        </template>

      </v-list-item>

    </v-list>

    <!-- <v-menu location="end" open-on-hover scroll-strategy="close"
              > <template v-slot:activator="{ props }"
                > <v-btn variant="plain" width="100%" color="primary" v-bind="props"> Dropdown </v-btn> 
                </template
              > <v-list class="ml-1"
                > <v-list-item v-for="(item, index) in items" :key="index" :value="index"
                  > <v-list-item-title>{{ item.title }}</v-list-item-title
                  > </v-list-item
                > </v-list
              > </v-menu> -->

  </v-sheet>

</template>

