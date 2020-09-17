package com.pfizer.restapi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients = new ArrayList<>();
}
