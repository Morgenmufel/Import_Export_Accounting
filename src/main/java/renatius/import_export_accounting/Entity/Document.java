package renatius.import_export_accounting.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Document {
    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "BYTEA")
    private byte[] fileAsArrayOfBytes;

    private String filePath;

    private String docType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private Partner partner;

    @OneToOne(mappedBy = "document")
    private Request request;

}
