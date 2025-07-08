package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.member.service.command.MemberCommandService;
import com.study.ecommerce.domain.member.service.query.MemberQueryService;
import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberValidationHandler extends OrderValidationHandler{

    private MemberQueryService memberQueryService;
    private MemberCommandService memberCommandService;
    private OrderService orderService;


    @Override
    protected void doValidate(OrderCreateRequest request) {
        Long memberId = request.memberId();
    }

    @Override
    protected String getHandlerName() {
        return "";
    }

    private boolean isMemberExists(Long memberId) {
        // memberService -> existsById(memberId)
        return memberQueryService.existsById(memberId);
    }

    private boolean isMemberActive(Long memberId) {
        // isActive(memberId);
        return true;
    }

    private boolean hasOrderPermission(Long memberId) {
        // hasOrderPermission(memberId);
        return true;
    }

    private boolean isCreditWorthy(Long memberId) {
        // getCreditScore(memberId) >= MINIMUM_CREDIT_SCORE;
        return true;
    }

    private boolean exceedsDailyOrderLimit(Long memberId, OrderCreateRequest request) {
        // OrderService 에서 일일 주문내역확인
        // BigDecimal todayOrderAmount = orderService.getTodayOrderAmount(memberId);
        // BigDecimal requestAmount = calculateTotalAmount(request);
        // return todayOrderAmount.add(requestAmount).compareTo(DAILY_ORDER_LIMIT) > 0;
        return true;
    }
}