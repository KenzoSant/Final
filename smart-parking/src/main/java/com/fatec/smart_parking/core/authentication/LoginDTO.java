package com.fatec.smart_parking.core.authentication;

public record LoginDTO(
        String email,
        String password
) {
}
