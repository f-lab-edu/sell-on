package com.sellon.payment.controller;

import com.sellon.payment.dto.examplePG.ExamplePgResponse;
import com.sellon.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/card")
    public ResponseEntity<ExamplePgResponse> processCardPayment(@RequestBody CardPaymentRequest request) {

    }
}
