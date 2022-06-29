package com.cherri.acs_portal.service;

import com.cherri.acs_portal.config.FiscProperties;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.SystemConstants;
import com.cherri.acs_portal.dto.bank.CardBrandTypeDto;
import com.cherri.acs_portal.manager.BankHandlingFeeManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import ocean.acs.models.data_object.entity.IssuerHandingFeeDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BankHandlingFeeJobService {

    private final BankHandlingFeeManager bankHandlingFeeManager;
    private final IssuerBankDAO issuerBankDao;
    private final FiscProperties fiscProperties;

    @Autowired
    public BankHandlingFeeJobService(
      BankHandlingFeeManager bankHandlingFeeManager,
      IssuerBankDAO issuerBankDao,
      FiscProperties fiscProperties) {
        this.bankHandlingFeeManager = bankHandlingFeeManager;
        this.issuerBankDao = issuerBankDao;
        this.fiscProperties = fiscProperties;
    }

    public boolean exportAndUploadBankHandlingFee(String operator) {
        LocalDateTime now = LocalDateTime.now(EnvironmentConstants.ACS_TIMEZONE_ID);
        log.debug("[exportAndUploadBankHandlingFee] execute time={}, operator={}", now, operator);

        try {
            List<IssuerBankDO> issuerBankList = issuerBankDao.findAll();

            List<CardBrandTypeDto> cardBrandTypeDtoList = bankHandlingFeeManager
              .getCardBrandTypeList();
            Map<Long, IssuerHandingFeeDO> issuerHandingFeeMap =
              bankHandlingFeeManager.getIssuerHandingFeeMap(issuerBankList);
            Map<String, Long> totalCardsMap =
              bankHandlingFeeManager.getIssuerTotalCardsMap(issuerBankList, cardBrandTypeDtoList);

            log.debug(
              "[exportAndUploadBankHandlingFee] cardBrand list size={}, issuerHandlingFee size={}, totalCards size={}",
              cardBrandTypeDtoList.size(),
              issuerHandingFeeMap.size(),
              totalCardsMap.size());

            // 手續費資料內容
            String fileContent =
              bankHandlingFeeManager.createHandlingFeeContent(
                issuerBankList, cardBrandTypeDtoList, issuerHandingFeeMap, totalCardsMap);

            // 匯出檔案
            exportBankHandlingFee(fileContent);

            // 執行送檔的指令
            executeUploadFileCmd();

            return true;
        } catch (Exception e) {
            log
              .error("[exportBankHandlingFee] unknown exception, execute time={}, operator={}", now,
                operator, e);
        }
        return false;
    }

    private void executeUploadFileCmd() throws IOException {
        final String[] uploadCmdAry = fiscProperties.getBankHandlingFee().getUploadCmd().split(" ");

        // Verify
        final Path uploadCmdPath = Paths.get(uploadCmdAry[0]);
        verifyPathExist(uploadCmdPath);
        verifyFileIsReadable(uploadCmdPath);

        /* Fortify: Command Injection

        // Execute upload file command
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(uploadCmdAry);
        Process process = processBuilder.start();

        // Print execute info
        printExecuteInfo(process);
        */
    }

    private void exportBankHandlingFee(String fileContent) throws IOException {
        final Path exportDirPath = Paths
          .get(fiscProperties.getBankHandlingFee().getExportDirPath());

        // Verify
        try {
            verifyPathExist(exportDirPath);
        } catch (FileNotFoundException e) {
            mkdir(exportDirPath);
        }
        verifyIsDir(exportDirPath);
        verifyFileIsReadable(exportDirPath);
        verifyFileIsWritable(exportDirPath);

        // Export file
        Path bankHandlingFeeFilePath =
          Paths.get(exportDirPath.toString() + File.separator + getFileName());
        log.debug("[exportBankHandlingFee] bankHandlingFeeFilePath={}", bankHandlingFeeFilePath);
        Files.write(bankHandlingFeeFilePath, fileContent.getBytes());
    }

    private void mkdir(Path exportDirPath) {
        boolean mkdirResult = exportDirPath.toFile().mkdir();
        log.debug(
          "[mkdir] Directory path={}, create status={}",
          exportDirPath.toString(),
          mkdirResult ? "success" : "fail");
    }

    private void verifyPathExist(Path exportDirPath) throws FileNotFoundException {
        // 路徑是否存在
        if (Files.notExists(exportDirPath)) {
            doErrorLog(exportDirPath, "not found");
            throw new FileNotFoundException(
              String.format("Export-path:[%s] not found.", exportDirPath));
        }
    }

    private void verifyIsDir(Path exportDirPath) throws IOException {
        // 是否為檔案目錄
        if (!Files.isDirectory(exportDirPath)) {
            doErrorLog(exportDirPath, "is not directory");
            throw new NotDirectoryException(
              String.format(
                "[exportBankHandlingFee] export-path:[%s] is not directory.", exportDirPath));
        }
    }

    private void verifyFileIsReadable(Path exportDirPath) throws FileSystemException {
        if (!Files.isReadable(exportDirPath)) {
            doErrorLog(exportDirPath, "not readable");
            throw new FileSystemException(
              String
                .format("[exportBankHandlingFee] export-path:[%s] not readable.", exportDirPath));
        }
    }

    private void verifyFileIsWritable(Path exportDirPath) throws FileSystemException {
        if (!Files.isWritable(exportDirPath)) {
            doErrorLog(exportDirPath, "not writable");
            throw new FileSystemException(
              String
                .format("[exportBankHandlingFee] export-path:[%s] not writable.", exportDirPath));
        }
    }

    private void doErrorLog(Path exportDirPath, String errDesc) {
        log.error(
          "[doErrorLog] export-path={}, error description={}.", exportDirPath, errDesc);
    }

    private String getFileName() {
        return LocalDate.now().format(SystemConstants.DATE_TIME_FORMATTER_YYYYMMDD) + "01.txt";
    }

    private void printExecuteInfo(Process process) {
        StringBuilder executeInfo = new StringBuilder();
        try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                executeInfo.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                log.info("[printExecuteInfo] Successful execution of upload bank fees.");
            } else {
                log.error(
                  "[printExecuteInfo] Error execution of upload bank fees, exit value={}", exitVal);
            }

            log.debug("[printExecuteInfo] execute info={}", executeInfo.toString());
        } catch (Exception e) {
            log.error("[printExecuteInfo] unknown exception", e);
        }
    }
}
