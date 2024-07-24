package com.allank.fitnesstracker.dto.request.session;

import java.time.Instant;
import java.util.List;

public record FilteredSessionsRequestDto(
        Instant startDate,
        Instant endDate,
        List<Long> activityTypes
) {
}
