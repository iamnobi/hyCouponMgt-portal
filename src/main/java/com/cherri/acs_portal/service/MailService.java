package com.cherri.acs_portal.service;

import com.cherri.acs_portal.dto.MailDto;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * MailService
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public MailService(JavaMailSender javaMailSender,
        SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public boolean sendPlainTextMail(MailDto d) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(d.getFrom());
            simpleMailMessage.setReplyTo(d.getReplyTo());
            simpleMailMessage.setTo(d.getTo());
            simpleMailMessage.setCc(d.getCc());
            simpleMailMessage.setBcc(d.getBcc());
            simpleMailMessage.setSentDate(d.getSentDate());
            simpleMailMessage.setSubject(d.getSubject());
            simpleMailMessage.setText(d.getText());
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (MailException e) {
            log.error("[sendPlainTextMail] Send mail failed. ", e);
            return false;
        }
    }

    /**
     * Send Html Mail
     *
     * @param mailDtoList MailDto Mail detail object list
     * @apiNote Send one letter time cost is 4 second.
     */
    private void sendHtmlMail(List<MailDto> mailDtoList) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            for (MailDto d : mailDtoList) {
                helper.setFrom(d.getFrom());
                helper.setTo(d.getTo());
                helper.setSubject(d.getSubject());
                helper.setText(d.getText(), true);
                long start = System.currentTimeMillis();
                javaMailSender.send(message);
                log.debug("Sent mail to [{}], Time cost: {}ms",
                  StringUtils.normalizeSpace(Arrays.toString(d.getTo())),
                  System.currentTimeMillis() - start);
            }
        } catch (MailException e) {
            log.error("[sendHtmlMail] Send mail failed. {}", e.getMessage());
        } catch (Exception e) {
            log.error("[sendHtmlMail] Unknown Exception. ", e);
        }
    }


    /**
     * Multiple Send HTML Mail
     *
     * @param mailDtoList Mail detail list
     * @apiNote TotalSentCostTime = (TotalMailCount / MaxThreadPoolNum) * OneMailCostTime
     */
    public void multipleSendHtmlMail(List<MailDto> mailDtoList) {
        if (CollectionUtils.isEmpty(mailDtoList)) {
            return;
        }
        log.debug("[multipleSendHtmlMail] Mail list size: {}", mailDtoList.size());

        /* Init value */
        final int maxThreadPoolNum = 8;
        final int threadPoolNum = Math.min(mailDtoList.size(), maxThreadPoolNum);

        /* Create thread pool */
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(
          threadPoolNum, new BasicThreadFactory.Builder()
          .namingPattern("Mail-Send-Thread-%d")
          .daemon(true)
          .build()
        );

        /* Partition */
        List<List<MailDto>> partitionList = Lists
          .partition(mailDtoList, mailDtoList.size() / threadPoolNum);
        try {
            for (int i = 0; i < threadPoolNum; i++) {
                final int point = i;
                try {
                    executorService.execute(() -> sendHtmlMail(partitionList.get(point)));
                } catch (Exception e) {
                    log.warn("[multipleSendHtmlMail] Send Mail Exception", e);
                }
            }
        } finally {
            executorService.shutdown();
            log.debug("Mail-Send-Thread Shutdown.");
        }
    }

    /**
     * Get template html string
     *
     * @param templateName Template html name
     * @param paramMap     Template html parameter
     * @return template string
     */
    public String getMailTemplateString(String templateName, Map<String, Object> paramMap) {
        Context ctx = new Context(LocaleContextHolder.getLocale(), paramMap);
        return templateEngine.process(templateName, ctx);
    }
}
