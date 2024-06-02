package com.fmi.sporttournament.email.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailRequestOneUser {
    String to;
    String subject;
    String text;
}
