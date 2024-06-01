package com.fmi.sporttournament.Dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailRequestAllUsers {
    String subject;
    String text;
}
