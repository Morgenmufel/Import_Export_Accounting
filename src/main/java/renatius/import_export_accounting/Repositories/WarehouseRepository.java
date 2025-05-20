package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import renatius.import_export_accounting.Entity.Warehouse;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByName(String name);

    Optional<Warehouse> findById(long id);

    List<Warehouse> findAll();

    Optional<Warehouse> findByAddress(String address);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END " +
            "FROM Warehouse w WHERE w.name = :name AND w.id <> :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END " +
            "FROM Warehouse w WHERE w.address = :address AND w.id <> :id")
    boolean existsByAddressAndIdNot(@Param("address") String address, @Param("id") Long id);
}
