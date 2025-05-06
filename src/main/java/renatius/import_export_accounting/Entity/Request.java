package renatius.import_export_accounting.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import renatius.import_export_accounting.Entity.Enum.RequestStatus;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Partner partner;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "document_id")
    private Document document;

    private int quantity;

    @OneToMany(mappedBy = "request" , cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Product> product;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.NEW;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
