package com.cherri.acs_portal.controller.validator;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.util.AcsPortalUtil;
import com.cherri.acs_portal.util.CsvUtil;
import com.cherri.acs_portal.util.FileUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.lang3.StringUtils;

public class BatchImportFileValidator {

    public static void valid(
      final byte[] batchImportFileContent)
      throws OceanException {

        if (null == batchImportFileContent || 0 == batchImportFileContent.length) {
            throw new OceanException(ResultStatus.COLUMN_NOT_EMPTY,
              "Missing argument : panListFile");
        }

        // 檢查批次檔案大小 - Max 2MB
        int batchImportFileMaxSize = 2000000;
        if (batchImportFileMaxSize < batchImportFileContent.length) {
            throw new OceanException(
              ResultStatus.ILLEGAL_FILE_SIZE, "The batch file is too large, max size:2MB");
        }

        List<String> cardList = CsvUtil.simpleCSVReader(batchImportFileContent);
        // 檢查檔案是否為空
        if (cardList.isEmpty()) {
            throw new OceanException(ResultStatus.COLUMN_NOT_EMPTY, "The batch file is empty");
        }
        // 檢查批次檔案比數 - Max 1萬筆
        int batchImportFileMaxRows = 10000;
        if (batchImportFileMaxRows < cardList.size()) {
            throw new OceanException(
              ResultStatus.ITEMS_EXCEEDS_UPPER_LIMIT,
              "Card items exceeds the upper limit:" + batchImportFileMaxRows + " records");
        }
        // 過濾掉重複卡號
        Set<String> cardSet =
          cardList.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        // 檢查是否有不合法的卡號
        String invalidCardsText =
          cardSet.stream()
            .filter(cardNumber -> !AcsPortalUtil.isValidCardNumber(cardNumber))
            .collect(Collectors.joining(", "));
        if (!invalidCardsText.isEmpty()) {
            throw new OceanException(
              ResultStatus.ILLEGAL_CARD,
              "The batch file contains invalid card:" + invalidCardsText);
        }
    }
}
