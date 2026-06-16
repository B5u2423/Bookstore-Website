<script setup>
import { AddressInfoService } from '@/api/cart-api.js'
import { CustomerService, UserService } from '@/api/customer-api'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'
import { useAuthStore } from '@/stores/auth-store'
import { useCartStore } from '@/stores/cart-store'
import { useUserProfileStore } from '@/stores/user-profile-store'
import { onMounted, ref, watch } from 'vue'

const userProfileStore = useUserProfileStore()
const cartStore = useCartStore()

const isFieldsEnabled = ref(false)
const isUpdated = ref(false)
const cities = ref([])
const communes = ref([])
const address = ref({ cityId: '', communeId: '', street: '' })
const dialog = ref(false)
const deleteConfirmId = ref(null)

const isError = ref(false)
const isSuccess = ref(false)
const message = ref('')

const currentUserProfileSnapshot = ref({
  name: userProfileStore.userInfo.name,
  email: userProfileStore.userInfo.email,
  phoneNumber: userProfileStore.userInfo.phone,
})

const headers = ref([
  { title: 'Địa chỉ', key: 'street', align: 'start' },
  { title: 'Xã / Phường', key: 'commune', align: 'start' },
  { title: 'Tỉnh / Thành', key: 'city', align: 'start' },
  { title: '', key: 'actions', align: 'center', sortable: false, width: '60px' },
])

watch(currentUserProfileSnapshot, () => {
  isUpdated.value = true
}, { deep: true })

function enableFieldsForUpdate() {
  isFieldsEnabled.value = true
}

function discardChanges() {
  currentUserProfileSnapshot.value.name = userProfileStore.userInfo.name
  currentUserProfileSnapshot.value.email = userProfileStore.userInfo.email
  currentUserProfileSnapshot.value.phoneNumber = userProfileStore.userInfo.phone
  isUpdated.value = false
  isFieldsEnabled.value = false
}

function notify(success, msg) {
  if (success) {
    isSuccess.value = true
    message.value = msg
  } else {
    isError.value = true
    message.value = msg
  }
  setTimeout(() => {
    isSuccess.value = false
    isError.value = false
  }, 2500)
}

async function updateChanges() {
  try {
    await UserService.updateUserProfile(currentUserProfileSnapshot.value)
    // reload
    await userProfileStore.getUserInfo()
    notify(true, 'Cập nhật thông tin thành công!')
  } catch (error) {
    console.error('Error updating user info', error)
    notify(false, 'Lỗi khi cập nhật thông tin người dùng')
  } finally {
    isUpdated.value = false
    isFieldsEnabled.value = false
  }
}

async function fetchCities() {
  try {
    cities.value = await AddressInfoService.getCities()
  } catch (e) {
    console.error('Error fetching cities', e)
  }
}

async function fetchCommunes() {
  try {
    address.value.communeId = ''
    communes.value = await AddressInfoService.getCommunes(address.value.cityId)
  } catch (e) {
    console.error('Error fetching communes', e)
  }
}

async function save() {
  try {
    const addrObj = {
      cityId: address.value.cityId,
      city: cities.value.find(o => o.code === address.value.cityId)?.name,
      communeId: address.value.communeId,
      commune: communes.value.find(o => o.code === address.value.communeId)?.name,
      street: address.value.street,
    }
    await CustomerService.setAddress(addrObj)
    userProfileStore.userInfo.addressList.push(addrObj)
    notify(true, 'Thêm địa chỉ thành công!')
  } catch (e) {
    notify(false, 'Lỗi khi thêm địa chỉ')
    console.error('Error adding address', e)
  } finally {
    dialog.value = false
    address.value = { cityId: '', communeId: '', street: '' }
  }
}

async function deleteAddress(id) {
  try {
    await CustomerService.deleteAddress(id)
    userProfileStore.userInfo.addressList = userProfileStore.userInfo.addressList.filter(a =>
      a.id !== id
    )
    notify(true, 'Xóa địa chỉ thành công')
  } catch (e) {
    console.error('Error deleting address', e)
    notify(false, 'Lỗi khi xóa địa chỉ')
  } finally {
    deleteConfirmId.value = null
  }
}

onMounted(() => {
  cartStore.syncCartWithBackEnd()
  fetchCities()
})
</script>

