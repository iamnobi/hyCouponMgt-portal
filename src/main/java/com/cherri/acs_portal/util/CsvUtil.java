package com.cherri.acs_portal.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.io.input.BOMInputStream;

@Log4j2
public class CsvUtil {

    private static final String COMMA_DELIMITER = ",";

    public static List<String> simpleCSVReader(byte[] fileContent) {
        List<String> records = new ArrayList<>();
        try (InputStream inputStream = new ByteArrayInputStream(fileContent);
          InputStream bomInputStream = new BOMInputStream(inputStream);
          Reader inputStreamReader = new InputStreamReader(bomInputStream);
          BufferedReader br = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.addAll(Arrays.asList(values));
            }
        } catch (IOException ioe) {
            log.error("[simpleCSVReader] Failed in parse csv file with byte content.", ioe);
            throw new OceanException(ResultStatus.IO_EXCEPTION);
        }

        return records;
    }
}
