package controller;

import model.Item;
import model.Order;
import model.OrderInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repository.ItemRepository;
import repository.OrderRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path ="/order")
public class OrderController {

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private ItemRepository itemRepository;

        @Autowired
        private AtomicInteger atomicInteger;

        @PostMapping(path = "/singleOrder")
        private @ResponseBody
        Integer singleOrder(@RequestBody final Order order){
            Integer orderId = atomicInteger.getAndIncrement()+1;
            order.setOrderId(orderId);
            final Optional<Item> result= itemRepository.findById(order.getItemId());
            if(result.isPresent()) {
                final Integer availableQuantity = result.get().getQuantity();
                if(order.getQuantity()<=availableQuantity) {
                    final Item item = result.get();
                    item.setQuantity(availableQuantity - order.getQuantity());
                    orderRepository.save(order);
                    itemRepository.save(item); // updating item quantity
                } else {
                    throw new IllegalArgumentException(String.format("Quantity Not Available For itemId : %s", order.getItemId()));
                }

            }
            orderRepository.save(order);
            return orderId;
        }

    @PostMapping(path = "/bundleOrder")
    private @ResponseBody
    Integer bundleOrder(@RequestBody final Map<Integer,Integer> itemToQuantityMap,
                    @RequestBody final String email){
        final Integer orderId = atomicInteger.getAndIncrement();
        final List<Order> orders = itemToQuantityMap.entrySet()
                .stream()
                .map(order ->
                        Order.builder()
                                .itemId(order.getKey())
                                .quantity(order.getValue())
                                .orderId(orderId)
                                .email(email)
                                .build())
                .collect(Collectors.toList());


        for(Order order: orders){
            Optional<Item> result= itemRepository.findById(order.getItemId());
            if(result.isPresent()) {
                final Integer quantity = result.get().getQuantity();
                if(order.getQuantity()<=quantity) {
                    Item item = result.get();
                    item.setQuantity(quantity - order.getQuantity());
                    orderRepository.save(order);
                    itemRepository.save(item); //updating item quantity
                } else {
                    throw new IllegalArgumentException(String.format("Quantity Not Available For itemId : %s", order.getItemId()));
                }

            }
        }
        return orderId;
    }

    @GetMapping("/getAll")
    private Map<Integer,List<OrderInventory>> getAllOrders(){
            Map<Integer,List<OrderInventory>> orderIdInventoriesList = new HashMap<>();
            List<Order> orders = new ArrayList<>();
            orderRepository.findAll().forEach(orders::add);
            for(Order order: orders) {
                orderIdInventoriesList.putIfAbsent(order.getOrderId(), new ArrayList<>());
                Map<Integer,Integer> itemQuantityMap = new HashMap<>();
                itemQuantityMap.put(order.getItemId(),order.getQuantity());
                OrderInventory orderInventory = OrderInventory.builder()
                                                              .email(order.getEmail())
                                                              .itemToQuantityMap(itemQuantityMap)
                                                              .build();
                orderIdInventoriesList.get(order.getOrderId()).add(orderInventory);
            }
            return orderIdInventoriesList;
    }
}


