package com.fmi.sporttournament.email.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailResponse {
    String to;
    String subject;
    String text;
}
