package com.RestTime.RestTime.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoldeCongeDTO {
    private Double soldeActuel;
    private Double soldeUtilise;
    private Double soldeRestant;
}