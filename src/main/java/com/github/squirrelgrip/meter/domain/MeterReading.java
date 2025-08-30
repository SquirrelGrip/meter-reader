package com.github.squirrelgrip.meter.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.id.uuid.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "meter_readings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"nmi", "timestamp"})
        }
)
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nmi")
    private String nmi;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "consumption")
    private BigDecimal consumption;

    public UUID getId() {
        return id;
    }

    public String getNmi() {
        return nmi;
    }

    public void setNmi(String nmi) {
        this.nmi = nmi;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }
}
