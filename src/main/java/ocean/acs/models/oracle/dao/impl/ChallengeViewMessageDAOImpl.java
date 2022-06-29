package ocean.acs.models.oracle.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.constant.PortalMessageConstants;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.ChallengeViewMessageDAO;
import ocean.acs.models.data_object.entity.ChallengeViewMessageDO;
import ocean.acs.models.oracle.entity.ChallengeViewMessage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Repository
@AllArgsConstructor
public class ChallengeViewMessageDAOImpl implements ChallengeViewMessageDAO {

    private final ocean.acs.models.oracle.repository.ChallengeViewMessageRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public List<ChallengeViewMessageDO> findAll() throws DatabaseException {
        try {
            List<ChallengeViewMessage> challengeViewMessageList = repo.findAll();
            return challengeViewMessageList.stream().map(ChallengeViewMessageDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<ChallengeViewMessageDO> findById(Long id) {
        return repo.findById(id).map(ChallengeViewMessageDO::valueOf);
    }

    @Override
    public List<ChallengeViewMessageDO> findByIssuerBankId(Long issuerBankId) {
        try {
            List<ChallengeViewMessage> challengeViewMessageList =
                    repo.findByIssuerBankId(issuerBankId);
            return challengeViewMessageList.stream().map(ChallengeViewMessageDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByIssuerBankId] unknown exception", e);
            throw new OceanExceptionForPortal(ResultStatus.SERVER_ERROR,
                    PortalMessageConstants.DB_READ_ERROR);
        }
    }

    @Override
    public List<ChallengeViewMessageDO> findByIssuerBankIdAndLanguageCode(
            Long issuerBankId, String languageCode) {
        try {
            List<ChallengeViewMessage> challengeViewMessageList =
                    repo.findByIssuerBankIdAndLanguageCodeAndDeleteFlagFalse(issuerBankId, languageCode);

            return challengeViewMessageList.stream()
                    .map(ChallengeViewMessageDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new OceanExceptionForPortal(
                    ResultStatus.SERVER_ERROR, PortalMessageConstants.DB_READ_ERROR);
        }
    }

    @Override
    public ChallengeViewMessageDO saveOrUpdate(ChallengeViewMessageDO challengeViewMessagesDO)
            throws DatabaseException {
        try {
            ChallengeViewMessage challengeViewMessage =
                    ChallengeViewMessage.valueOf(challengeViewMessagesDO);
            return ChallengeViewMessageDO.valueOf(repo.save(challengeViewMessage));
        } catch (Exception e) {
            log.error("[saveOrUpdate] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(ChallengeViewMessage.class, issuerBankId, deleter, deleteMillis);
    }

}
