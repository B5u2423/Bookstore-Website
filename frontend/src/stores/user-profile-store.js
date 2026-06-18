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
      try {
        const payload = await CustomerService.getCustomerAccount()

        userInfo.value.name = payload.name
        userInfo.value.email = payload.email
        userInfo.value.phone = payload.phoneNumber
        userInfo.value.addressList = payload.addressList
      } catch (e) {
        console.error(`UserProfile Store error fetching user info ${e.message}`)
      }
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
