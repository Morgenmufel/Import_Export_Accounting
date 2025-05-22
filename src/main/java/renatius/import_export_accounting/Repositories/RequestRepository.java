package renatius.import_export_accounting.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import renatius.import_export_accounting.Entity.Enum.RequestStatus;
import renatius.import_export_accounting.Entity.Request;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByPartnerId(Long partnerId);

    List<Request> findByPartnerId(Long partnerId);

    //List<Request> findByStatus(RequestStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"products", "partner"})
    Page<Request> findByStatus(RequestStatus status, Pageable pageable);

    @Query("SELECT r FROM Request r LEFT JOIN FETCH r.products WHERE r.id = :id")
    Optional<Request> findWithProductsById(@Param("id") Long id);
}
