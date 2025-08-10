package com.example.hanaro.domain.order.service;

import com.example.hanaro.domain.cart.entity.Cart;
import com.example.hanaro.domain.cart.entity.CartItem;
import com.example.hanaro.domain.cart.repository.CartItemRepository;
import com.example.hanaro.domain.cart.repository.CartRepository;
import com.example.hanaro.domain.order.dto.OrderListItemDto;
import com.example.hanaro.domain.order.dto.OrderResponseDto;
import com.example.hanaro.domain.order.entity.Order;
import com.example.hanaro.domain.order.entity.OrderItem;
import com.example.hanaro.domain.order.entity.OrderStatus;
import com.example.hanaro.domain.order.repository.OrderRepository;
import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.repository.ProductRepository;
import com.example.hanaro.global.error.CustomException;
import com.example.hanaro.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    /** 장바구니 기반 주문 생성 (재고 검증+차감, 실패 시 전체 롤백) */
    @Transactional
    public OrderResponseDto createFromCart(Long userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "장바구니가 없습니다."));

        List<CartItem> cartItems = cartItemRepository.findAllByCart_Id(cart.getId());
        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "장바구니가 비어 있습니다.");
        }

        Order order = Order.builder()
                .user(cart.getUser())
                .status(OrderStatus.PAYMENT_COMPLETED) // 요구 상태 플로우에 맞춰 시작
                .totalAmount(0)
                .totalQuantity(0)
                .build();

        int totalQty = 0;
        int totalAmt = 0;

        for (CartItem ci : cartItems) {
            Product p = ci.getProduct();
            int qty = ci.getQuantity();

            int updated = productRepository.decreaseStockIfEnough(p.getId(), qty);
            if (updated == 0) {
                throw new CustomException(ErrorCode.INVALID_INPUT, "재고 부족: " + p.getName());
            }

            int line = p.getPrice() * qty;

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(p)
                    .productName(p.getName())
                    .unitPrice(p.getPrice())
                    .quantity(qty)
                    .lineTotal(line)
                    .build();

            order.addItem(oi);
            totalQty += qty;
            totalAmt += line;
        }

        order.setTotalQuantity(totalQty);
        order.setTotalAmount(totalAmt);

        Order saved = orderRepository.save(order);

        // 장바구니 비우기
        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public java.util.List<OrderListItemDto> getMyOrders(Long userId) {
        return orderRepository.findAllByUser_IdOrderByIdDesc(userId)
                .stream()
                .map(OrderListItemDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getMyOrder(Long userId, Long orderId) {
        var order = orderRepository.findByIdAndUser_Id(orderId, userId)
                .orElseThrow(() -> new com.example.hanaro.global.error.CustomException(
                        com.example.hanaro.global.error.ErrorCode.NOT_FOUND, "주문을 찾을 수 없습니다."
                ));
        return OrderResponseDto.from(order);
    }
}
