<script setup>
import { BookService } from '@/api/book-api'
import { formatPriceVNLocale } from '@/utils/utils'
import { computed, onMounted, toRef, ref, shallowRef } from 'vue'
import { VFileUpload } from 'vuetify/labs/VFileUpload'

function createNewRecord() {
  return {
    title: '',
    author: '',
    isbn: '',
    productCode: '',
    pageCount: 1,
    inStock: 0,
    description: '',
    imageUrl: '',
    publisher: '',
    publishYear: '',
    price: '',
  }
}

// table
const headers = ref([
  { title: 'Tên sách', key: 'title', align: 'start' },
  { title: 'Mã ISBN', key: 'isbn', align: 'start' },
  { title: 'Tác giả', key: 'author', align: 'start' },
  { title: 'Nhà xuất bản', key: 'publisher', align: 'start' },
  { title: 'Mã sản phẩm', key: 'productCode', align: 'start' },
  { title: 'Giá sản phẩm (₫)', key: 'price', align: 'start' },
  { title: 'Số lượng trong kho', key: 'inStock', align: 'start' },
  { title: 'Thao tác', key: 'actions', align: 'end', sortable: false },
])
const itemsPerPage = ref(10)
const loading = ref(false)
const serverItems = ref([])
const totalItems = ref(0)

// edit-add dialog
const formModel = ref(createNewRecord())
const dialog = shallowRef(false)
const isEditing = toRef(() => !!formModel.value.id)

async function loadItems({ page, itemsPerPage }) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await BookService.fetchAllBooks({ page: page - 1, size: itemsPerPage })
    serverItems.value = payload.content
    totalItems.value = payload.page.totalElements
  } catch (error) {
    console.error('Error loading books from server', error)
  } finally {
    loading.value = false
  }
}

function add() {
  formModel.value = createNewRecord()
  dialog.value = true
}

function edit(id) {
  const found = serverItems.value.find((book) => book.id === id)

  formModel.value = {
    id: found.id,
    title: found.title,
    author: found.author,
    isbn: found.isbn,
    productCode: found.productCode,
    pageCount: found.pageCount,
    inStock: found.inStock,
    description: found.description,
    imageUrl: found.imageUrl,
    publisher: found.publisher,
    publishYear: found.publishYear,
    price: found.price,
  }

  dialog.value = true
}

function save() {
  console.log('saved')
}

// auto capitalize
function capitalizeVietnamese(str) {
  return str
    .trim()
    .replace(/\s+/g, ' ')
    .split(' ')
    .map((word) => word.charAt(0).toLocaleUpperCase('vi') + word.slice(1).toLocaleLowerCase('vi'))
    .join(' ')
}

const titleCaps = computed({
  get: () => formModel.value.title,
  set: (val) => {
    formModel.value.title = capitalizeVietnamese(val || '')
  },
})

const authorCaps = computed({
  get: () => formModel.value.author,
  set: (val) => {
    formModel.value.author = capitalizeVietnamese(val || '')
  },
})

const publisherCaps = computed({
  get: () => formModel.value.publisher,
  set: (val) => {
    formModel.value.publisher = capitalizeVietnamese(val || '')
  },
})

onMounted(() => {
  loadItems()
})
</script>

