package model;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "available_quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;
}
