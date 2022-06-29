package com.cherri.acs_portal.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.LangDO;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LanguageResDTO {

    @Getter
    private String languageCode;

    @Getter
    private String displayText;

    @JsonProperty(value = "isDefault")
    private boolean isDefault;

    public static LanguageResDTO valueOf(LangDO d) {
        return new LanguageResDTO(d.getLanguageCode(), d.getDisplayText(), d.isDefault());
    }
}
