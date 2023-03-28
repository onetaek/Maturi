package com.maturi.entity.article;

public enum ReportStatus {
  WAITING, // 신고 신청 상태... 처리 대기중
  REPORT_COMPLETE, // 처리 완료. 해당 글 비활성화(REPORT_COMPLETE) 처리
  CANCEL // 신고 취소 처리...

}