<template>
  <div class="user-info-page">
    <section class="info-section">
      <div class="section-header">
        <div class="section-header-left">
          <div class="section-icon-wrap" aria-hidden="true">
            <v-icon icon="mdi-account-circle-outline" size="20" />
          </div>
          <div>
            <h2 class="section-title">Thông tin cá nhân</h2>
            <p class="section-subtitle">Quản lý thông tin hồ sơ của bạn</p>
          </div>
        </div>
        <button
          v-if="!isFieldsEnabled"
          class="edit-btn"
          type="button"
          @click="enableFieldsForUpdate"
        >
          <v-icon icon="mdi-pencil-outline" size="15" class="mr-1" />
          Chỉnh sửa
        </button>
      </div>

      <div class="section-body">
        <div class="field-grid">
          <div class="field-group field-group--full">
            <label class="field-label" for="ui-name">
              Họ và tên <span class="required" aria-hidden="true">*</span>
            </label>
            <v-text-field
              id="ui-name"
              v-model="currentUserProfileSnapshot.name"
              variant="outlined"
              density="compact"
              placeholder="Nguyễn Văn A"
              :disabled="!isFieldsEnabled"
              hide-details
              class="info-field"
              prepend-inner-icon="mdi-account-outline"
            />
          </div>

          <div class="field-group">
            <label class="field-label" for="ui-email">
              Email <span class="required" aria-hidden="true">*</span>
            </label>
            <v-text-field
              id="ui-email"
              v-model="currentUserProfileSnapshot.email"
              variant="outlined"
              density="compact"
              placeholder="you@example.com"
              :disabled="!isFieldsEnabled"
              hide-details
              class="info-field"
              prepend-inner-icon="mdi-email-outline"
            />
          </div>

          <div class="field-group">
            <label class="field-label" for="ui-phone">Số điện thoại</label>
            <v-text-field
              id="ui-phone"
              v-model="currentUserProfileSnapshot.phoneNumber"
              variant="outlined"
              density="compact"
              placeholder="0900 000 000"
              :disabled="!isFieldsEnabled"
              hide-details
              class="info-field"
              prepend-inner-icon="mdi-phone-outline"
            />
          </div>
        </div>

        <!-- Action bar (shown only in edit mode) -->
        <Transition name="slide-down">
          <div v-if="isFieldsEnabled" class="action-bar">
            <button
              class="btn-save"
              type="button"
              :disabled="!isUpdated"
              @click="updateChanges"
            >
              <v-icon icon="mdi-check" size="15" class="mr-1" />
              Lưu thay đổi
            </button>
            <button class="btn-cancel" type="button" @click="discardChanges">
              Hủy
            </button>
          </div>
        </Transition>
      </div>
    </section>

    <section class="info-section">
      <div class="section-header">
        <div class="section-header-left">
          <div class="section-icon-wrap" aria-hidden="true">
            <v-icon icon="mdi-map-marker-outline" size="20" />
          </div>
          <div>
            <h2 class="section-title">Địa chỉ giao hàng</h2>
            <p class="section-subtitle">Quản lý danh sách địa chỉ của bạn</p>
          </div>
        </div>
        <button class="edit-btn" type="button" @click="dialog = true">
          <v-icon icon="mdi-plus" size="15" class="mr-1" />
          Thêm địa chỉ
        </button>
      </div>

      <div class="section-body">
        <!-- Empty state -->
        <div v-if="!userProfileStore.userInfo.addressList?.length" class="addr-empty">
          <v-icon icon="mdi-map-marker-off-outline" size="36" class="mb-2" />
          <p>
            Bạn chưa có địa chỉ nào. Thêm địa chỉ để đặt hàng nhanh hơn.
          </p>
        </div>

        <!-- Address list -->
        <div v-else class="addr-list">
          <div
            v-for="addr in userProfileStore.userInfo.addressList"
            :key="addr.id"
            class="addr-card"
          >
            <div class="addr-card-icon" aria-hidden="true">
              <v-icon icon="mdi-map-marker-outline" size="18" />
            </div>
            <div class="addr-card-body">
              <p class="addr-street">{{ addr.street }}</p>
              <p class="addr-region">{{ addr.commune }}, {{ addr.city }}</p>
            </div>
            <div class="addr-card-actions">
              <!-- Delete confirm inline -->
              <template v-if="deleteConfirmId === addr.id">
                <span class="delete-confirm-text">Xóa?</span>
                <button
                  class="addr-action-btn addr-action-btn--confirm"
                  @click="deleteAddress(addr.id)"
                >
                  Có
                </button>
                <button class="addr-action-btn" @click="deleteConfirmId = null">Không</button>
              </template>
              <button
                v-else
                class="addr-action-btn addr-action-btn--delete"
                :aria-label="`Xóa địa chỉ ${addr.street}`"
                @click="deleteConfirmId = addr.id"
              >
                <v-icon icon="mdi-trash-can-outline" size="16" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <v-dialog v-model="dialog" max-width="480" :scrim-opacity="0.4">
    <div class="addr-dialog">
      <div class="dialog-header">
        <div class="section-icon-wrap" aria-hidden="true">
          <v-icon icon="mdi-map-marker-plus-outline" size="20" />
        </div>
        <div>
          <h3 class="dialog-title">Thêm địa chỉ mới</h3>
          <p class="dialog-subtitle">Địa chỉ giao hàng của bạn</p>
        </div>
        <button class="dialog-close" @click="dialog = false" aria-label="Đóng">
          <v-icon icon="mdi-close" size="18" />
        </button>
      </div>

      <div class="dialog-body">
        <div class="field-group">
          <label class="field-label" for="addr-city">Tỉnh / Thành phố</label>
          <v-autocomplete
            id="addr-city"
            v-model="address.cityId"
            :items="cities"
            item-title="name"
            item-value="code"
            variant="outlined"
            density="compact"
            hide-details
            placeholder="Chọn tỉnh thành"
            prepend-inner-icon="mdi-city-variant-outline"
            class="info-field"
            @update:modelValue="fetchCommunes"
          />
        </div>

        <div class="field-group">
          <label class="field-label" for="addr-commune">Xã / Phường</label>
          <v-autocomplete
            id="addr-commune"
            v-model="address.communeId"
            :items="communes"
            item-title="name"
            item-value="code"
            variant="outlined"
            density="compact"
            hide-details
            placeholder="Chọn xã phường"
            prepend-inner-icon="mdi-home-map-marker"
            :disabled="!address.cityId"
            class="info-field"
          />
        </div>

        <div class="field-group">
          <label class="field-label" for="addr-street">Địa chỉ chi tiết</label>
          <v-text-field
            id="addr-street"
            v-model="address.street"
            variant="outlined"
            density="compact"
            hide-details
            placeholder="Số nhà, đường, thôn, ngõ…"
            prepend-inner-icon="mdi-road-variant"
            class="info-field"
          />
        </div>
      </div>

      <div class="dialog-footer">
        <button class="btn-cancel" type="button" @click="dialog = false">Hủy</button>
        <button
          class="btn-save"
          type="button"
          :disabled="!address.cityId || !address.communeId || !address.street"
          @click="save"
        >
          <v-icon icon="mdi-check" size="15" class="mr-1" />
          Lưu địa chỉ
        </button>
      </div>
    </div>
  </v-dialog>

  <snack-bar-on-failure :show="isError" :message="message" />
  <snack-bar-on-success :show="isSuccess" :message="message" />
