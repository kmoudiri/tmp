package com.pfizer.restapi.model;

import javax.persistence.*;

@Entity
public class Consultation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name= "patient_id")
    private Patient patient;

}
