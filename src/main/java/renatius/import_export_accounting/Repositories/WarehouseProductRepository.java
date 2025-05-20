package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import renatius.import_export_accounting.Entity.WarehouseProduct;
import java.util.List;

@Repository
public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {

    @Query("SELECT wp FROM WarehouseProduct wp JOIN FETCH wp.warehouse WHERE wp.product.id = :productId")
    List<WarehouseProduct> findByProductIdWithWarehouse(@Param("productId") Long productId);

    boolean existsByProductId(Long productId);

}
