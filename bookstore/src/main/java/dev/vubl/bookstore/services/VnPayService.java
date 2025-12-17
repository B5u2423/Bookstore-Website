package dev.vubl.bookstore.services;

import dev.vubl.bookstore.configs.VnPayConfig;
import dev.vubl.bookstore.dtos.VnPayRequest;
import dev.vubl.bookstore.dtos.VnPayResponse;
import dev.vubl.bookstore.utils.VnPayUtil;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class VnPayService {
  private final VnPayConfig vnPayConfig;

  public VnPayResponse createVnPayPayment(VnPayRequest payload, String ipaddr) {
    BigDecimal amount = payload.amount().multiply(BigDecimal.valueOf(100));
    // VnPay specific params
    Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
    vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
    // direct bank code so that client doesn't have to choose
    //    if (bankCode != null || !bankCode.isEmpty()) {
    //      vnpParamsMap.put("vnp_BankCode", bankCode);
    //    }
    vnpParamsMap.put("vnp_IpAddr", ipaddr);
    vnpParamsMap.put(
        "vnp_OrderInfo",
        "Thanh toan don hang: %s. %s".formatted(VnPayUtil.getRandomNumber(8), payload.info()));
    // build query url
    String queryUrl = VnPayUtil.getPaymentURL(vnpParamsMap, true);
    String hashData = VnPayUtil.getPaymentURL(vnpParamsMap, false);
    String vnpSecureHash = VnPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
    queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
    String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    return VnPayResponse.builder().code("ok").message("success").paymentUrl(paymentUrl).build();
  }
}
