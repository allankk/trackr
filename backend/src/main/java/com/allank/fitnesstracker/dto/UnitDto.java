package com.allank.fitnesstracker.dto;

public record UnitDto(
        Long id,
        String name,
        String unit,
        Double conversionFactor
) {
}
