package org.example.bookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.example.bookstore.dto.cart.CartItemRequestDto;
import org.example.bookstore.dto.cart.CartItemResponseDto;
import org.example.bookstore.dto.cart.ShoppingCartResponseDto;
import org.example.bookstore.dto.cart.UpdateCartItemRequestDto;
import org.example.bookstore.model.User;
import org.example.bookstore.security.JwtUtil;
import org.example.bookstore.service.cart.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ShoppingCartController.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private JwtUtil jwtUtil;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setShippingAddress("Test Street 1");
    }

    private UsernamePasswordAuthenticationToken userAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void getShoppingCart_shouldReturnShoppingCart() throws Exception {
        ShoppingCartResponseDto response = new ShoppingCartResponseDto(
                1L,
                10L,
                Set.of(
                        new CartItemResponseDto(
                                100L,
                                5L,
                                "Java Programming",
                                2
                        )
                )
        );

        when(shoppingCartService.getShoppingCart(any(User.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/cart")
                                .with(authentication(userAuthentication()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.cartItems[0].id").value(100))
                .andExpect(jsonPath("$.cartItems[0].bookId").value(5))
                .andExpect(jsonPath("$.cartItems[0].bookTitle")
                        .value("Java Programming"))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2));
    }

    @Test
    void addCartItem_shouldReturnCreatedCartItem() throws Exception {
        CartItemRequestDto request =
                new CartItemRequestDto(5L, 2);

        CartItemResponseDto response =
                new CartItemResponseDto(
                        100L,
                        5L,
                        "Java Programming",
                        2
                );

        when(shoppingCartService.addCartItem(
                any(User.class),
                any(CartItemRequestDto.class)
        )).thenReturn(response);

        mockMvc.perform(
                        post("/api/cart")
                                .with(authentication(userAuthentication()))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.bookId").value(5))
                .andExpect(jsonPath("$.bookTitle")
                        .value("Java Programming"))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void updateCartItem_shouldReturnUpdatedCartItem() throws Exception {
        UpdateCartItemRequestDto request =
                new UpdateCartItemRequestDto(5);

        CartItemResponseDto response =
                new CartItemResponseDto(
                        100L,
                        5L,
                        "Java Programming",
                        5
                );

        when(shoppingCartService.updateCartItem(
                any(User.class),
                any(Long.class),
                any(UpdateCartItemRequestDto.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/cart/cart-items/100")
                                .with(authentication(userAuthentication()))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.bookId").value(5))
                .andExpect(jsonPath("$.bookTitle")
                        .value("Java Programming"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void removeCartItem_shouldReturnNoContent() throws Exception {
        mockMvc.perform(
                        delete("/api/cart/cart-items/100")
                                .with(authentication(userAuthentication()))
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(shoppingCartService)
                .removeCartItem(any(User.class), any(Long.class));
    }

    @Test
    void addCartItem_withInvalidRequest_shouldReturnBadRequest()
            throws Exception {

        CartItemRequestDto invalidRequest =
                new CartItemRequestDto(5L, 0);

        mockMvc.perform(
                        post("/api/cart")
                                .with(authentication(userAuthentication()))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                invalidRequest
                                        )
                                )
                )
                .andExpect(status().isBadRequest());
    }
}
