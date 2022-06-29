package ocean.acs.models.sql_server.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.dao.CardBrandLogoDAO;
import ocean.acs.models.data_object.entity.CardBrandLogoDO;
import ocean.acs.models.sql_server.entity.CardBrandLogo;

@Log4j2
@Repository
@AllArgsConstructor
public class CardBrandLogoDAOImpl implements CardBrandLogoDAO {

    private final ocean.acs.models.sql_server.repository.CardBrandLogoRepository repo;

    @Override
    public List<CardBrandLogoDO> findAll(int version) throws DatabaseException {
        try {
            List<CardBrandLogo> cardBrandLogoList = repo.findAllByThreeDSVersionAndDeleteFlag(version, false);
            return cardBrandLogoList.stream().map(CardBrandLogoDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAll] unknown exception", e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public CardBrandLogoDO save(CardBrandLogoDO cardBrandLogoDO) {
        CardBrandLogo cardBrandLogo = CardBrandLogo.valueOf(cardBrandLogoDO);
        return CardBrandLogoDO.valueOf(repo.save(cardBrandLogo));
    }

    @Override
    public Optional<CardBrandLogoDO> findByCardBrandAndNotDelete(int version, String cardBrand) {
        Optional<CardBrandLogo> cardBrandLogoOpt =
                repo.findByCardBrandAndNotDelete(version, cardBrand);
        return cardBrandLogoOpt.map(CardBrandLogoDO::valueOf);
    }

    @Override
    public Optional<Long> findIdByCardBrandAndNotDelete(int version, String cardBrand) {
        return repo.findIdByCardBrandAndNotDelete(version, cardBrand);
    }

    @Override
    public Optional<CardBrandLogoDO> findById(Long id) {
        Optional<CardBrandLogo> cardBrandLogoOpt = repo.findById(id);
        return cardBrandLogoOpt.map(CardBrandLogoDO::valueOf);
    }

}
