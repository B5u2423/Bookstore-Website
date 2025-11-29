<script setup>
const categories = [
  { title: 'Sách kinh tế' },
  { title: 'Sách giáo khoa - giáo trình' },
  { title: 'Sách thường thức đời sống' },
  { title: 'Click Me 2' },
]

function capitalizeFirstword(sentence) {
  const words = sentence.split(' ')

  return words
    .map((word) => {
      return word[0].toUpperCase() + word.substring(1)
    })
    .join(' ')
}
</script>

<template>

  <v-card
    elevation="4"
    class="pa-0 align-center justify-center text-center mx-auto mb-6"
  >

    <v-card-title> Danh mục </v-card-title>

    <v-card-text class="pa-0">

      <v-list>

        <v-list-item
          density="compact"
          class="text-left"
          v-for="(category, i) in categories"
          link
          :key="i"
        >

          <v-list-item-title>{{ capitalizeFirstword(category.title) }}</v-list-item-title>

          <template v-slot:append>

            <v-icon
              icon="mdi-menu-right"
              size="small"
            ></v-icon>

          </template>

          <v-menu
            :offset="[6, 0]"
            activator="parent"
            open-on-click
            open-on-hover
            submenu
          >

            <v-list>

              <v-list-item
                link
                v-for="value in 3"
              >
                 Something
                <template v-slot:append>

                  <v-icon
                    icon="mdi-menu-right"
                    size="small"
                  ></v-icon>

                </template>

              </v-list-item>

            </v-list>

          </v-menu>

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

    </v-card-text>

  </v-card>

</template>

