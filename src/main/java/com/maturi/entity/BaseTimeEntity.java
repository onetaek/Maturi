package com.maturi.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
  @CreatedDate
  private LocalDateTime createdDate;
  @LastModifiedDate
  private LocalDateTime modifiedDate;

  public static String getDurationByDate(LocalDateTime date){
    //현재시간
    LocalDateTime currentTime = LocalDateTime.now();

    //두시간의 차이를 구함
    Duration duration = Duration.between(date, currentTime);

    //차이를 초, 분 시간, 일, 달, 년, 단위로 추출
    long seconds = duration.getSeconds();
    long minutes = duration.toMinutes();
    long hours = duration.toHours();
    long days = duration.toDays();
    long months = days / 30;
    long years = days/365;

    //결과를 리턴
    if (years > 0) {
      return years + "년 전";
    } else if (months > 0) {
      return months + "달 전";
    } else if (days > 0) {
      return days + "일 전";
    } else if (hours > 0) {
      return hours + "시간 전";
    } else if (minutes > 0) {
      return minutes + "분 전";
    } else {
      return seconds + "초 전";
    }
  }

  public static String getFormatDate(LocalDateTime date){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
    return date.format(formatter) ;
  }
}
