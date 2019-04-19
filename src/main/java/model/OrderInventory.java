package model;

import lombok.Builder;
import java.util.Map;

@Builder
public class OrderInventory {

    private Map<Integer, Integer> itemToQuantityMap;
    private String email;

}
