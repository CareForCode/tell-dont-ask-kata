package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        Order order = Order.createNewOrder();

        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = productCatalog.getByName(itemRequest.getProductName());

            if (product == null) {
                throw new UnknownProductException();
            }
            else {
                final BigDecimal taxAmount = product.getTaxAmount(itemRequest);
                final BigDecimal taxedAmount = product.getTaxedAmount(itemRequest.getQuantity());

                final OrderItem orderItem = new OrderItem(product, itemRequest.getQuantity(), taxAmount, taxedAmount);
                order.addItem(orderItem);

                order.addTaxedAmountToTotal(taxedAmount);
                order.addTaxAmount(taxAmount);
            }
        }

        orderRepository.save(order);
    }

}
