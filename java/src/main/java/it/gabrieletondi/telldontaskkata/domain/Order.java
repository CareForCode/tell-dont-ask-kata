package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public Order(OrderStatus status, int id) {
        this.status = status;
        this.id = id;
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

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
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

    public void addProductToOrder(Product product, int quantity) {
        final BigDecimal taxAmount = product.getTaxAmount(quantity);
        final BigDecimal taxedAmount = product.getTaxedAmount(quantity);

        final OrderItem orderItem = new OrderItem(product, quantity, taxAmount, taxedAmount);
        addItem(orderItem);

        addTaxedAmountToTotal(taxedAmount);
        addTaxAmount(taxAmount);
    }

    public boolean isShipped() {
        return status == OrderStatus.SHIPPED;
    }

    public boolean isApproved() {
        return status == OrderStatus.APPROVED;
    }

    public boolean isRejected() {
        return status == OrderStatus.REJECTED;
    }

    public boolean isCreated() {
        return status == CREATED;
    }

    public void approveOrder(boolean approved) {
        status = approved ? OrderStatus.APPROVED : OrderStatus.REJECTED;
    }

    public void ship() {
        status = OrderStatus.SHIPPED;
    }
}
