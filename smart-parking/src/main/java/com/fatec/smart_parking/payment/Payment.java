package com.fatec.smart_parking.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fatec.smart_parking.parking_price.ParkingPrice;
import com.fatec.smart_parking.parking_records.ParkingRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_records_id", nullable = false)
    private ParkingRecord parkingRecord;

    @ManyToOne
    @JoinColumn(name = "parking_prices_id", nullable = false)
    private ParkingPrice parkingPrice;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "pix_code", nullable = false)
    private String pixCode;

    @Column(name = "url_qrcode", nullable = false)
    private String urlCode;




}
