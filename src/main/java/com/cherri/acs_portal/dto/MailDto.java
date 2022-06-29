package com.cherri.acs_portal.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MailDto
 *
 * @author Alan Chen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {

    private String from;
    private String replyTo;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private Date sentDate;
    private String subject;
    private String text;
    private String[] fileNames;


    public void setTo(String to) {
        this.to = new String[]{to};
    }

    public void setCc(String cc) {
        this.cc = new String[]{cc};
    }

    public void setBcc(String bcc) {
        this.bcc = new String[]{bcc};
    }

    public void setTo(String... to) {
        this.to = to;
    }

    public void setCc(String... cc) {
        this.cc = cc;
    }

    public void setBcc(String... bcc) {
        this.bcc = bcc;
    }
}
