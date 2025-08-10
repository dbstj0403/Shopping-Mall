package com.example.hanaro.domain.cart.service;

import com.example.hanaro.domain.cart.dto.AddToCartRequestDto;
import com.example.hanaro.domain.cart.dto.CartItemDto;
import com.example.hanaro.domain.cart.dto.CartResponseDto;
import com.example.hanaro.domain.cart.dto.UpdateCartItemRequestDto;
import com.example.hanaro.domain.cart.entity.Cart;
import com.example.hanaro.domain.cart.entity.CartItem;
import com.example.hanaro.domain.cart.repository.CartItemRepository;
import com.example.hanaro.domain.cart.repository.CartRepository;
import com.example.hanaro.domain.product.entity.Product;
import com.example.hanaro.domain.product.repository.ProductRepository;
import com.example.hanaro.domain.user.entity.User;
import com.example.hanaro.domain.user.repository.UserRepository;
import com.example.hanaro.global.error.CustomException;
import com.example.hanaro.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /** 담기(있으면 수량 증가, 없으면 새로 추가) — 재고 제한 없음 */
    @Transactional
    public CartResponseDto addItem(Long userId, AddToCartRequestDto req) {
        if (req.quantity() <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT, "수량은 1 이상이어야 합니다.");
        }

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> createCartFor(userId));

        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + req.productId()));

        CartItem item = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), product.getId())
                .orElse(null);

        if (item == null) {
            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(req.quantity())
                    .build();
        } else {
            item.setQuantity(item.getQuantity() + req.quantity());
        }

        cartItemRepository.save(item);
        return getCart(userId); // 최신 상태 반환
    }

    /** 조회 — 없으면 빈 장바구니 생성 */
    @Transactional
    public CartResponseDto getCart(Long userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> createCartFor(userId));

        List<CartItemDto> items = cartItemRepository.findAllByCart_Id(cart.getId())
                .stream()
                .map(CartItemDto::from)
                .toList();

        return CartResponseDto.of(items);
    }

    /** 내부: 장바구니 없으면 생성 */
    @Transactional
    protected Cart createCartFor(Long userId) {
        return cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
    }

    /** 수량 수정(덮어쓰기) — 0이면 삭제, 재고 제한 없음 */
    @Transactional
    public CartResponseDto updateItemQuantity(Long userId, Long productId, UpdateCartItemRequestDto req) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "장바구니가 존재하지 않습니다."));

        CartItem item = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "장바구니에 해당 상품이 없습니다."));

        int q = req.quantity();

        if (q == 0) {
            cartItemRepository.delete(item);
            return getCart(userId);
        }

        item.setQuantity(q);
        cartItemRepository.save(item);
        return getCart(userId);
    }

    /** 아이템 삭제 */
    @Transactional
    public CartResponseDto removeItem(Long userId, Long productId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "장바구니가 존재하지 않습니다."));

        CartItem item = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "장바구니에 해당 상품이 없습니다."));

        cartItemRepository.delete(item);
        return getCart(userId);
    }

    /** 비우기 */
    @Transactional
    public CartResponseDto clearCart(Long userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> createCartFor(userId));

        cart.getItems().clear(); // orphanRemoval=true 가정
        cartRepository.save(cart);

        return getCart(userId);
    }
}
