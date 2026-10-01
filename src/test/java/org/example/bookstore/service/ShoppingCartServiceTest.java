package org.example.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.bookstore.dto.cart.CartItemRequestDto;
import org.example.bookstore.dto.cart.CartItemResponseDto;
import org.example.bookstore.dto.cart.ShoppingCartResponseDto;
import org.example.bookstore.dto.cart.UpdateCartItemRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CartItemMapper;
import org.example.bookstore.mapper.ShoppingCartMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.cart.CartItemRepository;
import org.example.bookstore.repository.cart.ShoppingCartRepository;
import org.example.bookstore.service.cart.ShoppingCartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    void getShoppingCart_shouldReturnShoppingCart() {
        User user = createUser(1L);
        ShoppingCart shoppingCart = createShoppingCart(10L, user);

        ShoppingCartResponseDto response =
                new ShoppingCartResponseDto(10L, 1L, java.util.Set.of());

        when(shoppingCartRepository.findByUserId(1L))
                .thenReturn(Optional.of(shoppingCart));

        when(shoppingCartMapper.toDto(shoppingCart))
                .thenReturn(response);

        ShoppingCartResponseDto result =
                shoppingCartService.getShoppingCart(user);

        assertThat(result).isSameAs(response);

        verify(shoppingCartRepository).findByUserId(1L);
        verify(shoppingCartMapper).toDto(shoppingCart);
    }

    @Test
    void getShoppingCart_shouldThrowException_whenCartDoesNotExist() {
        User user = createUser(1L);

        when(shoppingCartRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                shoppingCartService.getShoppingCart(user))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(
                        "Can't find shopping cart for user with id: 1"
                );
    }

    @Test
    void addCartItem_shouldAddNewCartItem() {
        User user = createUser(1L);
        ShoppingCart shoppingCart = createShoppingCart(10L, user);
        Book book = createBook(100L, "Java");

        CartItemRequestDto request =
                new CartItemRequestDto(100L, 3);

        CartItemResponseDto response =
                new CartItemResponseDto(1L, 100L, "Java", 3);

        when(shoppingCartRepository.findByUserId(1L))
                .thenReturn(Optional.of(shoppingCart));

        when(bookRepository.findById(100L))
                .thenReturn(Optional.of(book));

        when(cartItemRepository.findByShoppingCartIdAndBookId(
                10L,
                100L
        )).thenReturn(Optional.empty());

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(cartItemMapper.toDto(any(CartItem.class)))
                .thenReturn(response);

        CartItemResponseDto result =
                shoppingCartService.addCartItem(user, request);

        ArgumentCaptor<CartItem> captor =
                ArgumentCaptor.forClass(CartItem.class);

        verify(cartItemRepository).save(captor.capture());

        CartItem savedCartItem = captor.getValue();

        assertThat(result).isSameAs(response);
        assertThat(savedCartItem.getQuantity()).isEqualTo(3);
        assertThat(savedCartItem.getShoppingCart()).isSameAs(shoppingCart);
        assertThat(savedCartItem.getBook()).isSameAs(book);
    }

    @Test
    void addCartItem_shouldIncreaseQuantity_whenCartItemAlreadyExists() {
        User user = createUser(1L);
        ShoppingCart shoppingCart = createShoppingCart(10L, user);
        Book book = createBook(100L, "Java");

        CartItem existingCartItem = new CartItem();
        existingCartItem.setShoppingCart(shoppingCart);
        existingCartItem.setBook(book);
        existingCartItem.setQuantity(2);

        CartItemRequestDto request =
                new CartItemRequestDto(100L, 3);

        CartItemResponseDto response =
                new CartItemResponseDto(1L, 100L, "Java", 5);

        when(shoppingCartRepository.findByUserId(1L))
                .thenReturn(Optional.of(shoppingCart));

        when(bookRepository.findById(100L))
                .thenReturn(Optional.of(book));

        when(cartItemRepository.findByShoppingCartIdAndBookId(
                10L,
                100L
        )).thenReturn(Optional.of(existingCartItem));

        when(cartItemRepository.save(existingCartItem))
                .thenReturn(existingCartItem);

        when(cartItemMapper.toDto(existingCartItem))
                .thenReturn(response);

        CartItemResponseDto result =
                shoppingCartService.addCartItem(user, request);

        assertThat(existingCartItem.getQuantity()).isEqualTo(5);
        assertThat(result).isSameAs(response);

        verify(cartItemRepository).save(existingCartItem);
    }

    @Test
    void addCartItem_shouldThrowException_whenBookDoesNotExist() {
        User user = createUser(1L);
        ShoppingCart shoppingCart = createShoppingCart(10L, user);

        CartItemRequestDto request =
                new CartItemRequestDto(100L, 3);

        when(shoppingCartRepository.findByUserId(1L))
                .thenReturn(Optional.of(shoppingCart));

        when(bookRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                shoppingCartService.addCartItem(user, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find book by id: 100");
    }

    @Test
    void updateCartItem_shouldUpdateQuantity() {
        User user = createUser(1L);
        ShoppingCart shoppingCart = createShoppingCart(10L, user);
        Book book = createBook(100L, "Java");

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        UpdateCartItemRequestDto request =
                new UpdateCartItemRequestDto(7);

        CartItemResponseDto response =
                new CartItemResponseDto(1L, 100L, "Java", 7);

        when(cartItemRepository.findByIdAndShoppingCartUserId(1L, 1L))
                .thenReturn(Optional.of(cartItem));

        when(cartItemRepository.save(cartItem))
                .thenReturn(cartItem);

        when(cartItemMapper.toDto(cartItem))
                .thenReturn(response);

        CartItemResponseDto result =
                shoppingCartService.updateCartItem(
                        user,
                        1L,
                        request
                );

        assertThat(cartItem.getQuantity()).isEqualTo(7);
        assertThat(result).isSameAs(response);

        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void updateCartItem_shouldThrowException_whenCartItemDoesNotExist() {
        User user = createUser(1L);

        UpdateCartItemRequestDto request =
                new UpdateCartItemRequestDto(7);

        when(cartItemRepository.findByIdAndShoppingCartUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                shoppingCartService.updateCartItem(
                        user,
                        1L,
                        request
                ))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find cart item by id: 1");
    }

    @Test
    void removeCartItem_shouldDeleteCartItem() {
        User user = createUser(1L);
        ShoppingCart shoppingCart = createShoppingCart(10L, user);
        Book book = createBook(100L, "Java");

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        when(cartItemRepository.findByIdAndShoppingCartUserId(1L, 1L))
                .thenReturn(Optional.of(cartItem));

        shoppingCartService.removeCartItem(user, 1L);

        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void removeCartItem_shouldThrowException_whenCartItemDoesNotExist() {
        User user = createUser(1L);

        when(cartItemRepository.findByIdAndShoppingCartUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                shoppingCartService.removeCartItem(user, 1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can't find cart item by id: 1");

        verify(
                cartItemRepository,
                org.mockito.Mockito.never()
        ).delete(any(CartItem.class));
    }

    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private ShoppingCart createShoppingCart(
            Long id,
            User user
    ) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(id);
        shoppingCart.setUser(user);
        return shoppingCart;
    }

    private Book createBook(Long id, String title) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        return book;
    }
}
