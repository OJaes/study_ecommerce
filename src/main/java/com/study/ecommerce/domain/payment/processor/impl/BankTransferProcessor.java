package com.study.ecommerce.domain.payment.processor.impl;

import com.study.ecommerce.domain.payment.dto.PaymentRequest;
import com.study.ecommerce.domain.payment.dto.PaymentResult;
import com.study.ecommerce.domain.payment.processor.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BankTransferProcessor implements PaymentProcessor {
    private static final int MAX_AMOUNT = 10_000_000;
    private static final int FIXED_FEE = 500;

    @Override
    public PaymentResult process(PaymentRequest request) {
        log.info("은행 이체 처리 시작 - 주문 ID: {}, 금액: {}, 계좌번호: {}",
                request.orderId(), request.amount(), request.accountNumber());

        if (!supports(request.paymentMethod())) {
            log.warn("지원하지 않는 결제 수단: {}", request.paymentMethod());
            return PaymentResult.builder()
                    .success(false)
                    .message("지원하지 않는 결제 수단입니다.")
                    .paymentMethod("BANK")
                    .build();
        }


        if (request.amount() > MAX_AMOUNT) {
            log.warn("결제 금액 초과: {} > 최대 허용 금액 {}", request.amount(), MAX_AMOUNT);
            return PaymentResult.builder()
                    .success(false)
                    .message("최대 금액 초과입니다.")
                    .paymentMethod("BANK")
                    .build();
        }

        if (!isValidAccount(request.accountNumber())) {
            log.warn("유효하지 않은 계좌번호: {}", request.accountNumber());
            return PaymentResult.builder()
                    .success(false)
                    .message("유효하지 않은 계좌번호입니다.")
                    .build();

        }

        int fee = calculateFee(request.amount());
        String transactionId = UUID.randomUUID().toString();

        log.info("은행 이체 성공 - 주문 ID: {}, 수수료: {}", request.orderId(), FIXED_FEE);
        return PaymentResult.builder()
                .success(true)
                .transactionId(transactionId)
                .paymentMethod("BANK")
                .message("은행 이체에 성공했습니다.")
                .feeAmount(FIXED_FEE)
                .paidAmount(request.amount())
                .build();
    }


    @Override
    public int calculateFee(int amount) {

        return FIXED_FEE;
    }

    @Override
    public boolean supports(String paymentMethod) {
        return "BANK_TRANSFER".equalsIgnoreCase(paymentMethod);
    }

    @Override
    public int getMaxAmount() {

        return MAX_AMOUNT;
    }

    // isValidAccount -> 10자리이상, 14자리 이하
    private boolean isValidAccount(String accountNumber) {
        return accountNumber != null
                && accountNumber.length() >= 10
                && accountNumber.length() <= 14
                && accountNumber.chars().allMatch(Character::isDigit);
    }
}