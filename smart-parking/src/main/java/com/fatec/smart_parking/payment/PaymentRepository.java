package com.fatec.smart_parking.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p JOIN ParkingRecord pr ON pr.id = p.parkingRecord.id " +
            "JOIN pr.vehicle v " +
            "WHERE pr.exitTime IS NOT NULL AND v.client.id = :userId")
    List<Payment> findPaymentsByUserId(@Param("userId") Long userId);

    Optional<Payment> findByParkingRecordId(Long parkingRecordId);
}
