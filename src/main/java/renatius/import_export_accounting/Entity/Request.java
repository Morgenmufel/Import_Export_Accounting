package renatius.import_export_accounting.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import renatius.import_export_accounting.Entity.Enum.OperationType;
import renatius.import_export_accounting.Entity.Enum.RequestStatus;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Partner partner;

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.NEW;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(
            name = "request_products",
            joinColumns = @JoinColumn(name = "request_id")
    )
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Integer> products = new HashMap<>();
    @Enumerated(EnumType.STRING)
    private OperationType type;

    public void addProduct(Product product, int quantity) {
        products.merge(product, quantity, Integer::sum);
    }

    public int getTotalQuantity() {
        return products.values().stream().mapToInt(Integer::intValue).sum();
    }
}
