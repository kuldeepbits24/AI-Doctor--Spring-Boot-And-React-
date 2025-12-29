package com.jdoc.hospital.dto;

public class ChatResponse {
  private String reply;
  private boolean diagnosisRecorded;

  public ChatResponse() {}

  public ChatResponse(String reply, boolean diagnosisRecorded) {
    this.reply = reply;
    this.diagnosisRecorded = diagnosisRecorded;
  }

  public String getReply() { return reply; }
  public void setReply(String reply) { this.reply = reply; }
  public boolean isDiagnosisRecorded() { return diagnosisRecorded; }
  public void setDiagnosisRecorded(boolean diagnosisRecorded) { this.diagnosisRecorded = diagnosisRecorded; }
}
