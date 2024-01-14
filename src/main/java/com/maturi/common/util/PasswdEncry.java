package com.maturi.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswdEncry {
  /*  [사용 방법]
   *  // 객체 생성
   *  PasswdEncry pwEn = new PasswdEncry();
   *  // 암호화
   *  //String res = pwEn.getEncry(pw, pwEn.getSalt());
   *  String res = pwEn.getEncry(pw, "testSalt");
   * */

  public String getSalt() {
    SecureRandom r = new SecureRandom();
    byte[] salt = new byte[20];

    r.nextBytes(salt); // 난수 생성

    // byte To String (10진수 문자열로 변경)
    StringBuffer sb = new StringBuffer();
    for(byte b : salt) {
      sb.append(String.format("%02x", b));
    }

    return sb.toString();
  }

  public String getEncry(String pw, String salt) {
    String result = "";
    try {
      // SHA256 알고리즘 객체 생성
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      // pw와 salt 합친 문자열에 SHA256 적용
      md.update((pw + salt).getBytes());
      byte[] pwSalt = md.digest();

      // byte To String (10진수의 문자열로 변경)
      StringBuffer sb = new StringBuffer();
      for(byte b : pwSalt) {
        sb.append(String.format("%02x", b));
      }

      result = sb.toString();
    } catch(NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return result;
  }
}
