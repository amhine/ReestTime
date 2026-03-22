package com.RestTime.RestTime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueResponseDTO {
    private Long id;
    private String action;
    private String details;
    private LocalDateTime dateAction;
    private Long demandeId;
    private Long userId;
}
