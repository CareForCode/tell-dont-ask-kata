package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.useCase.exception.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.useCase.exception.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.useCase.exception.ShippedOrdersCannotBeChangedException;
import it.gabrieletondi.telldontaskkata.useCase.request.OrderApprovalRequest;

public class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    public OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        if (order.isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (isApproveRejectedOrder(request, order)) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (isRejectApprovedOrder(request, order)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        order.approveOrder(request.isApproved());
        orderRepository.save(order);
    }

    private boolean isRejectApprovedOrder(OrderApprovalRequest request, Order order) {
        return !request.isApproved() && order.isApproved();
    }

    private boolean isApproveRejectedOrder(OrderApprovalRequest request, Order order) {
        return request.isApproved() && order.isRejected();
    }

}
