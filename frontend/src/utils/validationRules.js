export const nameRules = [
  (value) => {
    if (value && value.trim()) return true
    return 'Tên là bắt buộc.'
  },
  value => {
    if (value?.length <= 50) return true
    return 'Tên phải ít hơn 50 ký tự.'
  },
  value => {
    if (/^[a-zA-ZÀ-ỹ\s]+$/.test(value)) return true
    return 'Tên chỉ được chứa chữ cái và khoảng trắng.'
  }
]

export const emailRules = [
  value => {
    if (value) return true
    return 'Email là bắt buộc.'
  },
  value => {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (emailPattern.test(value)) return true
    return 'Email không hợp lệ.'
  },
]

export const phoneRules = [
  value => {
    if (value) return true
    return 'Số điện thoại là bắt buộc'
  },
  value => {
    if (/^[0-9]{10,11}$/.test(value)) return true
    return 'Số điện thoại phải có 10-11 chữ số'
  }
]

export const passwordRules = [
  value => {
    if (value) return true
    return 'Mật khẩu là bắt buộc'
  },
  value => {
    if (value?.length >= 6) return true
    return 'Mật khẩu phải có ít nhất 6 ký tự'
  },
  value => {
    if (value?.length <= 50) return true
    return 'Mật khẩu không được quá 50 ký tự'
  }
]
