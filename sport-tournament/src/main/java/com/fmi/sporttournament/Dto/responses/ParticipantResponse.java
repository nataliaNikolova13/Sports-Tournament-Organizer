package com.fmi.sporttournament.Dto.responses;

import com.fmi.sporttournament.entity.enums.ParticipantStatus;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
public class ParticipantResponse {
    private Long userId;
    private Long teamId;
    private ParticipantStatus status;
    private Date timeStamp;
}
