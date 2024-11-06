package com.fatec.smart_parking.color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/colors")
public class ColorController {

    @Autowired
    private ColorService colorService;
    @GetMapping
    public ResponseEntity<List<ColorDTO>> findAll() {
        return ResponseEntity.ok(this.colorService.findAll());
    }
}
