package com.fatec.smart_parking.parking_records;

import com.fatec.smart_parking.core.authentication.AuthenticationService;
import com.fatec.smart_parking.core.exception.ParkingNotPaidException;
import com.fatec.smart_parking.parking.Parking;
import com.fatec.smart_parking.parking.ParkingService;
import com.fatec.smart_parking.payment.Payment;
import com.fatec.smart_parking.payment.PaymentService;
import com.fatec.smart_parking.user.User;
import com.fatec.smart_parking.vehicle.Vehicle;
import com.fatec.smart_parking.vehicle.VehicleService;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingRecordService {
    @Autowired
    private ParkingRecordRepository parkingRecordRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private PaymentService paymentService;

    public ParkingRecordDTO create(PlateDTO plateDTO){
        Vehicle vehicle = this.vehicleService.findByPlate(plateDTO.plate());
        Parking parking = this.parkingService.findFirstParking();
        ParkingRecord parkingRecord = new ParkingRecord(null,vehicle,parking,LocalDateTime.now(),null);
        ParkingRecordDTO parkingRecordDTO = new ParkingRecordDTO(
                vehicle.getClient().getName(), vehicle.getModel(),
                vehicle.getColor().getName(),vehicle.getPlate(),
                parking.getName(), LocalDateTime.now(),null,null);
        this.parkingRecordRepository.save(parkingRecord);
        System.out.println(plateDTO.plate());
        return parkingRecordDTO;
    }

    public boolean isPaid(Long parking_record_id){
       Payment payment =  this.paymentService.findByParkingRecord(parking_record_id);
        if(payment != null){
            return true;
        }
        return false;
    }

    public void finalize(PlateDTO plateDTO){
        Vehicle vehicle = this.vehicleService.findByPlate(plateDTO.plate());
        ParkingRecord parkingRecord = findCurrentByPlate(plateDTO.plate());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minutes = now.minusMinutes(15);
        if(parkingRecord.getEntryTime().isBefore(minutes)){
            if(!isPaid(parkingRecord.getId())){
                throw new ParkingNotPaidException();
            }
        }
        parkingRecord.setExitTime(LocalDateTime.now());
        this.parkingRecordRepository.save(parkingRecord);
    }

    public List<ParkingRecordDTO> findByCurrentRecords(Long id){
        List<ParkingRecord> parkingRecordList = this.parkingRecordRepository.findByUserId(id);
        return  parkingRecordList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ParkingRecord findCurrentByPlate(String plate){
        Vehicle vehicle = this.vehicleService.findByPlate(plate);
        List<ParkingRecord> currentRecords = this.parkingRecordRepository.findByUserId(vehicle.getClient().getId());
        return currentRecords.stream()
                .filter(record -> record.getVehicle().getPlate().equals(plate))
                .findFirst()
                .orElse(null);
    }


    public List<ParkingHistoryDTO> parkingHistory(){
        User user = this.authenticationService.getCurrentUser();
        List<Payment> paymentList = this.paymentService.findAllByUser(user.getId());
        return  paymentList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ParkingRecordAllDTO> findAllCurrent(){
        List<ParkingRecord> parkingRecords = this.parkingRecordRepository.findAllCurrent();
        return parkingRecords.stream()
                .map(this::convertToAllDTO)
                .collect(Collectors.toList());
    }

    public List<ParkingRecordsAverageDTO> findAllAverage(){
        List<Tuple> parkingRecordsAverageDTOS = this.parkingRecordRepository.findAverageRecordsPerMonthAsTuple();
        return parkingRecordsAverageDTOS.stream()
                .map(tuple -> {
                    Integer year = tuple.get(0, Integer.class);
                    Integer month = tuple.get(1, Integer.class);
                    Double averageRecordsPerMonth = tuple.get(2, Double.class);
                    return new ParkingRecordsAverageDTO(year, month, averageRecordsPerMonth);
                })
                .collect(Collectors.toList());
    }


    public ParkingRecordDTO convertToDTO(ParkingRecord parkingRecord) {
        LocalDateTime now = LocalDateTime.now();
        int current_hours = this.paymentService.calculateParkedHours(now,parkingRecord.getEntryTime());
        BigDecimal current_price = this.paymentService.calculateParkingFee(current_hours);
        return new ParkingRecordDTO(
                parkingRecord.getVehicle().getClient().getName(),
                parkingRecord.getVehicle().getModel(),
                parkingRecord.getVehicle().getColor().getName(),
                parkingRecord.getVehicle().getPlate(),
                parkingRecord.getParking().getName(),
                parkingRecord.getEntryTime(),
                parkingRecord.getExitTime(),
                current_price.toString());
    }
    public ParkingRecordAllDTO convertToAllDTO(ParkingRecord parkingRecord){
        return new ParkingRecordAllDTO(
                parkingRecord.getVehicle().getClient().getName(),
                parkingRecord.getVehicle().getClient().getEmail(),
                parkingRecord.getVehicle().getModel(),
                parkingRecord.getVehicle().getPlate(),
                parkingRecord.getParking().getName(),
                parkingRecord.getEntryTime(),
                parkingRecord.getExitTime()
        );
    }


    public ParkingHistoryDTO convertToDTO(Payment payment){
        return new ParkingHistoryDTO(
            payment.getUrlCode(),
            payment.getParkingRecord().getParking().getName(),
            payment.getAmount().toString(),
            payment.getParkingRecord().getVehicle().getModel());
    }




}
