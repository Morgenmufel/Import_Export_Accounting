package renatius.import_export_accounting.Service;

import renatius.import_export_accounting.Entity.Document;

public interface DocumentService {
    Document getDocument(Long documentId);
}
