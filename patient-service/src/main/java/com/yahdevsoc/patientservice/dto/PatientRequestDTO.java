package com.yahdevsoc.patientservice.dto;

import com.yahdevsoc.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientRequestDTO(
        @NotBlank(message = "name is required")
        @Size(max = 100,message = "Name cannot be exceed 100 characters")
        String name,
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Date of birth is required")
        String dateOfBirth,
        @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered Date is required")
        String registeredDate
) {
}
