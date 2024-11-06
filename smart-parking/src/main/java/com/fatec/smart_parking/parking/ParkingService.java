package com.fatec.smart_parking.parking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    public Parking findFirstParking() {
        List<Parking> parkingList = this.parkingRepository.findAll();
        Parking parking = parkingList.get(0);
        return parking;
    }
}
