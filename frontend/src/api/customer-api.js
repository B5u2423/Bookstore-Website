import api from './auth-api';

/**
 * Customer API functions - these require authentication with CUSTOMER role
 */

/**
 * Get current customer's account information
 * @returns {Promise} API response with customer account data
 */
export function getCustomerAccount() {
  return api.get('/api/v1/customers/account');
}

/**
 * Update customer profile
 * @param {Object} profileData - Updated profile data
 * @returns {Promise} API response
 */
export function updateCustomerProfile(profileData) {
  return api.put('/api/v1/customers/profile', profileData);
}

/**
 * Get customer's order history
 * @returns {Promise} API response with order history
 */
export function getCustomerOrders() {
  return api.get('/api/v1/customers/orders');
}

/**
 * Get customer's shopping cart
 * @returns {Promise} API response with cart items
 */
export function getCustomerCart() {
  return api.get('/api/v1/customers/cart');
}

/**
 * Add item to customer's cart
 * @param {Object} cartItem - Item to add to cart
 * @returns {Promise} API response
 */
export function addToCart(cartItem) {
  return api.post('/api/v1/customers/cart', cartItem);
}

/**
 * Remove item from customer's cart
 * @param {string} itemId - ID of item to remove
 * @returns {Promise} API response
 */
export function removeFromCart(itemId) {
  return api.delete(`/api/v1/customers/cart/${itemId}`);
}