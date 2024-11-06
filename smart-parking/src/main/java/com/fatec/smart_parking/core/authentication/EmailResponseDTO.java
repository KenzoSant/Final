package com.fatec.smart_parking.core.authentication;

import java.time.LocalDateTime;

public record EmailResponseDTO(
        String message,
        int statusCode,
        String statusMessage,
        LocalDateTime timestamp
) {
}
