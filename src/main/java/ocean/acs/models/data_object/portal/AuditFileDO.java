package ocean.acs.models.data_object.portal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.io.FilenameUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanExceptionForPortal;

@Data
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class AuditFileDO {
    
    private byte[] content;
    private String name;
    private String extension;

    public static AuditFileDO valueOf(File file) {
        AuditFileDO fileDTO = new AuditFileDO();

        if (!file.exists())
            return fileDTO;

        fileDTO.setName(file.getName());
        try {
            fileDTO.setContent(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            log.error("Failed in read file for auditing. filePath: " + file.getPath());
            throw new OceanExceptionForPortal(ResultStatus.NO_SUCH_DATA);
        }

        fileDTO.setExtension(FilenameUtils.getExtension(file.getPath()));

        return fileDTO;
    }

}
