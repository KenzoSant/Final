package com.fatec.smart_parking.payment;

import com.fatec.smart_parking.parking_records.ParkingRecord;
import com.fatec.smart_parking.parking_records.ParkingRecordDTO;
import com.fatec.smart_parking.parking_records.ParkingRecordService;
import com.fatec.smart_parking.parking_records.PlateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ParkingRecordService parkingRecordService;

    @PostMapping
    public ResponseEntity<ParkingRecordDTO> create(@RequestBody PlateDTO plate) {
        ParkingRecord parkingRecord = this.parkingRecordService.findCurrentByPlate(plate.plate());
        Payment payment = this.paymentService.create(parkingRecord);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
