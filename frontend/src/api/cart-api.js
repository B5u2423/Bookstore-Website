import api, { adminApi, customerApi, provinceApi } from './api-config'

export const CartService = {
  async addToCart(token, body) {
    try {
      const res = await customerApi.post('/carts/add', body)
      return res.data
    } catch (error) {
      console.error('Error adding item to cart')
    }
  },

  async getUsersActiveCart(token) {
    try {
      const res = await customerApi.get('/carts')
      return res.data
    } catch (error) {
      console.error('Error get user active cart')
    }
  },
  async removeAllItemsFromCart(token) {
    try {
      const res = await customerApi.delete('/carts/remove-all')
      return res.data
    } catch (error) {
      console.error('Error remove item from cart')
    }
  },
}

export const PaymentService = {
  async createPaymentPage(body, token) {
    try {
      const res = await customerApi.post('/payment/create-payment', body)
      return res.data
    } catch (error) {
      console.error('Error changing to payment page', error)
    }
  },
}

export const OrderService = {
  async createOrder(body, token) {
    try {
      const res = await customerApi.post('/orders/create-order', body)
      return res.data
    } catch (error) {
      console.error('Error making order', error)
    }
  },

  async getAllOrdersPaginated(token, params = {}) {
    try {
      const res = await adminApi.get('/orders', {
        params: params,
      })
      return res.data
    } catch (error) {
      console.error('Error fetch paginated orders', error)
    }
  },

  async getAllOrdersByEmail(params = {}) {
    try {
      const res = await customerApi.get('/orders/user', {
        params: params,
      })
      return res.data
    } catch (error) {
      console.error('Error get all orders by email', error)
    }
  },
}

export const AddressInfoService = {
  async getCities() {
    try {
      const res = await api.get('/proxy/province')
      return res.data
    } catch (error) {
      console.error('Error fetching cities data', error)
    }
  },
  async getCommunes(province) {
    try {
      const res = await api.get('/proxy/commune', {
        params: { province },
      })
      return res.data
    } catch (error) {
      console.error('Error fetching communes data', error)
    }
  },
}
