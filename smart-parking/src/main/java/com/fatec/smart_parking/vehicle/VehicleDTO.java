package com.fatec.smart_parking.vehicle;

public record VehicleDTO(
        String make,
        String model,
        String plate,
        String year,
        String color
        ) {
}
