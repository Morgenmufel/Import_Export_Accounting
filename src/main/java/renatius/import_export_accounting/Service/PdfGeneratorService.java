package renatius.import_export_accounting.Service;

import renatius.import_export_accounting.Entity.Request;

public interface PdfGeneratorService {
    public byte[] generateRequestDocument(Request request);
}
