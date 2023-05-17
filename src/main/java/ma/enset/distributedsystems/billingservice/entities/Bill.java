package ma.enset.distributedsystems.billingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.distributedsystems.billingservice.model.Customer;

import java.util.Collection;
import java.util.Date;

@Entity
@Data @AllArgsConstructor
@NoArgsConstructor @Builder @ToString
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date billingDate;
    @OneToMany(mappedBy = "bill")
    private Collection<ProductItem> productItems;
    private Long customerId;
    @Transient
    private Customer customer;
}
