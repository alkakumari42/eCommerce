package model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_order")
@IdClass(OrderId.class)
public class Order implements Serializable {
    @Id
    @Column(name = "id")
    private Integer orderId;

    @Id
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "email")
    private String email;
}
