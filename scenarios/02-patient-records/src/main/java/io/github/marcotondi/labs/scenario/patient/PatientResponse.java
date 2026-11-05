package io.github.marcotondi.labs.scenario.patient;

import java.time.LocalDate;

public class PatientResponse {

    public Long id;

    public String firstName;

    public String lastName;

    public String fiscalCode;

    public LocalDate birthDate;

    public String auditUser;

    public PatientResponse() {
    }

    public PatientResponse(Long id, String firstName, String lastName, String fiscalCode,
                           LocalDate birthDate, String auditUser) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = fiscalCode;
        this.birthDate = birthDate;
        this.auditUser = auditUser;
    }
}
