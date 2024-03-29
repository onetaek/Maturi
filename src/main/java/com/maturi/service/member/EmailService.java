package com.maturi.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class EmailService {
  private final JavaMailSender emailSender;

  public String sendSimpleMessage(String to) throws Exception {
    String key = createKey();
    MimeMessage message = createMessage(to, key);
    try{//예외처리
      emailSender.send(message);
    }catch(MailException es){
      es.printStackTrace();
      throw new IllegalArgumentException("이메일 전송을 실패하였습니다.");
    }
    return key;
  }

  private MimeMessage createMessage(String to, String key)throws Exception{
    MimeMessage  message = emailSender.createMimeMessage();

    message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
    message.setSubject("Maturi 이메일 인증 번호");//제목

    String msgg="";
    msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
    msgg+= "<h3'>인증 코드입니다.</h3>";
    msgg+= "<div style='font-size:130%'>";
    msgg+= "CODE : <strong>";
    msgg+= key + "</strong><div><br/> ";
    msgg+= "</div>";
    message.setText(msgg, "utf-8", "html"); //내용
    message.setFrom(new InternetAddress("dnjsxorchlrh@gmail.com","Maturi")); //보내는 사람

    return message;
  }

  private String createKey() {
    StringBuffer key = new StringBuffer();
    Random rnd = new Random();

    for (int i = 0; i < 8; i++) { // 인증코드 8자리
      int index = rnd.nextInt(3); // 0~2 까지 랜덤

      switch (index) {
        case 0:
          key.append((char) ((int) (rnd.nextInt(26)) + 97));
          //  a~z  (ex. 1+97=98 => (char)98 = 'b')
          break;
        case 1:
          key.append((char) ((int) (rnd.nextInt(26)) + 65));
          //  A~Z
          break;
        case 2:
          key.append((rnd.nextInt(10)));
          // 0~9
          break;
      }
    }
    return key.toString();
  }

}
