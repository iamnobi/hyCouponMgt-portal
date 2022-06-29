package ocean.acs.models.oracle.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import org.springframework.stereotype.Component;

@Component
public class DeleteIssuerBankRelativeDataHelper {

    private final EntityManager entityManager;

    public DeleteIssuerBankRelativeDataHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T extends IssuerBankRelativeData> int deleteByIssuerBankId(Class<T> entityClass, long issuerBankId, String deleter, long deleteMillis) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<T> update = cb.createCriteriaUpdate(entityClass);
        Root<T> root = update.from(entityClass);
        update.set(IssuerBankRelativeData.COL_DELETE_FLAG, true);
        update.set(IssuerBankRelativeData.COL_DELETER, deleter);
        update.set(IssuerBankRelativeData.COL_DELETE_MILLIS, deleteMillis);
        update.where(
                cb.equal(root.get(IssuerBankRelativeData.COL_ISSUER_BANK_ID), issuerBankId),
                cb.equal(root.get(IssuerBankRelativeData.COL_DELETE_FLAG1), false));

        return entityManager.createQuery(update).executeUpdate();
    }
}
