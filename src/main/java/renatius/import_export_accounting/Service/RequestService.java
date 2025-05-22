package renatius.import_export_accounting.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import renatius.import_export_accounting.Entity.Enum.RequestStatus;
import renatius.import_export_accounting.Entity.Product;
import renatius.import_export_accounting.Entity.Request;

import java.util.List;

public interface RequestService {

    public Request createRequest(Long partnerId, List<Long> productIds);
    public List<Product> getAllProducts();

    void saveRequest(Request request);

    List<Request> getRequestsByPartnerId(Long partnerId);

    Page<Request> findByStatus(RequestStatus requestStatus, Pageable pageable);

    void approveRequest(Long requestId);

    void rejectRequest(Long requestId);
}
