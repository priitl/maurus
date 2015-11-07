package com.priitlaht.maurus.backend.service;

import com.priitlaht.maurus.common.ApplicationProperties;
import com.priitlaht.maurus.backend.domain.User;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.WordUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.util.Locale;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailService {

  @Inject
  private ApplicationProperties applicationProperties;
  @Inject
  private JavaMailSenderImpl javaMailSender;
  @Inject
  private MessageSource messageSource;
  @Inject
  private SpringTemplateEngine templateEngine;

  @Async
  public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
    log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
      isMultipart, isHtml, to, subject, content);

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
      message.setTo(to);
      message.setFrom(applicationProperties.getMail().getFrom());
      message.setSubject(subject);
      message.setText(content, isHtml);
      javaMailSender.send(mimeMessage);
      log.debug("Sent e-mail to User '{}'", to);
    } catch (Exception e) {
      log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
    }
  }

  @Async
  public void sendActivationEmail(User user, String baseUrl) {
    log.debug("Sending activation e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("activationEmail", context);
    String subject = messageSource.getMessage("email.activation.title", null, locale);
    sendEmail(user.getEmail(), subject, content, false, true);
  }

  @Async
  public void sendPasswordResetMail(User user, String baseUrl) {
    log.debug("Sending password reset e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("passwordResetEmail", context);
    String subject = messageSource.getMessage("email.reset.title", null, locale);
    sendEmail(user.getEmail(), subject, content, false, true);
  }

  @Async
  public void sendSocialRegistrationValidationEmail(User user, String provider) {
    log.debug("Sending social registration validation e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("provider", WordUtils.capitalize(provider));
    String content = templateEngine.process("socialRegistrationValidationEmail", context);
    String subject = messageSource.getMessage("email.social.registration.title", null, locale);
    sendEmail(user.getEmail(), subject, content, false, true);
  }
}
