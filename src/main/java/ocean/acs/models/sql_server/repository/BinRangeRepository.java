package ocean.acs.models.sql_server.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.BinRange;

@Repository
public interface BinRangeRepository
        extends CrudRepository<BinRange, Long>, JpaSpecificationExecutor<BinRange> {

    @Query("select b from BinRange b where b.issuerBankId = :issuerBankId and :cardNumber between startBin and endBin and deleteFlag=false")
    List<BinRange> findByStartBinBetweenEndBin(@Param("issuerBankId") Long issuerBankId,
            @Param("cardNumber") BigInteger cardNumber);

    @Query("select b.issuerBankId from BinRange b where :cardNumber between startBin and endBin and deleteFlag=false")
    List<Long> findByCardNumberBetweenStartBinAndEndBin(@Param("cardNumber") BigInteger cardNumber);

    Page<BinRange> findAll(Pageable pageable);

    Optional<BinRange> findByIdAndIssuerBankIdAndDeleteFlagIsFalse(Long id, Long issuerBankId);

    @Query("select b from BinRange b where ((:startBin between b.startBin and b.endBin) "
            + "    or (:endBin between b.startBin and b.endBin) "
            + "    or (b.startBin between :startBin and :endBin) "
            + "    or (b.endBin between :startBin and :endBin)) " + "    and b.deleteFlag = false ")
    List<BinRange> existingBinRangeConflict(@Param("startBin") BigInteger startBin,
            @Param("endBin") BigInteger endBin);

    List<BinRange> findByIssuerBankIdAndDeleteFlagFalse(Long issuerBankId);

    Boolean existsByIssuerBankIdAndDeleteFlagFalse(Long issuerBankId);

    @Query("select case when count(b) > 0 then true else false end from BinRange b "
            + "where b.issuerBankId = :issuerBankId and :cardNumber between startBin and endBin and deleteFlag = false")
    boolean existsByIssuerBankIdAndCardNumberInBinRange(@Param("issuerBankId") Long issuerBankId,
            @Param("cardNumber") BigInteger cardNumber);

}
