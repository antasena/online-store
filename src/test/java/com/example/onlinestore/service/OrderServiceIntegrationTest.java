package com.example.onlinestore.service;

import com.example.onlinestore.api.request.OrderItemRequest;
import com.example.onlinestore.api.request.OrderRequest;
import com.example.onlinestore.exceptionhandling.BusinessValidationError;
import com.example.onlinestore.model.Orders;
import com.example.onlinestore.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceIntegrationTest {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ProductService productService;

    private static final Long CUSTOMER_ID = 1L;
    private static final Long ADDRESS_ID = 1L;
    private static final Long PRODUCT1_ID = 1L;
    private static final Long PRODUCT2_ID = 2L;
    private static final Long PRODUCT3_ID = 3L;
    private static final int QTY_1 = 10;
    private static final int QTY_2 = 5;
    private static final int QTY_3 = 10;

    //happy path
    @Test
    public void processOrderRequest() {
        Product product1 = productService.findById(PRODUCT1_ID);
        int initialStockProduct1 = product1.getUnitInStock();
        Long initalVersion1 = product1.getVersion();
        Product product2 = productService.findById(PRODUCT2_ID);
        int initialStockProduct2 = product2.getUnitInStock();
        Long initalVersion2 = product2.getVersion();

        OrderRequest orderRequest = createOrderRequest();

        Long createdId = ordersService.processOrder(orderRequest).getId();
        //verify the order order
        Orders createdOrder = ordersService.findById(createdId);
        assertNotNull(createdOrder);
        assertEquals("New", createdOrder.getStatus());
        assertEquals(0L, createdOrder.getVersion());
        assertEquals(1, createdOrder.getRecordStatus());

        // verify the product stock and version
        product1 = productService.findById(PRODUCT1_ID);
        assertEquals(++initalVersion1, product1.getVersion());
        assertEquals(initialStockProduct1 - QTY_1, product1.getUnitInStock());

        product2 = productService.findById(PRODUCT2_ID);
        assertEquals(++initalVersion2, product2.getVersion());
        assertEquals(initialStockProduct2 - QTY_2, product2.getUnitInStock());
    }

    //retry order if got optimistic-lock
    @Test
    public void processOrderRequest_withOptimisticLockHandling() throws InterruptedException {
        Product product1 = productService.findById(PRODUCT1_ID);
        int initialStockProduct1 = product1.getUnitInStock();
        Long initalVersion1 = product1.getVersion();
        Product product2 = productService.findById(PRODUCT2_ID);
        int initialStockProduct2 = product2.getUnitInStock();
        Long initalVersion2 = product2.getVersion();

        List<OrderRequest> multipleOrders = Arrays.asList(
                createOrderRequest(), createOrderRequest(), createOrderRequest()
        );
        final ExecutorService executor = Executors.newFixedThreadPool(multipleOrders.size());

        //process multiple orders with same product
        List<Long> orderIds = new ArrayList<>();
        for (final OrderRequest orderRequest : multipleOrders) {
            executor.execute(() -> {
                Long id = ordersService.processOrder(orderRequest).getId();
                orderIds.add(id);
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

        for (Long id : orderIds) {
            Orders createdOrder = ordersService.findById(id);
            assertNotNull(createdOrder);
            assertEquals("New", createdOrder.getStatus());
        }

        // verify the product stock and version
        product1 = productService.findById(PRODUCT1_ID);
        assertEquals(initalVersion1 + multipleOrders.size(), product1.getVersion());
        assertEquals(initialStockProduct1 - (QTY_1 * multipleOrders.size()), product1.getUnitInStock());
        product2 = productService.findById(PRODUCT2_ID);
        assertEquals(initalVersion2 + multipleOrders.size(), product2.getVersion());
        assertEquals(initialStockProduct2 - (QTY_2 * multipleOrders.size()), product2.getUnitInStock());
    }

    //throws business validation
    @Test
    public void processOrderRequest_throwsBusinessValidationError() {
        Product product3 = productService.findById(PRODUCT3_ID);
        int initialStockProduct3 = product3.getUnitInStock();
        Long initalVersion3 = product3.getVersion();

        Long createdId = ordersService.processOrder(createOrderRequestForProduct3()).getId();
        //verify the order order
        Orders createdOrder = ordersService.findById(createdId);
        assertNotNull(createdOrder);
        assertEquals("New", createdOrder.getStatus());
        assertEquals(0L, createdOrder.getVersion());
        assertEquals(1, createdOrder.getRecordStatus());

        // verify the product stock and version
        product3 = productService.findById(PRODUCT3_ID);
        assertEquals(++initalVersion3, product3.getVersion());
        assertEquals(initialStockProduct3 - QTY_3, product3.getUnitInStock());

        //make another request will throw exception
        Exception e = assertThrows(BusinessValidationError.class, () ->
                ordersService.processOrder(createOrderRequestForProduct3()));

        assertTrue(e instanceof BusinessValidationError);

        // verify the product stock and version is not changed
        product3 = productService.findById(PRODUCT3_ID);
        assertEquals(initalVersion3, product3.getVersion());
        assertEquals(0, product3.getUnitInStock());
    }

    private OrderRequest createOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(CUSTOMER_ID);
        orderRequest.setAddressId(ADDRESS_ID);
        orderRequest.setItems(Arrays.asList(
                new OrderItemRequest(PRODUCT1_ID, QTY_1),
                new OrderItemRequest(PRODUCT2_ID, QTY_2)));
        return orderRequest;
    }

    private OrderRequest createOrderRequestForProduct3() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(CUSTOMER_ID);
        orderRequest.setAddressId(ADDRESS_ID);
        orderRequest.setItems(Arrays.asList(
                new OrderItemRequest(PRODUCT3_ID, QTY_3)
        ));
        return orderRequest;
    }
}
