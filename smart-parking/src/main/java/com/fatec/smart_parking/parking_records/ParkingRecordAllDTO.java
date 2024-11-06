package com.fatec.smart_parking.parking_records;

import java.time.LocalDateTime;

public record ParkingRecordAllDTO(
        String client_name,
        String email,
        String model,
        String plate,
        String parking_name,
        LocalDateTime entry_time,
        LocalDateTime exit_time
) {
}