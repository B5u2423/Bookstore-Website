import { CustomerService } from '@/api/customer-api'
import { defineStore } from 'pinia'
import { ref } from 'vue'

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
      const res = await CustomerService.getCustomerAccount()

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
