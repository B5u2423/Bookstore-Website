export function formatPriceVNLocale(price = 0) {
  return new Intl.NumberFormat('vi-VN').format(price)
}