</template>

<style scoped>
.user-info-page {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  display: flex;
  flex-direction: column;
  color: var(--ink);
  font-family: system-ui, -apple-system, sans-serif;
  font-size: 0.9rem;
}

.info-section {
  border-bottom: 1px solid var(--border);
}
.info-section:last-child { border-bottom: none; }

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px 16px;
  gap: 12px;
}
.section-header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.section-icon-wrap {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  background: var(--accent-soft);
  color: var(--accent);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.section-title {
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--ink);
}
.section-subtitle {
  font-size: 0.78rem;
  color: var(--muted);
  margin: 2px 0 0;
}

.section-body {
  padding: 0 24px 24px;
}

.edit-btn {
  display: inline-flex;
  align-items: center;
  height: 34px;
  padding: 0 14px;
  background: var(--paper);
  border: 1px solid var(--border);
  border-radius: 6px;
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--ink);
  cursor: pointer;
  white-space: nowrap;
  transition: border-color 0.15s, color 0.15s, background 0.15s;
  flex-shrink: 0;
}
.edit-btn:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-soft);
}
.edit-btn:focus-visible { outline: 2px solid var(--accent); outline-offset: 2px; }

.field-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px 20px;
}
.field-group { display: flex; flex-direction: column; gap: 6px; }
.field-group--full { grid-column: 1 / -1; }
.field-label {
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--ink);
  letter-spacing: 0.01em;
}
.required { color: var(--accent); margin-left: 2px; }

