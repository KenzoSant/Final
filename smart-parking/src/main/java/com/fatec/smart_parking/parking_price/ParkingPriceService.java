package com.fatec.smart_parking.parking_price;

import com.fatec.smart_parking.parking.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingPriceService {

    @Autowired
    ParkingPriceRepository parkingPriceRepository;

    public ParkingPrice findCurrent(){
        return this.parkingPriceRepository.findAll().get(0);
    }
}
