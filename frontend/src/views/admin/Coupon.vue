<script setup>
import { CouponService } from '@/api/coupon-api'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { ref, onMounted, toRef, shallowRef } from 'vue'
import { VDateInput } from 'vuetify/labs/VDateInput'

const adminAuthStore = useAdminAuthStore()

// TODO: add snackbar
function createNewRecord() {
  return {
    id: null,
    code: '',
    discountType: '',
    discountValue: '',
    minOrderAmount: 0,
    maxUses: 0,
    usedCount: 0,
    validFrom: null,
    validUntil: null,
    isActive: false,
  }
}

// table
const headers = ref([
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Mã giảm giá', key: 'code', align: 'start' },
  { title: 'Phương thức giảm giá', key: 'discountType', align: 'start' },
  { title: 'Giá trị mã', key: 'discountValue', align: 'start' },
  { title: 'Đơn hàng tối thiểu', key: 'minOrderAmount', align: 'start' },
  { title: 'Số lượng tối đa', key: 'maxUses', align: 'start' },
  { title: 'Đã sử dụng (lần)', key: 'usedCount', align: 'start' },
  { title: 'Ngày bắt đầu', key: 'validFrom', align: 'start', sortable: false },
  { title: 'Ngày kết thúc', key: 'validUntil', align: 'start', sortable: false },
  { title: 'Trạng thái', key: 'isActive', align: 'start', sortable: false },
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
const types = ref([
  {
    id: 'FIXED',
    name: 'Theo số tiền',
  },
  {
    id: 'PERCENT',
    name: 'Theo phần trăm',
  },
])

// confirmation dialog
const confirmationDialog = shallowRef(false)
const itemId = ref('')
const isDelLoading = ref(false)

async function loadItems({ page = 1, itemsPerPage: size = itemsPerPage.value } = {}) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await CouponService.getAllCouponsPaginated(adminAuthStore.accessToken, {
      page: page - 1,
      size,
    })
    serverItems.value = payload.content
    totalItems.value = payload.page.totalElements
  } catch (error) {
    console.error('Error loading coupons from server', error)
  } finally {
    loading.value = false
  }
}

async function edit(id) {
  const found = serverItems.value.find((coupon) => coupon.id === id)

  formModel.value = {
    id: found.id,
    code: found.code,
    discountType: found.discountType,
    discountValue: found.discountValue,
    minOrderAmount: found.minOrderAmount,
    maxUses: found.maxUses,
    usedCount: found.usedCount,
    validFrom: found.validFrom,
    validUntil: found.validUntil,
    isActive: found.isActive,
  }

  dialog.value = true
}

function confirm(id) {
  confirmationDialog.value = true
  itemId.value = id
}

function add() {
  formModel.value = createNewRecord()
  dialog.value = true
}

async function save() {
  if (isEditing.value) {
    try {
      // API call
      const res = await CouponService.updateCoupon(
        formModel.value.id,
        formModel.value,
        adminAuthStore.accessToken,
      )
      // edit immediate view
      const index = serverItems.value.findIndex((coupon) => coupon.id === formModel.value.id)
      serverItems.value[index] = formModel.value
    } catch (error) {
      console.error('Error editing coupon')
    }
  } else {
    try {
      // API call
      const res = await CouponService.addNewCoupon(formModel.value, adminAuthStore.accessToken)
    } catch (error) {
      console.error('Error adding new coupon')
    }
  }
}

async function remove() {
  isDelLoading.value = true
  try {
    const res = await CouponService.deleteCoupon(itemId.value, adminAuthStore.accessToken)
    // update on frontend, just for immediate view
    const index = serverItems.value.findIndex((coupon) => coupon.id === itemId.value)
    serverItems.value.splice(index, 1)
    totalItems.value--
  } catch (error) {
    console.error(`Error deleting coupon with id ${id}`, error)
  } finally {
    isDelLoading.value = false
    confirmationDialog.value = false
  }
}

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
            icon="mdi-ticket-percent"
            size="x-small"
            start
          ></v-icon>
           Thông tin mã giảm giá
        </v-toolbar-title>

        <v-btn
          class="me-2"
          prepend-icon="mdi-plus"
          rounded="lg"
          text="Thêm mã"
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

      <div class="d-flex ga-2 justify-center">

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

  </v-data-table-server>

  <v-dialog
    v-model="dialog"
    max-width="800"
  >

    <v-card
      :title="`${isEditing ? 'Thay đổi thông tin' : 'Tạo bản ghi mới'}`"
      :subtitle="`${isEditing ? 'Cập nhật' : 'Thêm'} mã giảm giá`"
    >

      <template v-slot:text>

        <v-row class="px-3">

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Mã giảm giá
              <span class="text-red">(*)</span>

            </div>

            <v-text-field
              variant="outlined"
              v-model="formModel.code"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Mức giảm
              <span class="text-red">(*)</span>

            </div>

            <v-text-field
              variant="outlined"
              v-model="formModel.discountValue"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Phương thức
              <span class="text-red">(*)</span>

            </div>

            <v-autocomplete
              v-model="formModel.discountType"
              variant="outlined"
              density="compact"
              item-value="id"
              item-title="name"
              hide-details
              :items="types"
            ></v-autocomplete>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Đơn hàng tối thiểu
              <span class="text-red">(*)</span>

            </div>

            <v-text-field
              variant="outlined"
              v-model="formModel.minOrderAmount"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Số lượng tối đa
              <span class="text-red">(*)</span>

            </div>

            <v-text-field
              variant="outlined"
              v-model="formModel.maxUses"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Ngày bắt đầu
              <span class="text-red">(*)</span>

            </div>

            <v-date-input
              clearable
              variant="outlined"
              v-model="formModel.validFrom"
            ></v-date-input>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Ngày kết thúc
              <span class="text-red">(*)</span>

            </div>

            <v-date-input
              clearable
              variant="outlined"
              v-model="formModel.validUntil"
            ></v-date-input>

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

      <v-card-text>Bạn có chắc chắn muốn xóa mã?</v-card-text>

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

