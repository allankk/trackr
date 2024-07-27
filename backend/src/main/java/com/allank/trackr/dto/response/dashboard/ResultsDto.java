package com.allank.trackr.dto.response.dashboard;

import java.time.Instant;
import java.util.List;

public record ResultsDto(
        List<Instant> dates,
        List<Double> results
) {
}
