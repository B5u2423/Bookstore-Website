package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.VnPayRequest;
import dev.vubl.bookstore.dtos.VnPayResponse;
import dev.vubl.bookstore.services.VnPayService;
import dev.vubl.bookstore.utils.VnPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
  private final VnPayService vnPayService;

  @PostMapping("/create-payment")
  public ResponseEntity<VnPayResponse> createPayment(
      @RequestBody VnPayRequest payload, HttpServletRequest request) {
    return ResponseEntity.ok()
        .body(vnPayService.createVnPayPayment(payload, VnPayUtil.getIpAddress(request)));
  }

  @GetMapping("/payment-callback")
  public ResponseEntity<VnPayResponse> payCallbackHandler(HttpServletRequest request) {
    String status = request.getParameter("vnp_ResponseCode");
    return ResponseEntity.ok()
        .body(
            VnPayResponse.builder()
                .code(status)
                .message("00".equals(status) ? "Success" : "Error")
                .paymentUrl("")
                .build());
  }
}
