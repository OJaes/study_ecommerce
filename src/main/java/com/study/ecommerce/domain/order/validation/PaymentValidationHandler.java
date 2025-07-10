package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

@Slf4j
@Component
public class PaymentValidationHandler extends OrderValidationHandler{
    private static final BigDecimal MINIMUM_ORDER_AMOUNT = new BigDecimal("1000");
    private static final BigDecimal MAXIMUM_ORDER_AMOUNT = new BigDecimal("1000000");

    private static final Set<String> SUPPORTING_PAYMENT_METHODS = Set.of(
            "CARD", "BANK_TRANSFER","VIRTUAL_ACCOUNT","POINT"
    );

    @Override
    protected void doValidate(OrderCreateRequest request) {
        // 결제방법 검증
        // 지원하는 결제 방법인지 검증
        validateSupportingPaymentMethod(request);

        // 주문 총액 계산
        BigDecimal totalAmount = calculateTotalAmount(request);

        // 최소 주문 금액 검증
        // 최대 주문 금액 검증
        validateOrderAmountRange(request, totalAmount);

        // 결제 방법별 추가검증
        validatePaymentMethodSpecific(request, MINIMUM_ORDER_AMOUNT);
    }

    @Override
    protected String getHandlerName() {
        return "결제 검증";
    }

    private void validateSupportingPaymentMethod(OrderCreateRequest request) {
        String method = request.paymentMethod();
        if (!SUPPORTING_PAYMENT_METHODS.contains(method)) {
            fail("지원하지 않는 결제 방법입니다: " + method);
        }
    }

    private void validatePaymentMethodSpecific(OrderCreateRequest request, BigDecimal totalAmount) {
        String paymentMethod = request.paymentMethod();

        switch (paymentMethod) {
            case "CARD" -> validateCardPayment(request, totalAmount);
            case "BANK_TRANSFER" -> validateBankTransferPayment(request, totalAmount);
            case "VIRTUAL_ACCOUNT" -> validateVirtualAccountPayment(request, totalAmount);
            case "POINT" -> validatePointPayment(request, totalAmount);
        }
    }

    private BigDecimal calculateTotalAmount(OrderCreateRequest request) {
        return request.items().stream()
                .map(item -> {
                    if (item.getPrice() == null || item.getQuantity() == null) {
                        fail("상품 가격 또는 수량이 비어있습니다.");
                    }
                    return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private void validateOrderAmountRange(OrderCreateRequest request, BigDecimal totalAmount) {

        if (totalAmount.compareTo(MINIMUM_ORDER_AMOUNT) < 0) {
            fail("최소 주문 금액은 " + MINIMUM_ORDER_AMOUNT + "원 이상이어야 합니다.");
        }

        if (totalAmount.compareTo(MAXIMUM_ORDER_AMOUNT) > 0) {
            fail("최대 주문 금액은 " + MAXIMUM_ORDER_AMOUNT + "원을 초과할 수 없습니다.");
        }
    }

    private void validatePointPayment(OrderCreateRequest request, BigDecimal totalAmount) {
        // 포인트 compareTo 5만포인트
        if (totalAmount.compareTo(BigDecimal.valueOf(50000)) > 0) {
            fail("포인트 결제는 최대 5만원까지만 가능합니다.");
        }
    }

    private void validateVirtualAccountPayment(OrderCreateRequest request, BigDecimal totalAmount) {
        // 로그
        log.info(request.toString());
    }

    private void validateCardPayment(OrderCreateRequest request, BigDecimal totalAmount) {
        // 최소금액 검증
        if (totalAmount.compareTo(MINIMUM_ORDER_AMOUNT) < 0) {
            fail("카드 결제는 최소 " + MINIMUM_ORDER_AMOUNT + "원 이상이어야 합니다.");
        }
    }

    private void validateBankTransferPayment(OrderCreateRequest request, BigDecimal totalAmount) {
        //  영업시간에만 가능하다
        LocalTime now = LocalTime.now();
        LocalTime start = LocalTime.of(9, 0);  // 오전 9시
        LocalTime end = LocalTime.of(17, 0);   // 오후 5시

        if (now.isBefore(start) || now.isAfter(end)) {
            fail("계좌이체는 영업시간(09:00 ~ 17:00)에만 가능합니다.");
        }
    }
}