<template>

  <v-data-table-server
    v-model:items-per-page="itemsPerPage"
    :headers="headers"
    :items="serverItems"
    :items-length="totalItems"
    :loading="loading"
    item-value="title"
    items-per-page-text="Số sản phẩm hiển thị"
    @update:options="loadItems"
  >

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>

          <v-icon
            color="medium-emphasis"
            icon="mdi-book-multiple"
            size="x-small"
            start
          ></v-icon>
           Popular books
        </v-toolbar-title>

        <v-btn
          class="me-2"
          prepend-icon="mdi-plus"
          rounded="lg"
          text="Thêm sách"
          variant="outlined"
          @click="add"
        ></v-btn>

      </v-toolbar>

    </template>

    <!-- style the header -->

    <template v-slot:headers="{ columns }">

      <tr>

        <template
          v-for="column in columns"
          :key="column.key"
        >

          <th>

            <div class="d-flex align-center">

              <span
                class="me-2 cursor-pointer font-weight-bold"
                v-text="column.title"
              ></span>

            </div>

          </th>

        </template>

      </tr>

    </template>

    <!-- stylized book title as chips -->

    <template v-slot:item.title="{ value }">

      <v-chip
        :text="value"
        class="border-thin"
        prepend-icon="mdi-book"
        label
      >

        <template v-slot:prepend>

          <v-icon color="medium-emphasis"></v-icon>

        </template>

      </v-chip>

    </template>

    <!-- action buttons -->

    <template v-slot:item.actions="{ item }">

      <div class="d-flex ga-2 justify-end">

        <v-icon
          color="medium-emphasis"
          icon="mdi-pencil"
          size="small"
          @click="edit(item.id)"
        ></v-icon>

        <v-icon
          color="medium-emphasis"
          icon="mdi-delete"
          size="small"
        ></v-icon>

      </div>

    </template>

    <!-- format price-->

    <template v-slot:item.price="{ value }"> {{ formatPriceVNLocale(value) }} </template>

  </v-data-table-server>

  <v-dialog
    v-model="dialog"
    max-width="800"
  >

    <v-card
      :title="`${isEditing ? 'Thay đổi thông tin' : 'Tạo bản ghi mới'}`"
      :subtitle="`${isEditing ? 'Cập nhật' : 'Thêm'} sách`"
    >

      <template v-slot:text>

        <v-row class="px-3">

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Tên sách</div>

            <v-text-field
              variant="outlined"
              v-model="titleCaps"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Tác giả</div>

            <v-text-field
              variant="outlined"
              v-model="authorCaps"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">Mã ISBN</div>

            <v-text-field
              variant="outlined"
              v-model="formModel.isbn"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="2"
          >

            <div class="text-subtitle-1 text-high-emphasis">Số trang</div>

            <v-number-input
              class="rounded-lg"
              variant="outlined"
              v-model="formModel.pageCount"
              density="compact"
              :min="1"
              hide-details="true"
              control-variant="stacked"
            ></v-number-input>

          </v-col>

          <v-col
            cols="12"
            md="3"
          >

            <div class="text-subtitle-1 text-high-emphasis">Nhà xuất bản</div>

            <v-text-field
              variant="outlined"
              v-model="publisherCaps"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="3"
          >

            <div class="text-subtitle-1 text-high-emphasis">Năm xuất bản</div>

            <v-text-field
              variant="outlined"
              v-model="formModel.publishYear"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">Mã sản phẩm</div>

            <v-text-field
              variant="outlined"
              v-model="formModel.productCode"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="2"
          >

            <div class="text-subtitle-1 text-high-emphasis">Số lượng</div>

            <v-number-input
              class="rounded-lg"
              variant="outlined"
              v-model="formModel.inStock"
              density="compact"
              :min="0"
              hide-details="true"
              control-variant="stacked"
            ></v-number-input>

          </v-col>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Giá tiền (VND)</div>

            <v-text-field
              variant="outlined"
              v-model="formModel.price"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col cols="12">

            <div class="text-subtitle-1 text-high-emphasis">Mô tả thông tin sách</div>

            <v-textarea
              variant="outlined"
              v-model="formModel.description"
              density="compact"
              hide-details="true"
            ></v-textarea>

          </v-col>

          <v-col cols="12">

            <div class="text-subtitle-1 text-high-emphasis">Hình ảnh</div>

            <v-file-upload
              title="Kéo thả hoặc chọn hình ảnh"
              clearable
              density="compact"
              variant="compact"
            ></v-file-upload>

            <v-text-field
              class="mt-3"
              variant="outlined"
              v-model="formModel.imageUrl"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

        </v-row>

      </template>

      <v-divider></v-divider>

      <v-card-actions class="bg-surface-light">

        <v-btn
          color="green-darken-1"
          @click="save"
          variant="elevated"
        >
           Lưu
        </v-btn>

        <v-btn
          color="red-lighten-1"
          @click="dialog = false"
          variant="elevated"
        >
           Hủy
        </v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>

</template>

