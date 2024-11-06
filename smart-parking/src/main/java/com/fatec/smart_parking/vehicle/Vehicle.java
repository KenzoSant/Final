package com.fatec.smart_parking.vehicle;

import com.fatec.smart_parking.color.Color;
import com.fatec.smart_parking.make_car.MakeCar;
import jakarta.persistence.*;
import lombok.*;
import com.fatec.smart_parking.user.User;


@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "make_id", nullable = false)
    private MakeCar make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, unique = true)
    private String plate;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @Column(nullable = false)
    private Boolean enabled = true;
}
