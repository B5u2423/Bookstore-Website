import api, { adminApi, customerApi } from './api-config'

export const CartService = {
  async addToCart(body) {
    try {
      const res = await customerApi.post('/carts/add', body)
      return res.data
    } catch (error) {
      console.error('Error adding item to cart')
    }
  },

  async getUsersActiveCart() {
    try {
      const res = await customerApi.get('/carts')
      return res.data
    } catch (error) {
      console.error('Error get user active cart')
    }
  },
  async syncCartWithBackEnd(body) {
    try {
      const res = await customerApi.post('/carts/sync', body)
      return res
    } catch (error) {
      console.error('Error remove item from cart')
    }
  },
}

export const PaymentService = {
  async createPaymentPage(body) {
    try {
      const res = await customerApi.post('/payment/create-payment', body)
      return res.data
    } catch (error) {
      console.error('Error changing to payment page', error)
    }
  },
}

export const OrderService = {
  async updateStatusById(body) {
    try {
      const res = await adminApi.post('/orders/update-status-id', body)
      return res.data
    } catch (error) {
      console.error('Error updating order by Id', error)
    }
  },

  async updateStatus(body) {
    try {
      const res = await customerApi.post('/orders/update-status', body)
      return res.data
    } catch (error) {
      console.error('Error updating order status', error)
    }
  },

  async createOrder(body) {
    try {
      const res = await customerApi.post('/orders/create-order', body)
      return res.data
    } catch (error) {
      console.error('Error making order', error)
    }
  },

  async getAllOrdersPaginated(params = {}) {
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
