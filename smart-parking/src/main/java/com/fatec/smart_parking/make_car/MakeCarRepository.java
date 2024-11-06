package com.fatec.smart_parking.make_car;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MakeCarRepository extends JpaRepository<MakeCar, Long> {
    Optional<MakeCar> findByName(String name);
    List<MakeCar> findAllByEnabledTrue();
}
