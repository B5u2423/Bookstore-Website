<script setup>
import { BookService } from '@/api/book-api';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()
const paginationLength = ref(1)

</script>

<template>
  <v-row>
    <v-col cols="12" md="3" v-for="i in 24">
      <v-card title="Hello" subtitle="NOt hello">
        <v-card-text>

        {{ route.params.slug }}
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>

  <v-pagination :length="paginationLength"></v-pagination>
</template>
