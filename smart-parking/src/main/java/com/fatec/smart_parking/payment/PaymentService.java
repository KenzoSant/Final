package com.fatec.smart_parking.payment;

import com.fatec.smart_parking.core.exception.ParkingNotPaidException;
import com.fatec.smart_parking.core.exception.QrCodeGenerationException;
import com.fatec.smart_parking.parking_price.ParkingPrice;
import com.fatec.smart_parking.parking_price.ParkingPriceService;
import com.fatec.smart_parking.parking_records.ParkingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Value("${qr.code.service.url}")
    String url;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ParkingPriceService parkingPriceService;

    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<Payment> findAllByUser(Long userId) {
       return this.paymentRepository.findPaymentsByUserId(userId);

    }

    public Payment findByParkingRecord(Long parking_record_id) {
        Optional<Payment>  optPayment= this.paymentRepository.findByParkingRecordId(parking_record_id);
        if(optPayment.isPresent()){
            return optPayment.get();
        }
        throw new ParkingNotPaidException();
    }
    public QrCodeResponseDTO generateQrCode(String valor, Long id) {
        QrCodeDTO qrCodeDTO = new QrCodeDTO(valor,id.toString());
        try{
            QrCodeResponseDTO  qrCodeResponseDTO = restTemplate.postForObject(url, qrCodeDTO, QrCodeResponseDTO.class);
            return qrCodeResponseDTO;
        }catch (RestClientException e){
            throw new QrCodeGenerationException();
        }
    }

    public Payment create(ParkingRecord parkingRecord) {
        LocalDateTime now = LocalDateTime.now();
        int parkedHours = calculateParkedHours(now, parkingRecord.getEntryTime());
        BigDecimal parkingFee = calculateParkingFee(parkedHours);
        QrCodeResponseDTO qrCodeResponseDTO = generateQrCode(parkingFee.toString(), 1L);
        Payment payment = new Payment(null, parkingRecord,this.parkingPriceService.findCurrent(), now,parkingFee, qrCodeResponseDTO.payload(), qrCodeResponseDTO.url_qrcode());
        return this.paymentRepository.save(payment);
    }

    public int calculateParkedHours(LocalDateTime payment_time, LocalDateTime entryTime) {
        long hoursBetween = java.time.Duration.between(entryTime, payment_time).toHours();
        return (int) Math.max(0, hoursBetween);
    }


    public BigDecimal calculateParkingFee(int parkedHours) {
        ParkingPrice parkingPrice = this.parkingPriceService.findCurrent();
        BigDecimal fixedRate = parkingPrice.getFixed_rate();
        BigDecimal extraHourRate = parkingPrice.getExtra_hours_rate();
        int extraHours = Math.max(0, parkedHours - 4);
        BigDecimal finalAmount = fixedRate.add(extraHourRate.multiply(new BigDecimal(extraHours)));

        return finalAmount.setScale(2, RoundingMode.HALF_UP);
    }



}
