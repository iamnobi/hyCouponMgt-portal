package com.cherri.acs_portal.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
@UtilityClass
public class QrCodeUtils {

    /**
     * 二維碼BufferedImage對象生成方法
     *
     * @param contents 二維碼內容
     * @param width    二維碼圖片寬度
     * @param height   二維碼圖片高度
     * @param margin   二維碼邊框(0,2,4,8)
     * @return BufferedImage
     * @throws Exception exception
     * @author LinWenLi
     * @date 2018-08-23 12:51:00
     */
    public Byte[] createQRCode(String contents, int width, int height, int margin)
      throws Exception {
        log.info("[QrCodeUtils][createQRCode] Generate QR code image byte array by url string.");

        if (contents == null || "".equals(contents)) {
            throw new Exception("The contents can not be blank。");
        }
        // 二維碼基本參數設置
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        // 設置編碼字符集utf-8
        hints.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8);
        // 設置糾錯等級L/M/Q/H,糾錯等級越高越不易識別，當前設置等級為最高等級H
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 可設置範圍為0-10，但僅四個變化0 1(2) 3(4 5 6) 7(8 9 10)
        hints.put(EncodeHintType.MARGIN, margin);
        // 生成圖片類型為QRCode
        BarcodeFormat format = BarcodeFormat.QR_CODE;
        // 創建位矩陣對象
        BitMatrix matrix = null;
        try {
            // 生成二維碼對應的位矩陣對象
            matrix = new MultiFormatWriter().encode(contents, format, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        // 設置位矩陣轉圖片的參數
        MatrixToImageConfig config =
          new MatrixToImageConfig(Color.black.getRGB(), Color.white.getRGB());

        BufferedImage qrcode = MatrixToImageWriter.toBufferedImage(matrix, config);
        return createQRCodeWithLogo(
          qrcode, 300, 300, QrCodeUtils.class.getResource("/logo.png").getFile(), 3
        );
    }

    /**
     * 二維碼添加LOGO
     *
     * @param qrcode           qrcode
     * @param width            二維碼圖片寬度
     * @param height           二維碼圖片高度
     * @param logoPath         圖標LOGO路徑
     * @param logoSizeMultiple 二維碼與LOGO的大小比例
     * @return BufferedImage
     * @throws Exception exception
     * @author LinWenLi
     * @date 2018-08-23 13:17:07
     */
    public Byte[] createQRCodeWithLogo(
      BufferedImage qrcode, int width, int height, String logoPath, int logoSizeMultiple)
      throws Exception {
        File logoFile = new File(logoPath);
        if (!logoFile.exists() && !logoFile.isFile()) {
            throw new Exception("Logo file not found.");
        }
        try {
            // 讀取LOGO
            BufferedImage logo = ImageIO.read(logoFile);
            // 設置LOGO寬高
            int logoHeight = qrcode.getHeight() / logoSizeMultiple;
            int logoWidth = qrcode.getWidth() / logoSizeMultiple;
            // 設置放置LOGO的二維碼圖片起始位置
            int x = (qrcode.getWidth() - logoWidth) / 2;
            int y = (qrcode.getHeight() - logoHeight) / 2;
            // 新建空畫板
            BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 新建畫筆
            Graphics2D g = (Graphics2D) combined.getGraphics();
            // 將二維碼繪制到畫板
            g.drawImage(qrcode, 0, 0, null);
            // 設置不透明度，完全不透明1f,可設置範圍0.0f-1.0f
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            // 繪制LOGO
            g.drawImage(logo, x, y, logoWidth, logoHeight, null);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(combined, "jpg", byteArrayOutputStream);
            return ArrayUtils.toObject(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw e;
        }
    }
}
