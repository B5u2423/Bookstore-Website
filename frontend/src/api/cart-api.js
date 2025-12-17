import api, { provinceApi } from './api-config'

export const CartService = {
  async addToCart(token, body) {
    try {
      const res = await api.post('/api/v1/carts/add', body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      return res.data
    } catch (error) {
      console.error('Error adding item to cart')
    }
  },

  async getUsersActiveCart(token) {
    try {
      const res = await api.get('/api/v1/carts', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      return res.data
    } catch (error) {
      console.error('Error get user active cart')
    }
  },
  async removeAllItemsFromCart(token) {
    try {
      const res = await api.delete('/api/v1/carts/remove-all', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      return res.data
    } catch (error) {
      console.error('Error remove item from cart')
    }
  },
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
  },
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
  },
}

export const AddressInfoService = {
  async getCities() {
    try {
      const res = await provinceApi.get('/api/v2/')
      return res.data
    } catch (error) {
      console.error('Error fetching cities data', error)
    }
  },
  async getCommunes(province) {
    try {
      const res = await api.get('/api/v1/proxy/commune', {
        params: { province },
      })
      return res.data
    } catch (error) {
      console.error('Error fetching communes data', error)
    }
  },
}
