package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import renatius.import_export_accounting.Entity.WarehouseProduct;
import java.util.List;

@Repository
public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {

    @Query("SELECT wp FROM WarehouseProduct wp JOIN FETCH wp.warehouse WHERE wp.product.id = :productId")
    List<WarehouseProduct> findByProductIdWithWarehouse(@Param("productId") Long productId);

    boolean existsByProductId(Long productId);

    WarehouseProduct findByProductId(Long productId);

    List<WarehouseProduct> findWarehouseProductByWarehouseId(Long warehouseId);

    @Query("SELECT CASE WHEN COUNT(wp) > 0 THEN true ELSE false END " +
            "FROM WarehouseProduct wp WHERE wp.product.name = :name AND wp.product.id <> :id AND wp.warehouse.id = :warehouseId")
    boolean existsWarehouseProductByNameAndNotId(@Param("name") String productName, @Param("id") Long productId, @Param("warehouseId") Long warehouseId);

    @Query("SELECT CASE WHEN COUNT(wp) > 0 THEN true ELSE false END " +
            "FROM WarehouseProduct wp WHERE wp.product.productCode = :code AND wp.product.id <> :productId AND wp.warehouse.id = :warehouseId")
    boolean existsWarehouseProductByProductCodeAndNotId(@Param("code") String productCode, @Param("productId") long productId, @Param("warehouseId") Long warehouseId);

    boolean existsByProductProductCodeAndWarehouseId(String productCode, long warehouseId);

    boolean existsByProductNameAndWarehouseId(String productName, long warehouseId);

    @Transactional
    @Modifying
    @Query("DELETE FROM WarehouseProduct wp WHERE wp.id.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
