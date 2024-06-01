package com.fmi.sporttournament.Dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailRequestOneUser {
    String to;
    String subject;
    String text;
}
