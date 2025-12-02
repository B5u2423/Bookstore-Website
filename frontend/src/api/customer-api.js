import api from './auth-api'

export function getCustomerAccount(token) {
  return api.get('/api/v1/customers/account', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

export function updateCustomerProfile(profileData) {
  return api.put('/api/v1/customers/profile', profileData)
}
