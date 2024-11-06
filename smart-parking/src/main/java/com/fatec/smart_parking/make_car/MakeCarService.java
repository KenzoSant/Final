package com.fatec.smart_parking.make_car;

import com.fatec.smart_parking.core.exception.MakeCarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MakeCarService {

    @Autowired
    private MakeCarRepository makeCarRepository;

    public MakeCar findByName(String name){
        MakeCar makeCar = this.makeCarRepository.findByName(name)
                .orElseThrow(MakeCarNotFoundException::new);
        return makeCar;
    }

    public List<MakeCarDTO> findAll(){
        List<MakeCar> makeCars = this.makeCarRepository.findAll();
        return makeCars.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MakeCarDTO convertToDTO(MakeCar makeCar){

        return new MakeCarDTO(makeCar.getName());
    }
}
