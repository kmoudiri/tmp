package com.pfizer.restapi.representation;


import com.pfizer.restapi.model.Patient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PatientRepresentation {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String gender;
    private boolean active;
    private Date registerDate;
    private int age;
    /**
     * The URL of this resource.
     */
    private String uri;



    public PatientRepresentation(
            Patient patient) {
        if (patient != null) {
            username = patient.getUsername();
            password = patient.getPassword();
            name = patient.getName();
            surname = patient.getSurname();
            email = patient.getEmail();
            gender = patient.getGender();
            active = patient.isActive();
            registerDate = patient.getRegisterDate();
            age = patient.getAge();
            uri = "http://localhost:9000/v1/patient/" + patient.getId();
        }
    }

    public Patient createPatient() {
        Patient patient = new Patient();
        patient.setUsername(username);
        patient.setPassword(password);
        patient.setName(name);
        patient.setSurname(surname);
        patient.setEmail(email);
        patient.setGender(gender);
        patient.setActive(active);
        patient.setRegisterDate(registerDate);
        patient.setAge(age);

        return patient;
    }
}



