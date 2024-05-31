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
@Table(name = "hubs")
public class Hub {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "location", nullable = false, length = Integer.MAX_VALUE)
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "available_drones", nullable = false)
    private Integer availableDrones;

    @Override
    public String toString() {
        return "Hub{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", capacity=" + capacity +
                ", availableDrones=" + availableDrones +
                '}';
    }
}
