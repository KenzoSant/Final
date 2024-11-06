package com.fatec.smart_parking.parking_records;

import java.time.LocalDateTime;

public record ParkingRecordDTO(
        String client,
        String model,
        String color,
        String plate,
        String parking,
        LocalDateTime entry_time,
        LocalDateTime exit_time,
        String current_amount
) {
}
