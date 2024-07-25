package com.allank.trackr.dto;

public record UnitDto(
        Long id,
        String name,
        String unit,
        Double conversionFactor
) {
}
