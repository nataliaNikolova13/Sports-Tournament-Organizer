package com.fmi.sporttournament.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailResponse {
    String to;
    String subject;
    String text;
}
