package com.github.squirrelgrip.meter.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "meter_readings", uniqueConstraints = [UniqueConstraint(columnNames = ["nmi", "timestamp"])])
class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null
        private set

    @JvmField
    @Column(name = "nmi")
    var nmi: String? = null

    @JvmField
    @Column(name = "timestamp")
    var timestamp: LocalDateTime? = null

    @JvmField
    @Column(name = "consumption")
    var consumption: BigDecimal? = null
}