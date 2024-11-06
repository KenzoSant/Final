package com.fatec.smart_parking.admin;

import com.fatec.smart_parking.parking_records.ParkingRecordAllDTO;
import com.fatec.smart_parking.parking_records.ParkingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private ParkingRecordService parkingRecordService;

    @GetMapping("/current-records")
    public ResponseEntity<List<ParkingRecordAllDTO>> findAllCurrentRecords(){
       List<ParkingRecordAllDTO> listRecords =  this.parkingRecordService.findAllCurrent();
        return ResponseEntity.ok(listRecords);
    }


}
