package com.fatec.smart_parking.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;


    @PostMapping
    public ResponseEntity<VehicleDTO> create(@RequestBody VehicleDTO vehicleDTO) {
       VehicleDTO vehicle = this.vehicleService.create(vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> findAll() {
        List<VehicleDTO> vehicleDTOList = this.vehicleService.findByAll();
        return ResponseEntity.status(HttpStatus.OK).body(vehicleDTOList);

    }
}
