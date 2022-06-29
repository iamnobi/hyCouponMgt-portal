package com.cherri.acs_portal.controller.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyImportVerifyResDTO {

    private List<String> keyComponentsKcv;
    private String keyKcv;
}
