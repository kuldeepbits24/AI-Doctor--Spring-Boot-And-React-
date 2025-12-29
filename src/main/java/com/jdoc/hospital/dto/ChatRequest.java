package com.jdoc.hospital.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequest {
  @NotBlank
  private String userInput;

  public String getUserInput() { return userInput; }
  public void setUserInput(String userInput) { this.userInput = userInput; }
}
