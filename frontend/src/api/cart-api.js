import api from './api-config'

export function addToCart(token, body) {
  return api.post('/api/v1/carts/add', body, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

export function getUsersActiveCart(token) {
    return api.get('/api/v1/carts', { headers: {
      Authorization: `Bearer ${token}`,
    }
  })
}