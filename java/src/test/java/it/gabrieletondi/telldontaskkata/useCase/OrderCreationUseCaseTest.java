package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.*;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.exception.UnknownProductException;
import it.gabrieletondi.telldontaskkata.useCase.request.SellItemRequest;
import it.gabrieletondi.telldontaskkata.useCase.request.SellItemsRequest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrderCreationUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final Category food = new Category() {{
        setTaxPercentage(new BigDecimal("10"));
    }};;
    private final ProductCatalog productCatalog = new InMemoryProductCatalog(
            Arrays.<Product>asList(
                    new Product("salad", new BigDecimal("3.56"), food),
                    new Product("tomato", new BigDecimal("4.65"), food)
            )
    );

    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

    @Test
    public void sellMultipleItems() throws Exception {
        final SellItemsRequest request = new SellItemsRequest(new ArrayList<>());
        request.addRequest(new SellItemRequest("salad", 2));
        request.addRequest(new SellItemRequest("tomato", 3));

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();
        assertOrder(insertedOrder, OrderStatus.CREATED, new BigDecimal("23.20"), new BigDecimal("2.13"), "EUR", 2);
        assertOrderItem(insertedOrder.getItems().get(0), "salad", 2,
                new BigDecimal("3.56"), new BigDecimal("7.84"), new BigDecimal("0.72"));
        assertOrderItem(insertedOrder.getItems().get(1), "tomato", 3,
                new BigDecimal("4.65"), new BigDecimal("15.36"), new BigDecimal("1.41"));
    }

    private void assertOrder(Order insertedOrder, OrderStatus orderStatus, BigDecimal total, BigDecimal tax, String currency, int expectedItemSize) {
        assertThat(insertedOrder.getStatus(), is(orderStatus));
        assertThat(insertedOrder.getTotal(), is(total));
        assertThat(insertedOrder.getTax(), is(tax));
        assertThat(insertedOrder.getCurrency(), is(currency));
        assertThat(insertedOrder.getItems(), hasSize(expectedItemSize));
    }

    private void assertOrderItem(OrderItem orderItem, String name, int quantity, BigDecimal price, BigDecimal taxedAmount, BigDecimal tax) {
        assertThat(orderItem.getProduct().getName(), is(name));
        assertThat(orderItem.getProduct().getPrice(), is(price));
        assertThat(orderItem.getQuantity(), is(quantity));
        assertThat(orderItem.getTaxedAmount(), is(taxedAmount));
        assertThat(orderItem.getTax(), is(tax));
    }

    @Test(expected = UnknownProductException.class)
    public void unknownProduct() throws Exception {
        SellItemsRequest request = new SellItemsRequest(new ArrayList<>());
        request.addRequest(new SellItemRequest("unknown product", 0));

        useCase.run(request);
    }
}
