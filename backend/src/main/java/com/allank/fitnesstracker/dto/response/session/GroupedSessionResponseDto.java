package com.allank.fitnesstracker.dto.response.session;

import java.time.LocalDate;
import java.util.List;

public record GroupedSessionResponseDto(
        LocalDate date,
        List<SessionResponseDto> sessions
) {
}
