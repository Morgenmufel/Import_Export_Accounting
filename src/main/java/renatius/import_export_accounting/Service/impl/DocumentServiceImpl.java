package renatius.import_export_accounting.Service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renatius.import_export_accounting.Entity.Document;
import renatius.import_export_accounting.Repositories.DocumentRepository;
import renatius.import_export_accounting.Service.DocumentService;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public Document getDocument(Long documentId) {
        return documentRepository.findById(documentId).get();
    }
}
