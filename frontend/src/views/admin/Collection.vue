<script setup>
import { CollectionService } from '@/api/collection-api'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { ref, computed, onMounted, toRef, shallowRef } from 'vue'

const adminAuthStore = useAdminAuthStore()

function createNewRecord() {
  return {
    id: '',
    collectionName: '',
    collectionSlug: '',
  }
}

// table
const headers = ref([
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Tên bộ sưu tập', key: 'collectionName', align: 'start' },
  { title: 'Thao tác', key: 'actions', align: 'start' },
])
const itemsPerPage = ref(10)
const loading = ref(false)
const serverItems = ref([])
const totalItems = ref(0)

// edit-add dialog
const formModel = ref(createNewRecord())
const dialog = shallowRef(false)
const isEditing = toRef(() => !!formModel.value.id)

// confirmation dialog
// TODO: add snackbar to collection and coupon
const confirmationDialog = shallowRef(false)
const itemId = ref('')
const isDelLoading = ref(false)

async function loadItems({ page, itemsPerPage }) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await CollectionService.getAllCollectionsPaginated(adminAuthStore.accessToken, {
      page: page - 1,
      size: itemsPerPage,
    })
    serverItems.value = payload.content
    totalItems.value = payload.page.totalElements
  } catch (error) {
    console.error('Error loading collections from server', error)
  } finally {
    loading.value = false
  }
}

function add() {
  formModel.value = createNewRecord()
  dialog.value = true
}

async function edit(id) {
  const found = serverItems.value.find((collection) => collection.id === id)

  formModel.value = {
    id: found.id,
    collectionName: found.collectionName,
    collectionSlug: found.collectionSlug,
  }

  dialog.value = true
}

async function confirm(id) {
  confirmationDialog.value = true
  itemId.value = id
}

async function save() {
  if (isEditing.value) {
    try {
      // API call
      const res = await CollectionService.updateCollection(
        formModel.value.id,
        formModel.value,
        adminAuthStore.accessToken,
      )
      // edit immediate view
      const index = serverItems.value.findIndex(
        (collection) => collection.id === formModel.value.id,
      )
      serverItems.value[index] = formModel.value
    } catch (error) {
      console.error('Error editing collection')
    } finally {
      dialog.value = false
    }
  } else {
    try {
      // API call
      const res = await CollectionService.addNewCollection(
        formModel.value,
        adminAuthStore.accessToken,
      )
    } catch (error) {
      console.error('Error adding new collection')
    } finally {
      dialog.value = false
    }
  }
}

async function remove() {
  isDelLoading.value = true
  try {
    const res = await CollectionService.deleteCollection(itemId.value, adminAuthStore.accessToken)
    // update on frontend, just for immediate view
    const index = serverItems.value.findIndex((book) => book.id === itemId.value)
    serverItems.value.splice(index, 1)
    totalItems.value--
  } catch (error) {
    console.error(`Error deleting collection with id ${id}`, error)
  } finally {
    isDelLoading.value = false
    confirmationDialog.value = false
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

const collectionNameCaps = computed({
  get: () => formModel.value.collectionName,
  set: (val) => {
    formModel.value.collectionName = capitalizeVietnamese(val || '')
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
    items-per-page-text="Số bộ sưu tập hiển thị"
    @update:options="loadItems"
  >

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>

          <v-icon
            color="medium-emphasis"
            icon="mdi-ticket-percent"
            size="x-small"
            start
          ></v-icon>
           Thông tin bộ sưu tập
        </v-toolbar-title>

        <v-btn
          class="me-2"
          prepend-icon="mdi-plus"
          rounded="lg"
          text="Thêm bộ sưu tập"
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

    <!-- action buttons -->

    <template v-slot:item.actions="{ item }">

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

    </template>

  </v-data-table-server>

  <v-dialog
    v-model="dialog"
    max-width="500"
  >

    <v-card
      :title="`${isEditing ? 'Thay đổi thông tin' : 'Tạo bản ghi mới'}`"
      :subtitle="`${isEditing ? 'Cập nhật' : 'Thêm'} bộ sưu tập`"
    >

      <template v-slot:text>

        <v-row class="px-3">

          <v-col
            cols="12"
            md="12"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Tên bộ sưu tập
              <span class="text-red">(*)</span>

            </div>

            <v-text-field
              variant="outlined"
              v-model="collectionNameCaps"
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
          @click="
            () => {
              dialog = false
            }
          "
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

      <v-card-text>Bạn có chắc chắn muốn xóa bộ sưu tập?</v-card-text>

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

</template>

