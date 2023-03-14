package com.maturi.entity.member;

public enum MemberStatus {
  BAN{
    @Override
    public String toString() {
      return "ban";
    }
  }, WITHDRAW, DORMANT, NORMAL, ADMIN;
}
