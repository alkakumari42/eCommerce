package controller;

import model.Item;
import model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repository.ItemRepository;
import repository.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        @GetMapping(path = "/singleOrder")
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

    @GetMapping(path = "/bundleOrder")
    private @ResponseBody
    Integer bundleOrder(@RequestParam final Map<Integer,Integer> itemToQuantityMap,
                    @RequestParam final String email){
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
}


