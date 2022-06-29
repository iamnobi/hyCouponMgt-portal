package com.cherri.acs_portal.service;

import com.cherri.acs_portal.dto.report.AbnormalTransactionDTO;
import com.cherri.acs_portal.dto.report.AbnormalTransactionMonthlyDTO;
import com.cherri.acs_portal.dto.report.QueryTimeRange;
import com.cherri.acs_portal.manager.ReportManager;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.AbnormalTransactionDAO;
import ocean.acs.models.dao.AuthenticationLogDAO;
import ocean.acs.models.data_object.entity.AbnormalTransactionDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AbnormalTransactionJobService {

    static final List<String> N_RATE_MOLECULE_TRANS_STATUS_LIST = Arrays.asList("N", "R");
    static final List<String> U_RATE_MOLECULE = Collections.singletonList("U");

    private final ReportManager reportManager;
    private final AbnormalTransactionDAO abnormalTransactionDao;
    private final AuthenticationLogDAO authenticationLogDao;

    @Autowired
    public AbnormalTransactionJobService(
      ReportManager reportManager,
      AbnormalTransactionDAO abnormalTransactionDao,
      AuthenticationLogDAO authenticationLogDao) {
        this.reportManager = reportManager;
        this.abnormalTransactionDao = abnormalTransactionDao;
        this.authenticationLogDao = authenticationLogDao;
    }

    public boolean statisticsAbnormalTransactionMonthly(int year, int month, String operator) {
      List<AbnormalTransactionMonthlyDTO> abnormalTransactionMonthlyDTOList = reportManager
          .statisticsAbnormalTransactionRateMonthly(year, month);

      List<AbnormalTransactionDO> entityList = abnormalTransactionMonthlyDTOList.stream()
          .map(dto -> {
            AbnormalTransactionDO entity =
                abnormalTransactionDao.findByYearAndMonthAndDayAndIssuerBankIdAndMerchantId(
                    year, month, 0, dto.getIssuerBankId(), dto.getMerchantId()).orElse(null);
            if (entity == null) {
              // insert
              return AbnormalTransactionDO.builder()
                  .issuerBankId(dto.getIssuerBankId())
                  .year(year)
                  .month(month)
                  .dayOfMonth(0)
                  .merchantId(dto.getMerchantId())
                  .merchantName(dto.getMerchantName())
                  .nRate((double) dto.getNCount() / dto.getTotalCount() * 100)
                  .uRate((double) dto.getUCount() / dto.getTotalCount() * 100)
                  .nCount(dto.getNCount())
                  .uCount(dto.getUCount())
                  .totalCount(dto.getTotalCount())
                  .sysCreator(operator)
                  .createMillis(System.currentTimeMillis())
                  .build();
            } else {
              // update
              entity.setNRate((double) dto.getNCount() / dto.getTotalCount() * 100);
              entity.setURate((double) dto.getUCount() / dto.getTotalCount() * 100);
              entity.setNCount(dto.getNCount());
              entity.setUCount(dto.getUCount());
              entity.setTotalCount(dto.getTotalCount());
              entity.setSysUpdater(operator);
              entity.setUpdateMillis(System.currentTimeMillis());
              return entity;
            }
          }).collect(Collectors.toList());
      try {
        abnormalTransactionDao.saveAll(entityList);
        return true;
      } catch (Exception e) {
        log.error("[statisticsAbnormalTransactionMonthly] unknown exception", e);
        return false;
      }
    }

    public boolean statisticsAbnormalTransaction(final QueryTimeRange queryTimeRange,
      String operator) {
        // Statistics N-Rate and U-Rate
        // total 交易數
        Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> allTransactionMap = getRateMap(
          null, queryTimeRange);
        // N & R 交易數
        Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> nRateMap = getRateMap(
          N_RATE_MOLECULE_TRANS_STATUS_LIST, queryTimeRange);
        // U 交易數
        Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> uRateMap = getRateMap(
          U_RATE_MOLECULE,
          queryTimeRange);

        List<AbnormalTransactionDO> entityList = createOrUpdateAbnormalTransaction(
            allTransactionMap, uRateMap, nRateMap, queryTimeRange, operator);

        try {
            abnormalTransactionDao.saveAll(entityList);
            return true;
        } catch (Exception e) {
            log.error("[statisticsAbnormalTransaction] unknown exception", e);
            return false;
        }
    }

    private Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> getRateMap(
      List<String> transStatusList, QueryTimeRange queryTimeRange) {
        List<AbnormalTransactionDTO> rateList =
          reportManager.statisticsAbnormalTransactionRate(
            transStatusList,
            queryTimeRange.getStartMillis(),
            queryTimeRange.getEndMillis());

        log.debug("[getRateMap] rateList={}", rateList);
        return convertToMap(rateList);
    }

    private void setMerchantName(Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> map) {
      map.forEach((key, e) -> {
        String merchantName = authenticationLogDao
            .getLatestMerchantName(e.getKey().getIssuerBankId(), e.getKey().getMerchantId());
        e.setMerchantName(merchantName);
      });
    }

    private Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> convertToMap(
      List<AbnormalTransactionDTO> list) {
        return list.stream()
          .collect(Collectors.toMap(AbnormalTransactionDTO::getKey, Function.identity()));
    }

    private List<AbnormalTransactionDO> createOrUpdateAbnormalTransaction(
        Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> allTransactionMap,
        Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> uRateMap,
        Map<AbnormalTransactionDTO.Key, AbnormalTransactionDTO> nRateMap,
        QueryTimeRange queryTimeRange,
        String operator) {

      log.debug("[createOrUpdateAbnormalTransaction],\n" +
          " keySet={},\n" +
          " uRateMap={}\n" +
          " nRateMap={}\n", allTransactionMap, uRateMap, nRateMap);

      int year = queryTimeRange.getStartZonedDateTime().getYear();
      int month = queryTimeRange.getStartZonedDateTime().getMonthValue();
      int dayOfMonth = queryTimeRange.getStartZonedDateTime().getDayOfMonth();
      long nowMillis = queryTimeRange.getNowMillis();

      setMerchantName(allTransactionMap);

      return allTransactionMap.keySet().stream()
          .map(key -> {
            String merchantName = allTransactionMap.get(key).getMerchantName();
            long total = allTransactionMap.get(key).getCount();
            long nCount = 0;
            long uCount = 0;
            double nRate = 0.0;
            double uRate = 0.0;

            if (nRateMap.get(key) != null) {
              nCount = nRateMap.get(key).getCount();
              nRate = parseDouble((double) nCount / total * 100);
            }
            if (uRateMap.get(key) != null) {
              uCount = uRateMap.get(key).getCount();
              uRate = parseDouble((double) uCount / total * 100);
            }

            Optional<AbnormalTransactionDO> entityOpt =
                abnormalTransactionDao.findByYearAndMonthAndDayAndIssuerBankIdAndMerchantId(
                    year, month, dayOfMonth, key.getIssuerBankId(), key.getMerchantId());

            double finalURate = uRate;
            double finalNRate = nRate;
            long finalUCount = uCount;
            long finalNCount = nCount;
            return entityOpt.map(entity -> {
                  entity.setURate(finalURate);
                  entity.setNRate(finalNRate);
                  entity.setUCount(finalUCount);
                  entity.setNCount(finalNCount);
                  entity.setTotalCount(total);
                  entity.setUpdateMillis(nowMillis);
                  entity.setSysUpdater(operator);
                  return entity;
                }
            ).orElse(AbnormalTransactionDO.builder()
                .issuerBankId(key.getIssuerBankId())
                .year(year).month(month).dayOfMonth(dayOfMonth)
                .merchantId(key.getMerchantId()).merchantName(merchantName)
                .nRate(nRate).uRate(uRate)
                .nCount(nCount).uCount(uCount).totalCount(total)
                .sysCreator(operator).createMillis(nowMillis).build());
          }).collect(Collectors.toList());
    }

  private Double parseDouble(Double rate) {
    DecimalFormat decimalFormat = new DecimalFormat("#.###");
    if (!rate.isNaN()) {
      return Double.parseDouble(decimalFormat.format(rate));
    } else {
      return 0.0;
    }
  }

}
