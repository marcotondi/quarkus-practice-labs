package io.github.marcotondi.labs.scenario.patient;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient extends PanacheEntity {

    public String firstName;

    public String lastName;

    public String fiscalCode;

    public LocalDate birthDate;

    public Patient() {
    }

    public Patient(String firstName, String lastName, String fiscalCode, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = fiscalCode;
        this.birthDate = birthDate;
    }
}
