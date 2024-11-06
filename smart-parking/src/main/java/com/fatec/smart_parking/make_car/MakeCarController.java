package com.fatec.smart_parking.make_car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/makecars")
public class MakeCarController {

    @Autowired
    private MakeCarService makeCarService;

    @GetMapping
    public ResponseEntity<List<MakeCarDTO>> findAll() {
        return ResponseEntity.ok(makeCarService.findAll());
    }
}
