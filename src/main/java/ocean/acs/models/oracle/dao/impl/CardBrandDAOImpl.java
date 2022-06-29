package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.CardBrandDAO;
import ocean.acs.models.data_object.entity.CardBrandDO;
import ocean.acs.models.data_object.portal.CardBrandSigningCertificateConfigDO;
import ocean.acs.models.oracle.entity.CardBrand;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class CardBrandDAOImpl implements CardBrandDAO {
    private final EntityManager entityManager;
    private final ocean.acs.models.oracle.repository.CardBrandRepository cardBrandRepository;

    @Override
    public List<CardBrandDO> findCardBrandList() {
        try {
            List<CardBrand> cardBrandList =
                    cardBrandRepository.findAllByDeleteFlagFalseOrderByDisplayOrderAsc();

            return cardBrandList.stream().map(CardBrandDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findCardBrandList] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<CardBrandDO> findByName(String name) {
        try {
            return cardBrandRepository.findByNameAndDeleteFlagFalse(name).map(CardBrandDO::valueOf);
        } catch (Exception e) {
            log.error("[findByName] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public List<CardBrandSigningCertificateConfigDO> findCardBrandSigningCertificateConfig() {
        try {
            String sql =
                    "select cardBrand.name, systemSetting.value, systemSetting2.value\n"
                            + "from CardBrand cardBrand\n"
                            + "         left join SystemSetting systemSetting on cardBrand.name = systemSetting.category and\n"
                            + "                                                  systemSetting.className = 'useGlobalSigningCertificate' and\n"
                            + "                                                  systemSetting.deleteFlag = false\n"
                            + "         left join SystemSetting systemSetting2 on cardBrand.name = systemSetting2.category and\n"
                            + "                                                  systemSetting2.className = 'useGlobalSigningCertificate3ds1' and\n"
                            + "                                                  systemSetting2.deleteFlag = false\n"
                            + "where cardBrand.deleteFlag = false\n"
                            + "order by cardBrand.displayOrder asc";
            Query query = entityManager.createQuery(sql);
            List<Object[]> resultList = query.getResultList();
            return resultList.stream()
                    .map(
                            result -> {
                                String cardBrand = (String) result[0];
                                boolean useGlobalSigningCertificate;
                                if (result[1] == null) {
                                    useGlobalSigningCertificate = false;
                                } else {
                                    useGlobalSigningCertificate = result[1].equals("1");
                                }
                                boolean useGlobalSigningCertificate3ds1;
                                if (result[2] == null) {
                                    useGlobalSigningCertificate3ds1 = false;
                                } else {
                                    useGlobalSigningCertificate3ds1 = result[2].equals("1");
                                }
                                return new CardBrandSigningCertificateConfigDO(
                                        cardBrand, useGlobalSigningCertificate, useGlobalSigningCertificate3ds1);
                            })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("[findCardBrandSigningCertificateConfig] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }
}
