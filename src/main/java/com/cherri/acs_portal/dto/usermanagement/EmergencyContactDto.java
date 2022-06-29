package com.cherri.acs_portal.dto.usermanagement;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmergencyContactDto {

    private Long id;
    private String email;

    private EmergencyContactDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

//  public static EmergencyContactDto valueOf(EmergencyContact emergencyContact) {
//    return new EmergencyContactDto(emergencyContact.getId(), emergencyContact.getEmail());
//  }
}
