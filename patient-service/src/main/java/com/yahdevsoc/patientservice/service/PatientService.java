package com.yahdevsoc.patientservice.service;

import com.yahdevsoc.patientservice.dto.PatientRequestDTO;
import com.yahdevsoc.patientservice.dto.PatientResponseDTO;
import com.yahdevsoc.patientservice.exception.EmailAlreadyExistsException;
import com.yahdevsoc.patientservice.exception.PatientNotFoundException;
import com.yahdevsoc.patientservice.grpc.BillingServiceGrpcClient;
import com.yahdevsoc.patientservice.kafka.KafkaProducer;
import com.yahdevsoc.patientservice.mapper.PatientMapper;
import com.yahdevsoc.patientservice.model.Patient;
import com.yahdevsoc.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;
    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer){
        this.patientRepository = patientRepository ;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.email())){
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDTO.email());
        }

        Patient savedPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(savedPatient.getId().toString(),savedPatient.getName(),savedPatient.getEmail());

        kafkaProducer.sendEvent(savedPatient);

        return PatientMapper.toDTO(savedPatient);
    }

    public PatientResponseDTO updatePatient(UUID id , PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("Patient not found with id : " + id));
        if (!patient.getEmail().equals(patientRequestDTO.email())
                && patientRepository.existsByEmail(patientRequestDTO.email())) {

            throw new EmailAlreadyExistsException(
                    "A patient with this email already exists: " + patientRequestDTO.email());
        }
        patient.setAddress(patientRequestDTO.address());
        patient.setName(patientRequestDTO.name());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.dateOfBirth()));
        patient.setEmail(patientRequestDTO.email());
        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }


}
