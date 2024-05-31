package com.delidronehubs.delidronehubs.classes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "drones")
public class Drone {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "battery_level", nullable = false)
    private Integer batteryLevel;

    @Column(name = "payload_capacity", nullable = false)
    private Double payloadCapacity;

    @Column(name = "current_location", nullable = false, length = Integer.MAX_VALUE)
    private String currentLocation;

    @Override
    public String toString() {
        return "Drone{" +
                "id=" + id +
                ", hubId=" + hubId +
                ", status='" + status + '\'' +
                ", batteryLevel=" + batteryLevel +
                ", payloadCapacity=" + payloadCapacity +
                ", currentLocation='" + currentLocation + '\'' +
                '}';
    }
}
