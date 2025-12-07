<script setup>
import { useRoute } from 'vue-router';

const route = useRoute()
</script>

<template>{{ route.params.slug }}</template>
