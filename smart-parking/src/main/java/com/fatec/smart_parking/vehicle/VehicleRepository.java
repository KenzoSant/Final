package com.fatec.smart_parking.vehicle;

import ch.qos.logback.core.net.server.Client;
import com.fatec.smart_parking.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByPlate(String plate);

    List<Vehicle> findByClient(User client);
}
