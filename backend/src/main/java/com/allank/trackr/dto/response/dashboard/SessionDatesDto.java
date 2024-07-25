package com.allank.trackr.dto.response.dashboard;

import java.time.Instant;
import java.util.List;

public record SessionDatesDto(
        List<Instant> dates
) {
}
