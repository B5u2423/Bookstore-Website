import { CustomerService } from '@/api/customer-api'
import { defineStore } from 'pinia'
import { computed, ref, watch } from 'vue'
import { useAuthStore } from './auth-store'

export const useUserProfileStore = defineStore(
  'userprofile',
  () => {
    const userInfo = ref({
      name: '',
      phone: '',
      email: '',
      addressList: [],
    })

    async function getUserInfo() {
      const authStore = useAuthStore()
      const res = await CustomerService.getCustomerAccount(authStore.accessToken)

      userInfo.value.name = res.name
      userInfo.value.email = res.email
      userInfo.value.phone = res.phoneNumber
      userInfo.value.addressList = res.addressList
    }

    function updateUserInfo({ name, phone, email }) {
      userInfo.value.name = name
      userInfo.value.phone = phone
      userInfo.value.email = email
    }

    return {
      userInfo,
      updateUserInfo,
      getUserInfo,
    }
  },
  {
    persist: {
      storage: localStorage,
      paths: ['userInfo'],
    },
  },
)
