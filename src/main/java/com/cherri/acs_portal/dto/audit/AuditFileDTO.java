package com.cherri.acs_portal.dto.audit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.apache.commons.io.FilenameUtils;

@Data
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class AuditFileDTO {
  private byte[] content;

  private String name;

  private String extension;

  public static AuditFileDTO valueOf(File file) {
      AuditFileDTO fileDTO = new AuditFileDTO();

      if (!file.exists())
          return fileDTO;

      fileDTO.setName(file.getName());
      try {
          fileDTO.setContent(Files.readAllBytes(file.toPath()));
      } catch (IOException e) {
          log.error("Failed in read file for auditing. filePath: " + file.getPath());
          throw new OceanException(ResultStatus.NO_SUCH_DATA);
      }

      fileDTO.setExtension(FilenameUtils.getExtension(file.getPath()));

      return fileDTO;
  }

}
