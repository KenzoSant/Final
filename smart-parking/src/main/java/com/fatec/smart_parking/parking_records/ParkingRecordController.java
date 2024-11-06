package com.fatec.smart_parking.parking_records;

import com.fatec.smart_parking.core.authentication.AuthenticationService;
import com.fatec.smart_parking.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/parking-records")
public class ParkingRecordController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ParkingRecordService parkingRecordService;

    @PostMapping("/entry")
    public ResponseEntity<ParkingRecordDTO> create(@RequestBody PlateDTO plateDTO) {
        this.parkingRecordService.create(plateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/exit")
    public ResponseEntity<ParkingRecordDTO> finalize(@RequestBody PlateDTO plateDTO) {
        this.parkingRecordService.finalize(plateDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public Flux<ServerSentEvent<List<ParkingRecordDTO>>> findAllCurrentRecords(@PathVariable Long id) {
        return Flux.interval(Duration.ofSeconds(5))
                .flatMap(sequence ->
                        Mono.fromCallable(() -> parkingRecordService.findByCurrentRecords(id))
                                .map(records -> ServerSentEvent.<List<ParkingRecordDTO>>builder()
                                        .data(records)
                                        .build())
                                .onErrorReturn(ServerSentEvent.<List<ParkingRecordDTO>>builder().data(Collections.emptyList()).build())
                );
    }


    @GetMapping("/history")
    public ResponseEntity<List<ParkingHistoryDTO>> parkingHistory() {
        return ResponseEntity.ok(this.parkingRecordService.parkingHistory());
    }


}
