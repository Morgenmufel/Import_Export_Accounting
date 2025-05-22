package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import renatius.import_export_accounting.Entity.Enum.OperationType;
import renatius.import_export_accounting.Entity.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Product p WHERE p.productCode = :productCode AND p.id <> :id")
    boolean existsByProductCodeAndIdNot(@Param("productCode") String productCode, @Param("id") long id);

    boolean existsByNameAndIdNot(String name, long id);

    List<Product> findByOperationType(OperationType operationType);

    List<Product> findAll();


}
