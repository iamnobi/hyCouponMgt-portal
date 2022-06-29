package com.cherri.acs_portal.util;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
public class FileUtils {

    public static String getFileExtension(final String fileName) {
        if (StringUtils.isNotEmpty(fileName) &&
          (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

    public static String getFileExtension(final MultipartFile file) {
        if (file == null) {
            return "";
        }
        return getFileExtension(file.getOriginalFilename());
    }

    public static boolean isFileExtensionEquals(MultipartFile file, String extName) {
        if (extName == null || file == null) {
            return false;
        }
        String fileExtName = FileUtils.getFileExtension(file);
        return extName.equalsIgnoreCase(fileExtName);
    }

    public static boolean isFileExtensionEqualsAny(MultipartFile file, String... extNames) {
        if (extNames == null || extNames.length == 0 || file == null) {
            return false;
        }
        String fileExtName = FileUtils.getFileExtension(file);
        for (String extName : extNames) {
            if (extName.equalsIgnoreCase(fileExtName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * e.g. pass "static/img/logo.jpg" will get bytes of src/main/resource/static/img/logo.jpg
     *
     * @param fileClassPath file class path with file name and extension.
     * @return file byte[]
     */
    public static byte[] getFileBytes(String fileClassPath) {
        if (StringUtils.isEmpty(fileClassPath)) {
            return new byte[0];
        }

        try {
            InputStream in = new ClassPathResource(fileClassPath).getInputStream();
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            log.error("[getFileBytes] execute failed.", e);
        }
        return new byte[0];
    }

    /**
     * 去掉檔名前面的路徑字串只取檔名:
     *
     * @param fullFilePath ex:C:/aaa/bbb/ccc.txt
     * @return ccc.txt
     * @throws NullPointerException
     */
    public static String extractFilename(String fullFilePath) throws NullPointerException {
        String[] pathParts = fullFilePath.split("\\\\");
        return pathParts[pathParts.length - 1];
    }

    /**
     * 檔名是否超出DB中的file_name or image_name欄位長度
     *
     * @param filename
     * @return true=超出欄位長度; false=範圍內
     * @throws NullPointerException
     */
    public static boolean isFilenameExceededLengthLimit(String filename)
      throws NullPointerException {
        return EnvironmentConstants.MAX_FILENAME_LENGTH < filename.length();
    }

    /**
     * 批次卡號黑名單CSV轉成卡號List
     */
    public static Set<String> extractCardList(byte[] fileContent) {
        List<String> cardCollection = CsvUtil.simpleCSVReader(fileContent);
        return cardCollection.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }
}
