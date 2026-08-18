package org.example.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
