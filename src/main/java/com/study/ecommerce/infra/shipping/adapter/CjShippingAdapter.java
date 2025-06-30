package com.study.ecommerce.infra.shipping.adapter;

import com.study.ecommerce.infra.shipping.dto.ShippingRequest;
import com.study.ecommerce.infra.shipping.dto.ShippingResponse;
import com.study.ecommerce.infra.shipping.external.CjShippingApi;
import com.study.ecommerce.infra.shipping.external.CjShippingRequest;
import com.study.ecommerce.infra.shipping.external.CjShippingResponse;
import com.study.ecommerce.infra.shipping.external.CjTrackingResponse;
import com.study.ecommerce.infra.shipping.gateway.ShippingGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Adapter Pattern: CJ대한통운 어댑터
 *
 * CJ대한통운의 고유한 API를 우리 시스템의 공통 인터페이스에 맞게 변환
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CjShippingAdapter implements ShippingGateway {
    private final CjShippingApi cjShippingApi;

    @Override
    public ShippingResponse registerShipping(ShippingRequest request) {
        log.info("CJ대한통운 어댑터 - 배송 등록 요청 변환 및 처리");

        // 1. 우리 시스템의 요청을 CJ API 형태로 변환
        CjShippingRequest cjRequest = convertToCjRequest(request);

        // 2. CJ API 호출
        CjShippingResponse cjResponse = cjShippingApi.registerDelivery(cjRequest);

        // 3. CJ 응답을 우리 시스템 형태로 변환
        return convertToShippingResponse(cjResponse);
    }

    @Override
    public ShippingResponse getShippingStatus(String trackingNumber) {
        log.info("CJ대한통운 어댑터 - 배송 상태 조회: {}", trackingNumber);
        CjTrackingResponse cjResponse = cjShippingApi.getTrackingInfo(trackingNumber);
        return convertTrackingToShippingResponse(cjResponse);
    }

    @Override
    public ShippingResponse cancelShipping(String trackingNumber, String reason) {
        log.info("CJ대한통운 어댑터 - 배송 취소: {}, 사유: {}", trackingNumber, reason);

        CjShippingResponse cjResponse = cjShippingApi.cancelDelivery(trackingNumber, reason);
        return convertToShippingResponse(cjResponse);
    }

    @Override
    public int calculateShippingCost(ShippingRequest request) {
        // CJ API를 통해 실제 배송비 계산 (여기서는 간단 계산)

        return cjShippingApi.registerDelivery(convertToCjRequest(request)).deliveryCharge();
    }

    @Override
    public String getCarrierName() {
        return "CJ대한통운";
    }

    /**
     * 공통 요청을 CJ API 요청으로 변환
     */
    private CjShippingRequest convertToCjRequest(ShippingRequest request) {

        return CjShippingRequest.builder()
                .orderNo(request.orderId())
                .senderName(request.senderName())
                .senderTel(request.senderPhone())
                .senderAddr(request.senderAddress())
                .receiverName(request.receiverName())
                .receiverTel(request.receiverPhone())
                .receiverAddr(request.receiverAddress())
                .receiverZipCode(request.receiverZipCode())
                .weight(request.weight())
                .boxType(convertPackageType(request.packageType()))
                .specialService("01")
                .deliveryMessage(request.deliveryMessage())
                .build();

    }

    /**
     * 패키지 타입 변환
     */
    private String convertPackageType(String packageType) {
        return switch (packageType) {
            case "BOX" -> "1";
            case "ENVELOPE" -> "2";
            case "BAG" -> "3";
            default -> "1";
        };
    }

    /**
     * CJ API 응답을 공통 응답으로 변환
     */
    private ShippingResponse convertToShippingResponse(CjShippingResponse cjResponse) {
        boolean isSuccess = "0000".equals(cjResponse.resultCode());

        return ShippingResponse.builder()
                .success(isSuccess)
                .trackingNumber(cjResponse.invoiceNo())
                .message(cjResponse.resultMessage())
                .shippingCost(cjResponse.deliveryCharge())
                .carrierName("CJ대한통운")
                .errorCode(isSuccess ? null : cjResponse.resultCode())
                .build();
    }

    /**
     * CJ 배송 조회 응답을 공통 응답으로 변환
     */
    private ShippingResponse convertTrackingToShippingResponse(CjTrackingResponse cjResponse) {
        boolean isSuccess = "0000".equals(cjResponse.resultCode());

        return ShippingResponse.builder()
                .success(isSuccess)
                .trackingNumber(cjResponse.invoiceNo())
                .status(convertCjStatus(cjResponse.deliveryStatus()))
                .message(cjResponse.resultMessage())
                .shippingCost(0) // 배송조회 시점에는 배송비 정보 없음
                .estimatedDeliveryDate(parseDateTime(cjResponse.deliveryDateTime()))
                .carrierName("CJ대한통운")
                .errorCode(isSuccess ? null : cjResponse.resultCode())
                .build();
    }

    /**
     * CJ 배송 상태를 공통 상태로 변환
     */
    private String convertCjStatus(String cjStatus) {
        if (cjStatus == null) return "REGISTERED";

        return switch (cjStatus) {
            case "10" -> "REGISTERED"; // 접수
            case "20" -> "PICKED_UP";  // 집하
            case "30" -> "IN_TRANSIT"; // 배송중
            case "40" -> "DELIVERED";  // 배송완료
            default -> "REGISTERED";
        };
    }

    private LocalDateTime parseDateTime(String datetime) {
        if (datetime == null || datetime.isBlank()) return null;
        try {
            return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            log.warn("CJ 응답 날짜 파싱 실패: {}", datetime);
            return null;
        }
    }

}