/* Vuetify field overrides */
.info-field :deep(.v-field) { border-radius: 8px !important; font-size: 0.875rem; }
.info-field :deep(.v-field--focused .v-field__outline) { color: var(--accent) !important; }
.info-field :deep(.v-field__prepend-inner .v-icon) { color: var(--muted) !important; opacity: 1; }
.info-field :deep(.v-field--disabled) { opacity: 0.65; }

.action-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid var(--border);
}

.btn-save,
.btn-cancel {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 38px;
  padding: 0 18px;
  border-radius: 7px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s, border-color 0.15s, opacity 0.15s;
  white-space: nowrap;
}
.btn-save {
  background: var(--accent);
  color: #fff;
  border: none;
}
.btn-save:hover:not(:disabled) { background: #8e1f24; }
.btn-save:disabled { opacity: 0.45; cursor: not-allowed; }
.btn-cancel {
  background: #fff;
  color: var(--muted);
  border: 1px solid var(--border);
}
.btn-cancel:hover { border-color: var(--muted); color: var(--ink); }
.btn-save:focus-visible,
.btn-cancel:focus-visible { outline: 2px solid var(--accent); outline-offset: 2px; }

.addr-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 32px 16px;
  color: var(--muted);
  font-size: 0.875rem;
  background: var(--paper);
  border-radius: 10px;
  border: 1px dashed var(--border);
}

.addr-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.addr-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 16px;
  background: var(--paper);
  border: 1px solid var(--border);
  border-radius: 10px;
  transition: border-color 0.15s;
}
.addr-card:hover { border-color: #c8bfb5; }
.addr-card-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  background: var(--accent-soft);
  color: var(--accent);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.addr-card-body { flex: 1; min-width: 0; }
.addr-street {
  font-size: 0.875rem;
  font-weight: 600;
  margin: 0 0 2px;
  color: var(--ink);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.addr-region {
  font-size: 0.8rem;
  color: var(--muted);
  margin: 0;
}
.addr-card-actions {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-shrink: 0;
}
.addr-action-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 30px;
  padding: 0 10px;
  border-radius: 6px;
  font-size: 0.78rem;
  font-weight: 600;
  border: 1px solid var(--border);
  background: #fff;
  color: var(--muted);
  cursor: pointer;
  transition: all 0.15s;
}
.addr-action-btn:hover { border-color: var(--accent); color: var(--accent); }
.addr-action-btn--delete { padding: 0 8px; }
.addr-action-btn--delete:hover { background: var(--accent-soft); }
.addr-action-btn--confirm {
  border-color: var(--accent);
  background: var(--accent);
  color: #fff;
}
.addr-action-btn--confirm:hover { background: #8e1f24; }
.delete-confirm-text { font-size: 0.78rem; color: var(--accent); font-weight: 600; }
.addr-action-btn:focus-visible { outline: 2px solid var(--accent); outline-offset: 2px; }

.addr-dialog {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid var(--border);
  box-shadow: 0 8px 32px -8px rgba(43,36,32,0.18);
}
.dialog-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 20px 16px;
  border-bottom: 1px solid var(--border);
  background: var(--paper);
}
.dialog-title {
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--ink);
}
.dialog-subtitle { font-size: 0.78rem; color: var(--muted); margin: 2px 0 0; }
.dialog-close {
  margin-left: auto;
  width: 30px;
  height: 30px;
  border-radius: 6px;
  border: none;
  background: transparent;
  color: var(--muted);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: background 0.15s;
}
.dialog-close:hover { background: var(--border); }
.dialog-body {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 16px 20px;
  border-top: 1px solid var(--border);
  background: var(--paper);
}

.slide-down-enter-active,
.slide-down-leave-active { transition: max-height 0.25s ease, opacity 0.2s ease; overflow: hidden; max-height: 80px; }
.slide-down-enter-from,
.slide-down-leave-to { max-height: 0; opacity: 0; }

@media (max-width: 600px) {
  .section-header { padding: 16px 16px 12px; flex-wrap: wrap; }
  .section-body { padding: 0 16px 20px; }
  .field-grid { grid-template-columns: 1fr; }
  .field-group--full { grid-column: auto; }
}
</style>
