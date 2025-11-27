<script setup>
const buttons = [
  {title: "Dashboard", icon: "mdi-view-dashboard", link: "/"},
  {title: "Dashboard", icon: "mdi-view-dashboard", link: "/"},
  {title: "Dashboard", icon: "mdi-view-dashboard", link: "/"},
  {title: "Dashboard", icon: "mdi-view-dashboard", link: "/"}
]
</script>

<template>

  <v-navigation-drawer color="blue" class="pa-4">

    <div> <h4>First section</h4> </div>
    <v-list>
      <v-list-item v-for="(item, i) in buttons" :key="i" :title="item.title" link :href="item.link" :prepend-icon="item.icon" density="compact"></v-list-item>
    </v-list>

    <v-divider></v-divider>

    <div> <h3>Something</h3> </div>

    <v-list-item
      link
      href="/"
      prepend-icon="mdi-view-dashboard"
      title="Dashboard"
    ></v-list-item>

    <v-list-item
      link
      href="/"
      prepend-icon="mdi-account-box"
      title="Account"
    ></v-list-item>

    <v-list-item
      link
      href="/"
      prepend-icon="mdi-gavel"
      title="Admin"
    ></v-list-item>
  </v-navigation-drawer>

</template>

