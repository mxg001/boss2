package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BankImport;
import cn.eeepay.framework.model.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BankImportService {

    List<BankImport> selectList(BankImport baseInfo, Page<BankImport> page);

    Result importData(MultipartFile file, Long id, String bonusType) throws IOException;

    Result matchData(Integer id);
}
