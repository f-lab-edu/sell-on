package com.sellon.payment.service;

import com.sellon.order.entity.Order;
import com.sellon.order.exception.OrderNotFoundException;
import com.sellon.order.repository.OrderRepository;
import com.sellon.payment.dto.ApprovePaymentRequest;
import com.sellon.payment.dto.ApprovePaymentResponse;
import com.sellon.payment.dto.CreatePaymentRequest;
import com.sellon.payment.dto.CreatePaymentResponse;
import com.sellon.payment.dto.PayApproveReponse;
import com.sellon.payment.dto.PayReadyResponse;
import com.sellon.payment.entity.Payment;
import com.sellon.payment.exception.PaymentFailedException;
import com.sellon.payment.mapper.PaymentMapper;
import com.sellon.payment.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final List<PayClient> payClients;

    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
        Order order = orderRepository.findByIdWithPessimisticLock(request.getOrderId())
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다: " + request.getOrderId()));

        Payment payment = paymentMapper.toEntity(request, order);
        paymentRepository.save(payment);

        PayClient payClient = payClients.stream()
            .filter(client -> client.isAvailable(request.getPaymentMethod()))
            .findFirst()
            .orElseThrow(() -> {
                paymentRepository.save(payment.fail("잘못된 결제 수단"));
                return new PaymentFailedException("결제 수단을 확인해주세요.");
            });

        PayReadyResponse payReadyResponse = payClient.ready(order);
        return CreatePaymentResponse.from(payment, payReadyResponse);
    }

    public ApprovePaymentResponse approvePayment(ApprovePaymentRequest request) {
        Payment payment = paymentRepository.findByIdWithPessimisticLock(request.getPaymentId())
            .orElseThrow(() -> new PaymentFailedException("정산을 찾을 수 없습니다: " + request.getPaymentId()));

        PayClient payClient = payClients.stream()
            .filter(client -> client.isAvailable(request.getPaymentMethod()))
            .findFirst()
            .orElseThrow(() -> {
                paymentRepository.save(payment.fail("잘못된 결제 수단"));
                return new PaymentFailedException("결제 수단을 확인해주세요.");
            });

        PayApproveReponse payApproveResponse = payClient.approve(paymentMapper.toPayApproveRequest(request));
        paymentRepository.save(payment.complete(LocalDateTime.now()));
        return ApprovePaymentResponse.from(payment, payApproveResponse);
    }

}
