package com.fatec.smart_parking.admin;

import com.fatec.smart_parking.client.ClientDTO;
import com.fatec.smart_parking.client.ClientService;
import com.fatec.smart_parking.parking_records.ParkingRecordAllDTO;
import com.fatec.smart_parking.parking_records.ParkingRecordDTO;
import com.fatec.smart_parking.parking_records.ParkingRecordService;
import com.fatec.smart_parking.parking_records.ParkingRecordsAverageDTO;
import com.fatec.smart_parking.user.User;
import com.fatec.smart_parking.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private ParkingRecordService parkingRecordService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/current-records")
    public ResponseEntity<List<ParkingRecordAllDTO>> findAllCurrentRecords(){
       List<ParkingRecordAllDTO> listRecords =  this.parkingRecordService.findAllCurrent();
        return ResponseEntity.ok(listRecords);
    }

    @GetMapping("/clients")
    public ResponseEntity<List<UserDTO>> findAllClients(){
        List<UserDTO> parkingRecordDTOS = this.clientService.findAll();
        return ResponseEntity.ok(parkingRecordDTOS);

    }

    @GetMapping("/average-months")
    public ResponseEntity<List<ParkingRecordsAverageDTO>> findAllAverageMonth(){
        List<ParkingRecordsAverageDTO> parkingRecordsAverageDTOS = this.parkingRecordService.findAllAverage();
        return ResponseEntity.ok(parkingRecordsAverageDTOS);

    }





}
