package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;

public interface AuditServiceWrapper<T extends AuditableDTO> {

    Optional<T> add(T draftData) throws DatabaseException;

    boolean delete(DeleteDataDTO draftData);

    Optional<T> update(T draftData) throws DatabaseException;

    Optional<T> convertJsonToConcreteDTO(String draftInJson) throws IOException;

    // 取得原始資料物件
    Optional<T> getOriginalDataDTO(AuditableDTO draftData) throws DatabaseException;

    // 取得原始資料附加檔案
    Optional<AuditFileDTO> getOriginalFileDTO(T draftData);

    // 取得待審核資料中附加檔案
    Optional<AuditFileDTO> getDraftFileDTO(T draftData);

    // 過濾掉系統參數，讓審核的時候可以閱讀物件基本資訊。
    void filterForAuditUsed(T originalData);

    boolean isAuditingLockAvailable(AuditableDTO draftData) throws DatabaseException;

    boolean lockDataAsAuditing(AuditableDTO draftData) throws DatabaseException;

    boolean unlockDataFromAuditing(AuditableDTO draftData) throws DatabaseException;

    default OceanException getMarkAuditingException(
      String markType, String tableName, AuditableDTO draftData) {
        return new OceanException(
          String.format(
            "Failed in %s data as auditing in update %s with Id: %d, issuerBankId: %d",
            markType, tableName, draftData.getId(), draftData.getIssuerBankId()));
    }
}
