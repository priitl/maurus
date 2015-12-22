package com.priitlaht.ppwebtv.common.config.thymeleaf;

import org.apache.commons.lang.CharEncoding;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ThymeleafConfiguration {

  @Bean
  @Description("Thymeleaf template resolver serving HTML 5 emails")
  public ClassLoaderTemplateResolver emailTemplateResolver() {
    ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
    emailTemplateResolver.setPrefix("mails/");
    emailTemplateResolver.setSuffix(".html");
    emailTemplateResolver.setTemplateMode("HTML5");
    emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
    emailTemplateResolver.setOrder(1);
    return emailTemplateResolver;
  }

  @Bean
  @Description("Spring mail message resolver")
  public MessageSource emailMessageSource() {
    log.info("loading non-reloadable mail messages resources");
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/mails/messages/messages");
    messageSource.setDefaultEncoding(CharEncoding.UTF_8);
    return messageSource;
  }
}
