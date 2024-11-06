package com.fatec.smart_parking.parking_records;

public record ParkingHistoryDTO(
        String url_image,
        String name,
        String amount,
        String model
) {
}
