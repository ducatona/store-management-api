package com.purchase.service.impl;

import com.purchase.exception.ResourceNotFoundException;
import com.purchase.model.dto.request.OrderRequest;
import com.purchase.model.dto.request.UpdateStockRequest;
import com.purchase.model.dto.response.OrderResponse;
import com.purchase.model.dto.response.ProductResponse;
import com.purchase.model.dto.response.ProductTypeResponse;
import com.purchase.model.dto.response.UserResponse;
import com.purchase.repository.IOrderRepository;
import com.purchase.repository.Order;
import com.purchase.service.client.ProductClient;
import com.purchase.service.client.UserClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;



@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {


    @Mock
    private ModelMapper modelMapper;
    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private UserClient userClient;


    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    private OrderResponse firstOrder;
    private OrderResponse secondOrder;
    private Order order;
    private List<OrderResponse> orders;

    @BeforeEach
    void setUp() {
        order = new Order();
        firstOrder = new OrderResponse();
        firstOrder.setOrderId(1l);
        firstOrder.setUserId(1l);
        firstOrder.setProductId(1l);
        firstOrder.setProductTypeId(1l);
        firstOrder.setQuantity(10);

        secondOrder = new OrderResponse();
        secondOrder.setOrderId(2l);
        secondOrder.setUserId(2l);
        secondOrder.setProductId(2l);
        secondOrder.setProductTypeId(3l);
        secondOrder.setQuantity(10);

        orders = new ArrayList<>();
        orders.add(firstOrder);
        orders.add(secondOrder);

    }


    @Test
    void getAllPurchases_returnsAllPurchases() {

        Mockito.when(orderRepository.findAll()).thenReturn(List.of(order));
        Mockito.when(modelMapper.map(order, OrderResponse.class)).thenReturn(firstOrder);

        List<OrderResponse> expectedOrders = orders;

        List<OrderResponse> actualOrders = purchaseService.getAllPurchases();


        assertEquals(expectedOrders.get(0).getOrderId(), actualOrders.get(0).getOrderId());
        assertEquals(expectedOrders.get(0).getProductId(), actualOrders.get(0).getProductId());

    }


    @Test
    void getAllPurchasesByProductId_returnsAllPurchasesByProductId() {
        Long id = 1l;

        Mockito.when(orderRepository.findByProductTypeId(id)).thenReturn(List.of(order));
        Mockito.when(modelMapper.map(order, OrderResponse.class)).thenReturn(firstOrder);

        List<OrderResponse> actual = purchaseService.getAllPurchasesByProductType(id);

        assertEquals(1l, actual.get(0).getOrderId());
        assertEquals(1l, actual.get(0).getProductId());

    }

    @Test
    void getAllPurchasesByUser_returnsAllPurchasesByProductId() {
        Long id = 1l;

        Mockito.when(orderRepository.findByUserId(id)).thenReturn(List.of(order));
        Mockito.when(modelMapper.map(order, OrderResponse.class)).thenReturn(firstOrder);

        List<OrderResponse> actual = purchaseService.getAllPurchasesByUser(id);

        assertEquals(1l, actual.get(0).getOrderId());
        assertEquals(1l, actual.get(0).getProductId());

    }

    @Test
    void getPurchaseById_whenGivenId_returnsPurchaseById() throws ResourceNotFoundException {

        Long id = 1l;

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(modelMapper.map(order, OrderResponse.class)).thenReturn(firstOrder);

        OrderResponse actual = purchaseService.getPurchaseById(id);


        assertEquals(1l, actual.getOrderId());

    }

    @Test
    void getPurchaseById_whenGivenInvalidUserId_throwsNotFoundException() throws ResourceNotFoundException {

        Long id = 10l;
        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> purchaseService.getPurchaseById(id));

        assertEquals("Not order found with id: " + id, exception.getMessage());
    }


    @Test
    void createPurchase_whenGivenCorrectRequest_returnsCreatedPurchase() throws ResourceNotFoundException {

        Long idProduct = 1l;
        Integer quantity = 10;

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(idProduct);
        productResponse.setProductTypeId(idProduct);

        ProductTypeResponse productTypeResponse = new ProductTypeResponse();
        productTypeResponse.setId(idProduct);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(idProduct);


        Mockito.when(productClient.getProductById(idProduct)).thenReturn(productResponse);

        Mockito.when(productClient.getProductTypeByID(idProduct)).thenReturn(productTypeResponse);

        Mockito.when(userClient.getUserById(idProduct)).thenReturn(userResponse);


        Order order = new Order();
        order.setId(idProduct);
        order.setProductId(productResponse.getId());
        order.setProductTypeId(productTypeResponse.getId());
        order.setUserId(userResponse.getId());

        order.setQuantity(quantity);

        UpdateStockRequest updateStockRequest = new UpdateStockRequest(quantity);

        Mockito.doNothing().when(productClient).reduceStock(Mockito.eq(idProduct), Mockito.any(UpdateStockRequest.class));


        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        Mockito.when(modelMapper.map(Mockito.any(Order.class), Mockito.eq(OrderResponse.class))).thenReturn(firstOrder);


        OrderRequest request = new OrderRequest(idProduct, idProduct, idProduct, quantity);
        OrderResponse actualResponse = purchaseService.createPurchase(request);

        assertEquals(firstOrder, actualResponse);

    }

    @Test
    void createPurchase_whenGivenInvalidProduct_throwResourceNotFoundException() throws ResourceNotFoundException {

        Long idProduct = 1l;
        Integer quantity = 10;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(1l);
        orderRequest.setProductTypeId(1l);
        orderRequest.setUserId(1l);
        orderRequest.setQuantity(quantity);


        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(idProduct);
        productResponse.setProductTypeId(99l);


        ProductTypeResponse productTypeResponse = new ProductTypeResponse();
        productTypeResponse.setId(idProduct);


        UserResponse userResponse = new UserResponse();
        userResponse.setId(idProduct);


        Mockito.when(productClient.getProductById(idProduct)).thenReturn(productResponse);

        Mockito.when(productClient.getProductTypeByID(idProduct)).thenReturn(productTypeResponse);

        Mockito.when(userClient.getUserById(idProduct)).thenReturn(userResponse);


        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> purchaseService.createPurchase(orderRequest));

        assertEquals("No product found with that product type", exception.getMessage());

    }

    }






