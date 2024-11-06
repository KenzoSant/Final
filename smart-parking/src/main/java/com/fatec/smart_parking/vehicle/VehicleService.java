package com.fatec.smart_parking.vehicle;

import com.fatec.smart_parking.color.Color;
import com.fatec.smart_parking.color.ColorService;
import com.fatec.smart_parking.core.authentication.AuthenticationService;
import com.fatec.smart_parking.core.exception.VehicleNotFoundException;
import com.fatec.smart_parking.make_car.MakeCar;
import com.fatec.smart_parking.make_car.MakeCarService;
import com.fatec.smart_parking.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MakeCarService makeCarService;

    @Autowired
    private ColorService colorService;

    public Vehicle findByPlate(String plate) {
        Optional<Vehicle>  optVehicle = this.vehicleRepository.findByPlate(plate);
        if(optVehicle.isPresent()){
            Vehicle vehicle = optVehicle.get();
            return vehicle;
        }
        throw new VehicleNotFoundException();
    }

    public VehicleDTO create(VehicleDTO vehicleDTO) {
        User user = this.authenticationService.getCurrentUser();
        MakeCar makeCar = this.makeCarService.findByName(vehicleDTO.make());
        Color color = this.colorService.findByName(vehicleDTO.color());
        Vehicle vehicle = new Vehicle(null,user,makeCar,vehicleDTO.model(),
                vehicleDTO.plate(),Integer.valueOf(vehicleDTO.year()),color,
                true);
        VehicleDTO vehicleCreated = convertToDTO(this.vehicleRepository.save(vehicle));
        return  vehicleCreated;
    }

    public List<VehicleDTO> findByAll(){
        User user = this.authenticationService.getCurrentUser();
        List<Vehicle> vehiclesList = this.vehicleRepository.findByClient(user);

        return vehiclesList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }





    public VehicleDTO convertToDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getMake().getName(),
                vehicle.getModel(),
                vehicle.getPlate(),
                vehicle.getYear().toString(),
                vehicle.getColor().getName()
        );
    }



}
