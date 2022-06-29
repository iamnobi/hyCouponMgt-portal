package ocean.acs.models.oracle.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.utils.IpUtils;
import ocean.acs.models.dao.BlackListIpGroupDAO;
import ocean.acs.models.data_object.entity.BlackListIpGroupDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.PortalBlackListIpGroupDO;
import ocean.acs.models.oracle.entity.BlackListIpGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class BlackListIpGroupDAOImpl implements BlackListIpGroupDAO {

    private final ocean.acs.models.oracle.repository.BlackListIpGroupRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public Optional<BlackListIpGroupDO> findById(Long id) {
        return repo.findById(id).map(BlackListIpGroupDO::valueOf);
    }

    @Override
    public void deleteByIds(DeleteDataDO deleteDto) {
        long now = System.currentTimeMillis();
        repo.deleteByIds(deleteDto.getId(), deleteDto.getUser(), now);
    }

    @Override
    public BlackListIpGroupDO create(PortalBlackListIpGroupDO createDto, TransStatus transStatus,
            String creator) {
        BlackListIpGroup ipGroup = new BlackListIpGroup();
        ipGroup.setIssuerBankId(createDto.getIssuerBankId());
        ipGroup.setOriginVersion(createDto.getOriginVersion());
        if (4 == createDto.getOriginVersion()) { // 轉IPv6
            String ipV6 = IpUtils.ip4To6(createDto.getIp());
            log.debug("[create] IPv4={} to IPv6={}",
                StringUtils.normalizeSpace(createDto.getIp()),
                StringUtils.normalizeSpace(ipV6)
            );
            ipGroup.setIp(ipV6);

            if (null == createDto.getCidr()) {
                int defaultIpv6CidrMax = 128;
                ipGroup.setCidr(defaultIpv6CidrMax);
            } else {
                ipGroup.setCidr(cidr4to6(createDto.getCidr()));
            }
        } else {
            ipGroup.setIp(createDto.getIp());
            ipGroup.setCidr(createDto.getCidr());
        }
        ipGroup.setTransStatus(transStatus.getCode());
        ipGroup.setAuditStatus(createDto.getAuditStatus().getSymbol());
        ipGroup.setCreator(creator);
        ipGroup.setCreateMillis(System.currentTimeMillis());
        ipGroup = repo.save(ipGroup);
        return BlackListIpGroupDO.valueOf(ipGroup);
    }

    @Override
    public BlackListIpGroupDO update(BlackListIpGroupDO blackListIpGroupDO) {
        BlackListIpGroup blackListIpGroup = BlackListIpGroup.valueOf(blackListIpGroupDO);
        return BlackListIpGroupDO.valueOf(repo.save(blackListIpGroup));
    }

    @Override
    public void updateTransStatusByIssuerBankId(Long issuerBankId, TransStatus transStatus,
            String updater) {
        repo.updateTransStatusByIssuerBankId(issuerBankId, transStatus.getCode(), updater,
                System.currentTimeMillis());
    }

    @Override
    public List<BlackListIpGroupDO> findByIssuerBankId(Long issuerBankId) {
        return repo.findByIssuerBankId(issuerBankId).stream().map(BlackListIpGroupDO::valueOf)
          .collect(Collectors.toList());
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(BlackListIpGroup.class, issuerBankId, deleter, deleteMillis);
    }

    @Override
    public Page<BlackListIpGroupDO> findAll(BlackListIpGroupDO blackListIpGroupDO,
      Pageable pageable) {

        Specification<BlackListIpGroup> spec = (ipGroupRoot, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predList = new ArrayList<>();
            Predicate pred;

            if (blackListIpGroupDO.getIssuerBankId() != null) {
                pred =
                  criteriaBuilder.equal(
                    ipGroupRoot.get("issuerBankId"), blackListIpGroupDO.getIssuerBankId());
                predList.add(pred);
            }

            String ip = blackListIpGroupDO.getIp();
            if (StringUtils.isNotBlank(ip)) {
                if (IpUtils.isIPv4(ip)) {
                    ip = IpUtils.ip4To6(ip);
                }
                pred = criteriaBuilder.like(ipGroupRoot.get("ip"), "%" + ip + "%");
                predList.add(pred);
            }

            pred = criteriaBuilder.equal(ipGroupRoot.get("deleteFlag"), 0);
            predList.add(pred);

            Long startTime = blackListIpGroupDO.getStartTime();
            Long endTime = blackListIpGroupDO.getEndTime();
            if (startTime != null && endTime == null) {
                pred = criteriaBuilder.ge(ipGroupRoot.get("createMillis"), startTime);
                predList.add(pred);
            } else if (startTime == null && endTime != null) {
                pred = criteriaBuilder.le(ipGroupRoot.get("createMillis"), endTime);
                predList.add(pred);
            } else if (startTime != null && endTime != null) {
                pred =
                  criteriaBuilder.between(
                    ipGroupRoot.get("createMillis"), startTime, endTime);
                predList.add(pred);
            }

            Predicate[] predicates = new Predicate[predList.size()];
            return criteriaBuilder.and(predList.toArray(predicates));
        };

        Page<BlackListIpGroup> blackListIpGroupPage = repo.findAll(spec, pageable);
        if (blackListIpGroupPage.isEmpty()) {
            return Page.empty();
        }
        List<BlackListIpGroupDO> list = blackListIpGroupPage.getContent().stream()
          .map(BlackListIpGroupDO::valueOf).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, blackListIpGroupPage.getTotalElements());
    }

    @Override
    public List<BlackListIpGroupDO> findByIdIn(List<Long> ids, Pageable pageable) {
        return repo.findByIdIn(ids, pageable).stream().map(BlackListIpGroupDO::valueOf).collect(
          Collectors.toList());
    }

    private Integer cidr4to6(Integer cidr4) {
        String ipv4 = String.format("127.0.0.1/%d", cidr4);
        String ipv6 = IpUtils.ip4To6(ipv4);
        return Integer.parseInt(ipv6.split("/")[1]);
    }

}
