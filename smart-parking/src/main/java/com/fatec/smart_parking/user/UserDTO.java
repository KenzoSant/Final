package com.fatec.smart_parking.user;


public record UserDTO(
        Long id,
        String name,
        String email,
        String role,
        Boolean enabled
) {
}
