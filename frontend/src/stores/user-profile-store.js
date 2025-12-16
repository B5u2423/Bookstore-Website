import { CustomerService, getCustomerAccount } from '@/api/customer-api'
import { defineStore } from 'pinia'
import { computed, ref, watch } from 'vue'
import { useAuthStore } from './auth-store'

export const useUserProfileStore = defineStore(
  'userprofile',
  () => {
    const userInfo = ref({
      firstName: '',
      lastName: '',
      phone: '',
      email: '',
      addressList: [],
    })

    async function getUserInfo() {
      const authStore = useAuthStore()
      const res = await CustomerService.getCustomerAccount(authStore.accessToken)

      userInfo.value.firstName = res.firstName
      userInfo.value.lastName = res.lastName
      userInfo.value.email = res.email
      userInfo.value.phone = res.phoneNumber
      userInfo.value.addressList = res.addressList
    }

    function updateUserInfo({ firstName, lastName, phone, email }) {
      userInfo.value.firstName = firstName
      userInfo.value.lastName = lastName
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
