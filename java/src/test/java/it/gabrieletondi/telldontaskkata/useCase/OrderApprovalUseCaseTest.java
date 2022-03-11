package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.useCase.exception.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.useCase.exception.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.useCase.exception.ShippedOrdersCannotBeChangedException;
import it.gabrieletondi.telldontaskkata.useCase.request.OrderApprovalRequest;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderApprovalUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    public void approvedExistingOrder() throws Exception {
        Order initialOrder = new Order(OrderStatus.CREATED, 1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = getOrderApprovalRequest(true);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus(), is(OrderStatus.APPROVED));
    }

    private OrderApprovalRequest getOrderApprovalRequest(boolean approved) {
        return new OrderApprovalRequest(1, approved);
    }

    @Test
    public void rejectedExistingOrder() throws Exception {
        Order initialOrder = new Order(OrderStatus.CREATED, 1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = getOrderApprovalRequest(false);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus(), is(OrderStatus.REJECTED));
    }

    @Test(expected = RejectedOrderCannotBeApprovedException.class)
    public void cannotApproveRejectedOrder() throws Exception {
        Order initialOrder = new Order(OrderStatus.REJECTED, 1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = getOrderApprovalRequest(true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ApprovedOrderCannotBeRejectedException.class)
    public void cannotRejectApprovedOrder() throws Exception {
        Order initialOrder = new Order(OrderStatus.APPROVED, 1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = getOrderApprovalRequest(false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeApproved() throws Exception {
        Order initialOrder = new Order(OrderStatus.SHIPPED, 1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = getOrderApprovalRequest(true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeRejected() throws Exception {
        Order initialOrder = new Order(OrderStatus.SHIPPED, 1);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = getOrderApprovalRequest(false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }
}
