import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { loginUser, loginAdmin, registerUser, logoutUser, getCurrentUser } from '@/api/auth-api';

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null);
  const accessToken = ref(localStorage.getItem('accessToken'));
  const refreshToken = ref(localStorage.getItem('refreshToken'));
  const isLoading = ref(false);
  const error = ref(null);
  const isAuthenticated = computed(() => !!accessToken.value);
  const isAdmin = computed(() => user.value?.userType === 'ADMIN');
  const isCustomer = computed(() => user.value?.userType === 'CUSTOMER');
  const isStaff = computed(() => user.value?.userType === 'STAFF');

  async function login(credentials) {
    isLoading.value = true;
    error.value = null;
    
    try {
      const response = await loginUser(credentials);
      const { user: userData, token, refresh } = response.data;
      
      // Check if user is ADMIN and prevent login through regular endpoint
      if (userData.userType === 'ADMIN') {
        error.value = 'Tài khoản ADMIN không thể đăng nhập qua trang này. Vui lòng sử dụng trang đăng nhập dành cho quản trị viên.';
        return { success: false, error: error.value };
      }
      
      user.value = userData;
      accessToken.value = token;
      refreshToken.value = refresh;
      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('accessToken', token);
      localStorage.setItem('refreshToken', refresh);
      
      return { success: true, data: response.data };
    } catch (err) {
      error.value = err.response?.data || 'Đăng nhập thất bại';
      return { success: false, error: error.value };
    } finally {
      isLoading.value = false;
    }
  }

  async function adminLogin(credentials) {
    isLoading.value = true;
    error.value = null;
    
    try {
      const response = await loginAdmin(credentials);
      const { user: userData, token, refresh } = response.data;
      
      user.value = userData;
      accessToken.value = token;
      refreshToken.value = refresh;
      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('accessToken', token);
      localStorage.setItem('refreshToken', refresh);
      
      return { success: true, data: response.data };
    } catch (err) {
      error.value = err.response?.data || 'Đăng nhập quản trị viên thất bại';
      return { success: false, error: error.value };
    } finally {
      isLoading.value = false;
    }
  }

  async function register(registrationData) {
    isLoading.value = true;
    error.value = null;
    
    try {
      const response = await registerUser(registrationData);
      const { createdUser } = response.data;
      
      return { success: true, data: createdUser };
    } catch (err) {
      error.value = err.response?.data || 'Đăng ký thất bại';
      return { success: false, error: error.value };
    } finally {
      isLoading.value = false;
    }
  }

  async function logout() {
    isLoading.value = true;
    
    try {
      await logoutUser();
    } catch (err) {
      console.warn('Logout failed on server:', err);
    } finally {
      user.value = null;
      accessToken.value = null;
      refreshToken.value = null;
      localStorage.removeItem('user');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      
      isLoading.value = false;
    }
  }

  async function fetchCurrentUser() {
    if (!accessToken.value) return;
    
    try {
      const response = await getCurrentUser();
      user.value = response.data;
      localStorage.setItem('user', JSON.stringify(response.data));
    } catch (err) {
      console.warn('Failed to fetch current user:', err);
      if (err.response?.status === 401) {
        await logout();
      }
    }
  }

  function initializeAuth() {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      try {
        user.value = JSON.parse(storedUser);
      } catch (err) {
        console.warn('Failed to parse stored user data:', err);
        localStorage.removeItem('user');
      }
    }
  }

  function clearError() {
    error.value = null;
  }

  return {
    user,
    accessToken,
    refreshToken,
    isLoading,
    error,
    isAuthenticated,
    isAdmin,
    isCustomer,
    isStaff,
    login,
    adminLogin,
    register,
    logout,
    fetchCurrentUser,
    initializeAuth,
    clearError
  };
});