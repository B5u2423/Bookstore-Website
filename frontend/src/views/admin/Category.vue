<script setup>
import { CategoryService } from '@/api/category-api'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { toRef, shallowRef, ref } from 'vue'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'

const adminAuthStore = useAdminAuthStore()
// table
const headers = ref([
  { title: 'ID', key: 'id', align: 'start', sortable: false },
  { title: 'Tên danh mục', key: 'categoryName', align: 'start', sortable: false },
  { title: 'Danh mục cha', key: 'parentName', align: 'start', sortable: false },
  { title: 'Các danh mục con', key: 'children', align: 'start', sortable: false },
  { title: 'Thao tác', key: 'actions', align: 'end', sortable: false },
])
const itemsPerPage = ref(10)
const loading = ref(false)
const serverItems = ref([])
const totalItems = ref(0)

// snackbars
const isSuccess = ref(false)
const isError = ref(false)
const message = ref('')

function createNewRecord() {
  return {
    categoryName: '',
    parentName: '',
    children: [],
  }
}

// edit-add dialog
const formModel = ref(createNewRecord())
const dialog = shallowRef(false)
const isEditing = toRef(() => !!formModel.value.id)
const candidates = ref([])

// confirmation dialog
const confirmationDialog = shallowRef(false)
const itemId = ref('')
const isDelLoading = ref(false)

async function loadItems({ page, itemsPerPage }) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await CategoryService.fetchAllCategoriesPaginated({
      page: page - 1,
      size: itemsPerPage,
    })
    serverItems.value = payload.content
    totalItems.value = payload.page.totalElements
  } catch (error) {
    console.error('Error loading books from server', error)
  } finally {
    loading.value = false
  }
}

async function add() {
  formModel.value = createNewRecord()
  try {
    candidates.value = await CategoryService.fetchAllCategories()
  } catch (error) {
    console.log('Error fetching all candidates for new record')
  }
  dialog.value = true
}

async function edit(id) {
  const found = serverItems.value.find((c) => c.id === id)

  formModel.value = {
    id: found.id,
    categoryName: found.categoryName,
    parentName: found.parentName == null ? 'N/A' : found.parentName,
    children: found.children,
  }

  try {
    candidates.value = await CategoryService.fetchChildCandidates(id)
  } catch (error) {
    console.error('Error fetching children candidates')
  }

  dialog.value = true
}

function confirm(id) {
  confirmationDialog.value = true
  itemId.value = id
}

function remove() {
  try {
    const res = CategoryService.deleteCategoryById(itemId.value, adminAuthStore.accessToken)
    isSuccess.value = true
    message.value = 'Xóa danh mục thành công!'
  } catch (error) {
    isError.value = true
    message.value = 'Lỗi xảy ra khi xóa danh mục'
    console.error('Error deleting item')
  } finally {
    isDelLoading.value = false
    confirmationDialog.value = false
  }
}

async function save() {
  if (isEditing.value) {
    try {
      // API call
      const res = await CategoryService.updateCategory(formModel.value, adminAuthStore.accessToken)
      isSuccess.value = true
      message.value = 'Cập nhật thông tin danh mục thành công!'
    } catch (error) {
      console.error('Error editing category')
      isError.value = true
      message.value = 'Lỗi xảy ra khi cập nhật danh mục'
    }
  } else {
    try {
      // API call
      const res = await CategoryService.addCategory(formModel.value, adminAuthStore.accessToken)
      isSuccess.value = true
      message.value = 'Cập nhật thông tin danh mục thành công!'
    } catch (error) {
      isError.value = true
      message.value = 'Lỗi xảy ra khi thêm danh mục'
      console.error('Error adding new category')
    }
  }

  dialog.value = false
}
</script>

<template>

  <v-data-table-server
    v-model:items-per-page="itemsPerPage"
    :headers="headers"
    :items="serverItems"
    :items-length="totalItems"
    :loading="loading"
    item-value="title"
    items-per-page-text="Số danh mục hiển thị"
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
           Thông tin danh mục
        </v-toolbar-title>

        <v-btn
          class="me-2"
          prepend-icon="mdi-plus"
          rounded="lg"
          text="Thêm danh mục"
          variant="outlined"
          @click="add"
        ></v-btn>

      </v-toolbar>

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

    <!-- parent category -->

    <template v-slot:item.parentName="{ item }">

      <template v-if="item.parentName == null">

        <v-chip
          color="red-lighten-1"
          variant="outlined"
        >
           Không có
        </v-chip>

      </template>

      <template v-else>

        <v-chip>{{ item.parentName }}</v-chip>

      </template>

    </template>

    <!-- children categories -->

    <template v-slot:item.children="{ item }">

      <template v-if="!item.children.length">

        <v-chip
          color="red-lighten-1"
          variant="outlined"
        >
           Không có
        </v-chip>

      </template>

      <template v-else>

        <v-chip
          v-for="child in item.children"
          class="ma-1"
          color="green-darken-1"
        >
           {{ child.categoryName }}
        </v-chip>

      </template>

    </template>

  </v-data-table-server>

  <v-dialog
    v-model="dialog"
    max-width="800"
  >

    <v-card
      :title="`${isEditing ? 'Thay đổi thông tin' : 'Tạo bản ghi mới'}`"
      :subtitle="`${isEditing ? 'Cập nhật' : 'Thêm'} danh mục`"
    >

      <v-card-text>

        <v-row>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Tên danh mục</div>

            <v-text-field
              variant="outlined"
              v-model="formModel.categoryName"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Danh mục cha</div>

            <v-text-field
              variant="outlined"
              v-model="formModel.parentName"
              disabled="true"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col cols="12">

            <div class="text-subtitle-1 text-high-emphasis">Các danh mục con</div>

            <v-autocomplete
              v-model="formModel.children"
              variant="outlined"
              density="compact"
              item-value="id"
              item-title="categoryName"
              hide-details
              :items="candidates"
              multiple
              chips
              closable-chips
            ></v-autocomplete>

          </v-col>

        </v-row>

      </v-card-text>

      <v-card-actions>

        <v-btn
          color="green-darken-1"
          variant="elevated"
          @click="save"
        >
           Lưu
        </v-btn>

        <v-btn
          color="red-lighten-1"
          variant="elevated"
          @click="dialog = !dialog"
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

      <v-card-text>Bạn có chắc chắn muốn xóa danh mục?</v-card-text>

      <v-card-actions>

        <v-btn
          variant="elevated"
          color="green-darken-1"
          :loading="isDelLoading"
          @click="remove"
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

