<script setup>
const props = defineProps(['prop'])
</script>

<template>
    <v-container fill-height fluid class="pa-0" >
      <h2 class="text-center bg-grey-darken-1">{{ prop.name }}</h2>
      <v-breadcrumbs :items="prop.items" class="pa-2 justify-center align-center bg-grey-darken-1">
        <template v-slot:divider>
          <v-icon icon="mdi-chevron-right"></v-icon>
        </template>
      </v-breadcrumbs>
    </v-container>
</template>