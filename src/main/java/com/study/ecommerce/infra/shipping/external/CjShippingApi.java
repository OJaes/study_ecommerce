package com.study.ecommerce.infra.shipping.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CJ대한통운 외부 API (실제 API 호출 시뮬레이션)
 */
@Slf4j
@Component
public class CjShippingApi {
    /**
     * CJ대한통운 배송 등록
     */
    public CjShippingResponse registerDelivery(CjShippingRequest request) {
        log.info("CJ대한통운 API 호출 : {}", request);

        String invoiceNo = generateInvoiceNo();

        int charge = calculateDeliveryCharge(request);

        return CjShippingResponse.builder()
                .resultCode("0000")
                .resultMessage("DONE")
                .invoiceNo(invoiceNo)
                .orderNo(request.orderNo())
                .deliveryCharge(charge)
                .build();
    }

    /**
     * 배송 상태 조회
     */
    public CjTrackingResponse getTrackingInfo(String invoiceNo) {
        log.info("CJ대한통운 API 호출 - 배송 조회: 송장번호={}", invoiceNo);

        return new CjTrackingResponse(
                "0000",
                "SUCCESS",
                invoiceNo,
                "30",
                "배송중",
                "부산 물류센터",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    /**
     * 배송 취소
     */
    public CjShippingResponse cancelDelivery(String invoiceNo, String cancelReason) {
        log.info("CJ대한통운 API 호출 - 배송 취소: 송장번호={}, 사유={}", invoiceNo, cancelReason);

        return CjShippingResponse.builder()
                .resultCode("0001")
                .resultMessage("CANCELLED")
                .invoiceNo(invoiceNo)
                .deliveryCharge(0)
                .build();
    }

    /**
     * 배송비 계산 (CJ 고유 로직)
     */
    private int calculateDeliveryCharge(CjShippingRequest request) {
        int baseCharge = 3000; // 기본 배송비

        // 무게에 따른 추가 요금
        // 5kg 초과

        int weightKg = (int) Math.ceil(request.weight() / 1000.0);
        if (weightKg > 5) {
            baseCharge += (weightKg - 5) * 500;
        }

        // 제주도/도서산간 추가 요금

        String addr = request.receiverAddr();
        if (addr.contains("제주") || addr.contains("도서") || addr.contains("산간")) {
            baseCharge += 3000;
        }

        return baseCharge;
    }

    private String generateInvoiceNo() {
        return "CJ" + System.currentTimeMillis(); // 임의 송장번호 생성
    }
}