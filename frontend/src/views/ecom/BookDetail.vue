<script setup></script>

<template>

  <v-container class="mb-8">

    <v-row>

      <v-col md="4">

        <v-img
          cover
          src="https://product.hstatic.net/200000845405/product/_o-_m---l_ch-s_-m_-thu_t-trung-hoa_1_0a623d0805aa4d7096b9832426c304d0_master.jpg"
        />

      </v-col>

      <v-col md="8">

        <v-row class="bg-blue">

          <v-col md="5">

            <v-card
              variant="flat"
              class="bg-transparent"
            >

              <v-card-title> Book title </v-card-title>

              <v-card-subtitle>

                <div>Tác giả</div>

                <div>Thể loại</div>

              </v-card-subtitle>

              <v-card-text>

                <v-card
                  variant="flat"
                  class="bg-transparent"
                >

                  <v-card-title class="bg-red">PRICE: 300.000VND</v-card-title>

                  <v-card-text> Description list </v-card-text>

                </v-card>

              </v-card-text>

            </v-card>

          </v-col>

          <v-col md="5">

            <v-card
              variant="flat"
              class="bg-transparent"
            >

              <v-card-actions>

                <v-btn class="bg-red">Thêm vào giỏ hàng</v-btn>

              </v-card-actions>

            </v-card>

          </v-col>

        </v-row>

      </v-col>

    </v-row>

    <v-row> Mô tả something here </v-row>

  </v-container>

</template>

