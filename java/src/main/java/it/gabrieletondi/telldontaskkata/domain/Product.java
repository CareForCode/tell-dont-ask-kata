package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.SellItemRequest;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private String name;
    private BigDecimal price;
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    private BigDecimal getUnitaryTax() {
        return price.divide(valueOf(100))
                .multiply(category.getTaxPercentage())
                .setScale(2, HALF_UP);
    }

    public BigDecimal getTaxedAmount(int quantity) {
        return price.add(getUnitaryTax())
                .setScale(2, HALF_UP)
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, HALF_UP);
    }

    public BigDecimal getTaxAmount(SellItemRequest itemRequest) {
        return getUnitaryTax().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
    }
}
