package com.cherri.acs_portal.util;

import com.cherri.acs_portal.dto.report.CardBrandTransStatusDTO;
import java.text.DecimalFormat;

public class ThreeDsRateUtil {

    public enum ThreeDsVersion {
        ONE,
        TWO
    }

    public static double calculateNRate(
      CardBrandTransStatusDTO.TransStatus aResTransStatus,
      CardBrandTransStatusDTO.TransStatus rReqTransStatus,
      ThreeDsVersion version) {
        Long aresY = aResTransStatus.getY();
        Long aresN = aResTransStatus.getN();
        Long aresA = aResTransStatus.getA();
        Long aresU = aResTransStatus.getU();
        Long aresR = aResTransStatus.getR();

        Long rresY = rReqTransStatus.getY();
        Long rresN = rReqTransStatus.getN();
        Long rresA = rReqTransStatus.getA();
        Long rresU = rReqTransStatus.getU();
        Long rresR = rReqTransStatus.getR();

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        double nRate;
        if (ThreeDsVersion.ONE == version) {
            //        1.0 計算公式說明:
            //        N-Rate = (PARes=N) / [(PARes=Y+N+A+U) + (VERes=N)]
            long denominator = rresY + rresN + rresA + rresU + aresN;

            nRate = rresN / (double) (denominator);
            nRate = Double.isNaN(nRate) ? 0d : Double.parseDouble(decimalFormat.format(nRate));
            return nRate;
        }
        //        2.0 計算公式說明:
        //        N-Rate = [(ARes=N or R) + (RReq=N or R)] / [(ARes=Y+N+A+U+R) + (RReq=Y+N+A+U+R)]
        long denominator =
          aresY + aresN + aresA + aresU + aresR + rresY + rresN + rresA + rresU + rresR;

        nRate = ((aresN + aresR) + (rresN + rresR)) / (double) (denominator);
        nRate = Double.isNaN(nRate) ? 0d : Double.parseDouble(decimalFormat.format(nRate));
        return nRate;
    }

    public static double calculateURate(
      CardBrandTransStatusDTO.TransStatus aResTransStatus,
      CardBrandTransStatusDTO.TransStatus rReqTransStatus,
      ThreeDsVersion version) {
        Long aresY = aResTransStatus.getY();
        Long aresN = aResTransStatus.getN();
        Long aresA = aResTransStatus.getA();
        Long aresU = aResTransStatus.getU();
        Long aresR = aResTransStatus.getR();

        Long rresY = rReqTransStatus.getY();
        Long rresN = rReqTransStatus.getN();
        Long rresA = rReqTransStatus.getA();
        Long rresU = rReqTransStatus.getU();
        Long rresR = rReqTransStatus.getR();

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        double uRate;
        long denominator;
        if (ThreeDsVersion.ONE == version) {
            //        1.0 計算公式說明:
            //        U-Rate = (PARes=U) / (Y+N+U+A+(VERes=N))
            denominator = rresY + rresN + rresA + rresU + aresN;

            uRate = rresU / (double) (denominator);
        } else {

            //        2.0 計算公式說明:
            //        U-Rate = [(ARes=U) + (RReq=U)] / [(ARes=Y+N+A+U+R) + (RReq=Y+N+A+U+R)]
            denominator =
              aresY + aresN + aresA + aresU + aresR + rresY + rresN + rresA + rresU + rresR;

            uRate = (aresU + rresU) / (double) (denominator);
        }
        uRate = Double.isNaN(uRate) ? 0d : Double.parseDouble(decimalFormat.format(uRate));

        return uRate;
    }
}
