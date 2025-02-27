package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.exception.UnknownProductException;
import it.gabrieletondi.telldontaskkata.useCase.request.SellItemRequest;
import it.gabrieletondi.telldontaskkata.useCase.request.SellItemsRequest;

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
        addItemsRequestToOrder(request, order);
        orderRepository.save(order);
    }

    private void addItemsRequestToOrder(SellItemsRequest request, Order order) {
        for (SellItemRequest itemRequest : request.getRequests()) {
            addItemRequestToOrder(order, itemRequest);
        }
    }

    private void addItemRequestToOrder(Order order, SellItemRequest itemRequest) {
        Product product = productCatalog.getByName(itemRequest.getProductName());
        addProductToOrder(order, itemRequest, product);
    }

    private void addProductToOrder(Order order, SellItemRequest itemRequest, Product product) {
        if (product == null) {
            throw new UnknownProductException();
        }
        else {
            order.addProductToOrder(product, itemRequest.getQuantity());
        }
    }

}
