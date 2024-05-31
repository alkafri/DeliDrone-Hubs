package com.delidronehubs.delidronehubs.classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "merchandiser_id")
    private UUID merchandiserId;

    @Column(name = "drone_id")
    private UUID droneId;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "pickup_location", nullable = false, length = Integer.MAX_VALUE)
    private String pickupLocation;

    @Column(name = "delivery_location", nullable = false, length = Integer.MAX_VALUE)
    private String deliveryLocation;

    @Column(name = "order_time", nullable = false)
    private Instant orderTime;

    @Column(name = "delivery_time")
    private Instant deliveryTime;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "special_instructions", length = Integer.MAX_VALUE)
    private String specialInstructions;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", merchandiserId=" + merchandiserId +
                ", droneId=" + droneId +
                ", hubId=" + hubId +
                ", status='" + status + '\'' +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", deliveryLocation='" + deliveryLocation + '\'' +
                ", orderTime=" + orderTime +
                ", deliveryTime=" + deliveryTime +
                ", weight=" + weight +
                ", specialInstructions='" + specialInstructions + '\'' +
                '}';
    }
}
