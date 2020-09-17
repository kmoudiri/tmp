package com.pfizer.restapi.model;

import javax.persistence.*;

@Entity
public class DailySum {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;    /** Technical identifier.  primary key */

    @ManyToOne
    @JoinColumn(name= "patient_id")
    private Patient patient = new Patient();


}
