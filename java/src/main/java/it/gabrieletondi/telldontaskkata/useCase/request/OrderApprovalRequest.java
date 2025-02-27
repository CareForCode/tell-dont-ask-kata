package it.gabrieletondi.telldontaskkata.useCase.request;

public class OrderApprovalRequest {
    private final int orderId;
    private final boolean approved;

    public OrderApprovalRequest(int orderId, boolean approved) {
        this.orderId = orderId;
        this.approved = approved;
    }

    public int getOrderId() {
        return orderId;
    }

    public boolean isApproved() {
        return approved;
    }
}
