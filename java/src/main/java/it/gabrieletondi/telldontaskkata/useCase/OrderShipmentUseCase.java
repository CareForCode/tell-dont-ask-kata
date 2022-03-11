package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.exception.OrderCannotBeShippedException;
import it.gabrieletondi.telldontaskkata.useCase.exception.OrderCannotBeShippedTwiceException;
import it.gabrieletondi.telldontaskkata.useCase.request.OrderShipmentRequest;

public class OrderShipmentUseCase {
    private final OrderRepository orderRepository;
    private final ShipmentService shipmentService;

    public OrderShipmentUseCase(OrderRepository orderRepository, ShipmentService shipmentService) {
        this.orderRepository = orderRepository;
        this.shipmentService = shipmentService;
    }

    public void run(OrderShipmentRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());
        checkShipping(order);
        shipmentService.ship(order);
        order.ship();
        orderRepository.save(order);
    }

    private void checkShipping(Order order) {
        if (order.isCreated() || order.isRejected()) {
            throw new OrderCannotBeShippedException();
        }

        if (order.isShipped()) {
            throw new OrderCannotBeShippedTwiceException();
        }
    }


}
