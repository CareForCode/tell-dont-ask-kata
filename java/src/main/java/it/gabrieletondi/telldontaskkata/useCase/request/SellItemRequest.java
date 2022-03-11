package it.gabrieletondi.telldontaskkata.useCase.request;

public class SellItemRequest {
    private final int quantity;
    private final String productName;

    public SellItemRequest(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }
}
