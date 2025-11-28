
import { defineStore } from 'pinia'
import { computed, ref, watch } from 'vue'

export const useUserProfileStore = defineStore(
  'userprofile',
  () => {
    const userInfo = ref({
      firstName: '',
      lastName: '',
      phone: '',
      email: ''
    })

    function updateUserInfo({
      firstName, lastName, phone, email
    }) {
      userInfo.value.firstName = firstName
      userInfo.value.lastName = lastName
      userInfo.value.phone = phone
      userInfo.value.email = email
    }

    return {
      userInfo, updateUserInfo
    }
  }, 
  {
    persist: {
    storage: localStorage,
    paths: ['userInfo']
    }
  }
)