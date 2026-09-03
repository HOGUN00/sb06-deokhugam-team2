package com.codeit.sb06deokhugamteam2.book.dto.data;

import java.util.UUID;

public record BookDashboardDto(
        UUID id,
        double periodScore,
        long rank
) {
}
