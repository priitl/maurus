package com.priitlaht.ppwebtv.frontend.activity;

import com.priitlaht.ppwebtv.common.ApplicationConstants;
import com.priitlaht.ppwebtv.common.util.security.SecurityUtil;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Inject
  private SimpMessageSendingOperations messagingTemplate;

  @SubscribeMapping("/topic/activity")
  @SendTo("/topic/tracker")
  public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
    activityDTO.setUserLogin(SecurityUtil.getCurrentUserLogin());
    activityDTO.setUserLogin(principal.getName());
    activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
    activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(ApplicationConstants.IP_ADDRESS).toString());
    Instant instant = Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis());
    activityDTO.setTime(dateTimeFormatter.format(ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault())));
    log.debug("Sending user tracking data {}", activityDTO);
    return activityDTO;
  }

  @Override
  public void onApplicationEvent(SessionDisconnectEvent event) {
    ActivityDTO activityDTO = new ActivityDTO();
    activityDTO.setSessionId(event.getSessionId());
    activityDTO.setPage("logout");
    messagingTemplate.convertAndSend("/topic/tracker", activityDTO);
  }
}
