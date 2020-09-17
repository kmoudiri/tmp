package com.pfizer.restapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;    /** Technical identifier.  primary key */

    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String gender;
    private boolean active;
    private Date registerDate;
    private int age;

    @OneToMany(mappedBy = "patient")
    private List<Consultation> consultations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name= "doctor_id")
    private Doctor doctor;

    @OneToMany(mappedBy = "patient")
    private List<DailySum> summaries = new ArrayList<>();

}
