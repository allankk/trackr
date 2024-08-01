package com.allank.trackr.dto.response.session;

import java.time.Instant;
import java.util.List;

public record GroupedSessionResponseDto(
        Instant date,
        List<SessionResponseDto> sessions
) {
}
