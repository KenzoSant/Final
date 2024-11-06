package com.fatec.smart_parking.parking_records;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    @Query("SELECT pr FROM ParkingRecord pr JOIN pr.vehicle v WHERE pr.entryTime IS NOT NULL AND pr.exitTime IS NULL AND v.client.id = :userId")
    List<ParkingRecord> findByUserId( Long userId);

    @Query("SELECT pr FROM ParkingRecord pr JOIN pr.vehicle v WHERE pr.entryTime IS NOT NULL AND pr.exitTime IS NULL")
    List<ParkingRecord> findAllCurrent();





}
