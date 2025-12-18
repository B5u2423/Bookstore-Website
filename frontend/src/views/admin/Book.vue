<script setup>
import { BookService } from '@/api/book-api'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { formatPriceVNLocale } from '@/utils/utils'
import { computed, onMounted, toRef, ref, shallowRef, onUpdated, isShallow } from 'vue'
import { VFileUpload } from 'vuetify/labs/VFileUpload'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'
import { CategoryService } from '@/api/category-api'

const adminAuthStore = useAdminAuthStore()

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
    categoryId: '',
    categoryName: '',
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
const candidates = ref([])

// edit-add dialog
const formModel = ref(createNewRecord())
const dialog = shallowRef(false)
const isEditing = toRef(() => !!formModel.value.id)
const imageUrlToggleEdit = ref(true)
const imageFile = ref(null)

// confirmation dialog
const confirmationDialog = shallowRef(false)
const itemId = ref('')
const isDelLoading = ref(false)

// snackbars
const isError = ref(false)
const isSuccess = ref(false)
const message = ref('')

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

async function edit(id) {
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
    categoryId: found.categoryId,
    categoryName: found.categoryName,
  }

  dialog.value = true
}

function confirm(id) {
  confirmationDialog.value = true
  itemId.value = id
}

async function remove() {
  isDelLoading.value = true
  try {
    const res = await BookService.deleteBookById(itemId.value, adminAuthStore.accessToken)
    // update on frontend, just for immediate view
    const index = serverItems.value.findIndex((book) => book.id === itemId.value)
    serverItems.value.splice(index, 1)
    totalItems.value--
  } catch (error) {
    console.error(`Error deleting book with id ${id}`, error)
  } finally {
    isDelLoading.value = false
    confirmationDialog.value = false
  }
}

function buildFormData() {
  const formData = new FormData()

  // append file only if selected
  if (imageFile.value) {
    formData.append("imageFile", imageFile.value)
  }

  // append other fields
  Object.entries(formModel.value).forEach(([key, value]) => {
    if (value !== null && value !== undefined) {
      formData.append(key, value)
    }
  })

  return formData 
}

async function save() {
  if (isEditing.value) {
    try {
      // build form
      const formData = buildFormData()
      // API call
      const res = await BookService.updateBookById(
        formData,
        formModel.value.id,
        adminAuthStore.accessToken,
      )
      // success snack bar
      isSuccess.value = true
      message.value = 'Cập nhật sản phẩm thành công'
      // edit immediate view
      const index = serverItems.value.findIndex((book) => book.id === formModel.value.id)
      serverItems.value[index] = formModel.value
    } catch (error) {
      // error snack bar
      isError.value = true
      message.value = `Lỗi cập nhật sản phẩm ${error.message}`
      console.error('Error editing book')
    }
  } else {
    // API call
    try {
      // build form
      const formData = buildFormData()
      // API call
      const res = await BookService.addNewBook(formData, adminAuthStore.accessToken)
      isSuccess.value = true
      message.value = 'Thêm sản phẩm thành công'
    } catch (error) {
      isError.value = true
      message.value = `Lỗi thêm mới sản phẩm ${error.message}`
      console.error('Error adding new book')
    }
    // update the immediate view
    formModel.value.id = totalItems.value++
    serverItems.value.push(formModel.value)
    totalItems.value++
  }

  dialog.value = false
}

async function fetchCategoryCandidates() {
  try {
    const res = await CategoryService.fetchChildrenCategories()
    candidates.value = res
  } catch (error) {
    console.error('Error fetching categories that is not root')
  }
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
  fetchCategoryCandidates()
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
           Thông tin sách
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
          @click="confirm(item.id)"
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

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Thể loại</div>

            <v-autocomplete
              v-model="formModel.categoryId"
              variant="outlined"
              density="compact"
              item-value="id"
              item-title="categoryName"
              hide-details
              :items="candidates"
            ></v-autocomplete>

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
              v-model="imageFile"
              clearable
              density="compact"
              variant="compact"
            >

              <template v-slot:title>

                <span class="text-h5"> Kéo thả hoặc tải lên hình ảnh </span>

              </template>

            </v-file-upload>

            <v-text-field
              class="mt-3"
              variant="outlined"
              v-model="formModel.imageUrl"
              density="compact"
              hide-details="true"
              :readonly="imageUrlToggleEdit"
              append-icon="mdi-pencil"
              @click:append="imageUrlToggleEdit = !imageUrlToggleEdit"
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

  <!-- confirmation dialog -->

  <v-dialog
    v-model="confirmationDialog"
    max-width="500"
  >

    <v-card title="Xác nhận">

      <v-card-text>Bạn có chắc chắn muốn xóa sản phẩm?</v-card-text>

      <v-card-actions>

        <v-btn
          variant="elevated"
          color="green-darken-1"
          :loading="isDelLoading"
          @click="remove()"
        >
           Đồng ý
        </v-btn>

        <v-btn
          variant="elevated"
          color="red-lighten-1"
          @click="confirmationDialog = !confirmationDialog"
        >
           Hủy
        </v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>

  <SnackBarOnFailure
    :show="isError"
    :message="message"
  />

  <SnackBarOnSuccess
    :show="isSuccess"
    :message="message"
  />

</template>

