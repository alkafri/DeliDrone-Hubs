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
@Table(name = "routes")
public class Route {
    @Id
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "drone_id")
    private UUID droneId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "start_location", nullable = false, length = Integer.MAX_VALUE)
    private String startLocation;

    @Column(name = "end_location", nullable = false, length = Integer.MAX_VALUE)
    private String endLocation;

    @Column(name = "distance", nullable = false)
    private Double distance;

    @Column(name = "estimated_time", nullable = false)
    private Instant estimatedTime;

    @Column(name = "actual_start_time")
    private Instant actualStartTime;

    @Column(name = "actual_end_time")
    private Instant actualEndTime;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", droneId=" + droneId +
                ", orderId=" + orderId +
                ", startLocation='" + startLocation + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", distance=" + distance +
                ", estimatedTime=" + estimatedTime +
                ", actualStartTime=" + actualStartTime +
                ", actualEndTime=" + actualEndTime +
                ", status='" + status + '\'' +
                '}';
    }
}
