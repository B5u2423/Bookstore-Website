import api from './api-config'

// TODO: Rewrite these api into CartService Object

export function addToCart(token, body) {
  return api.post('/api/v1/carts/add', body, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

export function getUsersActiveCart(token) {
  return api.get('/api/v1/carts', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

export function removeAllItemsFromCart(token) {
  return api.delete('/api/v1/carts/remove-all', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

export const PaymentService = {
  async createPaymentPage(body, token) {
    try {
      const res = await api.post('/api/v1/payment/create-payment', body, {
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error changing to payment page', error)
    }
  }
}

export const OrderService = {
  async createOrder(body, token) {
    try {
      const res = await api.post('/api/v1/orders/create-order', body, {
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error making order', error)
    }
  }
}