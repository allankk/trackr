package com.allank.trackr.dto.request.session;

import java.time.Instant;
import java.util.List;

public record SessionRequestDto(
        Instant date,
        String notes,
        List<SessionActivityRequestDto> activities
) {
}
