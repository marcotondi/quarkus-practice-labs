package io.github.marcotondi.labs.scenario.patient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class PatientService {

    @Inject
    JsonWebToken jwt;

    public PatientResponse create(PatientRequest request) {
        throw new UnsupportedOperationException();
    }

    public PatientResponse get(Long id) {
        throw new UnsupportedOperationException();
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }
}
