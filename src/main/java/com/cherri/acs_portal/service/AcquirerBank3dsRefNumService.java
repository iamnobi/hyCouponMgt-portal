package com.cherri.acs_portal.service;

import com.cherri.acs_portal.dto.AcquirerBank3dsRefNumDTO;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.oracle.dao.impl.AcquirerBank3dsRefNumDAOImpl;
import ocean.acs.models.oracle.entity.AcquirerBank3dsRefNum;
import org.springframework.stereotype.Service;

/**
 * AcquirerBank3dsRefNumService
 */
@Log4j2
@AllArgsConstructor
@Service
public class AcquirerBank3dsRefNumService {

    private final AcquirerBank3dsRefNumDAOImpl acquirerBank3dsRefNumDAO;

    public boolean create3dsRefNum(String sdkRefNumber, String user) {
        try {
            boolean isExisted = acquirerBank3dsRefNumDAO
                    .findAll()
                    .stream()
                    .anyMatch(element -> element.getSdkReferenceNumber().equals(sdkRefNumber));
            if (isExisted)
                throw new OceanException(ResultStatus.DUPLICATE_DATA_ELEMENT, "3DS SDK reference number is existed.");
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }

        AcquirerBank3dsRefNum acquirerBank3dsRefNum =
                new AcquirerBank3dsRefNum(null,
                        sdkRefNumber,
                        user,
                        System.currentTimeMillis());

        try {
            acquirerBank3dsRefNumDAO.addAcquirerBank3dsRefNum(acquirerBank3dsRefNum);
            return true;
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
        }
    }

    public boolean delete3dsRefNum(long id) {
        try {
            acquirerBank3dsRefNumDAO.deleteAcquirerBank3dsRefNum(id);
            return true;
        }
        catch (DatabaseException e) {
            log.error("[delete3dsRefNum] Data not found. id: {}", id);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
        }
    }

    public List<AcquirerBank3dsRefNumDTO> query3dsRefNumList() {
        try {
            return acquirerBank3dsRefNumDAO
                    .findAll()
                    .stream()
                    .map(AcquirerBank3dsRefNumDTO::valueOf)
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            log.error("[query3dsRefNumList] error");
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage(), e);
        }
    }
}
