<script setup></script>

<template>

  <v-sheet
    class="d-flex align-center justify-center text-center mx-auto pa-5 mb-6"
    elevation="4"
    max-width="800"
    width="100%"
    rounded
  >

    <div>

      <h2 class="text-h5 text-black">Đăng nhập</h2>

      <div class="my-2">

        <v-text-field
          density="compact"
          min-width="400"
          label="Email"
          variant="outlined"
        ></v-text-field>

        <v-text-field
          density="compact"
          label="Mật khẩu"
          variant="outlined"
        ></v-text-field>

      </div>

      <v-btn
        color="green"
        class="mb-3"
        width="100%"
      >
         ĐĂNG NHẬP
      </v-btn>

      <p class="text-black"> Hoặc đăng nhập bằng </p>

      <v-btn
        color="blue"
        class="ma-2 pa-2"
        prepend-icon="mdi-facebook"
      >
         Facebook
      </v-btn>

      <v-btn
        color="red"
        class="ma-2 pa-2"
        prepend-icon="mdi-google"
      >
         Google
      </v-btn>

    </div>

  </v-sheet>

</template>

