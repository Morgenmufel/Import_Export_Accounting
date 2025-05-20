package renatius.import_export_accounting.Service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renatius.import_export_accounting.Service.MainService;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MainServiceImpl implements MainService {

}
