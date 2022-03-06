package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public Order() {
    }

    public Order(OrderStatus status, List<OrderItem> items, String currency, BigDecimal total, BigDecimal tax) {
        this.total = total;
        this.currency = currency;
        this.items = items;
        this.tax = tax;
        this.status = status;
    }

    public static Order createNewOrder() {
        return new Order(OrderStatus.CREATED, new ArrayList<>(),
                "EUR",
                new BigDecimal("0.00"),
                new BigDecimal("0.00"));
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
    }

    public void addTaxedAmountToTotal(BigDecimal taxedAmount) {
        total = total.add(taxedAmount);
    }

    public void addTaxAmount(BigDecimal taxAmount) {
        tax = tax.add(taxAmount);
    }
}
