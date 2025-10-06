export const nameRules = [
  (value) => {
    if (value) return true
    return 'Name is required.'
  },
  value => {
    if (value?.length <= 255) return true
    return 'Name must be less than 255 characters.'
  },
]
export const emailRules = [
  value => {
    if (value) return true
    return 'E-mail is required.'
  },
  value => {
    if (/.+@.+\..+/.test(value)) return true

    return 'E-mail must be valid.'
  },
]

export const phoneRules = [
  value => {
    if (value) return true
    return 'Phone number is required'
  },
  value => {
    if (/\d{7,15}/.test(value)) return true
    return 'Invalid phone number'
  }
]

export const passwordRules = [
  value => {
    if (value) return true
    return 'Password is required'
  },
  value => {
    if (value?.length >= 8) return true
    return 'Password should be at least 8 characters'
  }
]
