package renatius.import_export_accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class ImportExportAccountingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImportExportAccountingApplication.class, args);
    }

}
