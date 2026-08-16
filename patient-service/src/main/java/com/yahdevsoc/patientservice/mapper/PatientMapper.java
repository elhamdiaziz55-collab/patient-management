package com.yahdevsoc.patientservice.mapper;

import com.yahdevsoc.patientservice.dto.PatientRequestDTO;
import com.yahdevsoc.patientservice.dto.PatientResponseDTO;
import com.yahdevsoc.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient){
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO(patient.getId().toString(),
                                                                        patient.getName(),
                                                                        patient.getEmail(),
                                                                        patient.getAddress(),
                                                                        patient.getDateOfBirth().toString());
        return patientResponseDTO;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO){
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.name());
        patient.setAddress(patientRequestDTO.address());
        patient.setEmail(patientRequestDTO.email());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.dateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.registeredDate()));
        return patient;
    }
}
