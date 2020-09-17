package com.pfizer.restapi.repository;



import com.pfizer.restapi.model.Patient;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class PatientRepository {

    private EntityManager entityManager;

    public PatientRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Patient> findById(Long id) {
        Patient patient = entityManager.find(Patient.class, id);
        return patient != null ? Optional.of(patient) : Optional.empty();
    }

    public List<Patient> findAll() {
        return entityManager.createQuery("from Patient").getResultList();
    }

    public Optional<Patient> findByName(String name) {
        Patient patient = entityManager.createQuery("SELECT b FROM Patient b WHERE b.name = :name", Patient.class)
                .setParameter("name", name)
                .getSingleResult();
        return patient != null ? Optional.of(patient) : Optional.empty();
    }

    public Optional<Patient> findByNameNamedQuery(String name) {
        Patient patient = entityManager.createNamedQuery("Patient.findByName", Patient.class)
                .setParameter("name", name)
                .getSingleResult();
        return patient != null ? Optional.of(patient) : Optional.empty();
    }


    public Optional<Patient> save(Patient patient){

        try {
            entityManager.getTransaction().begin();
            entityManager.persist (patient);
            entityManager.getTransaction().commit();
            return Optional.of(patient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public Optional<Patient> update(Patient patient) {

        Patient in = entityManager.find(Patient.class, patient.getId());
        in.setUsername(patient.getUsername());
        in.setPassword(patient.getPassword());
        in.setName(patient.getName());
        in.setSurname(patient.getSurname());
        in.setEmail(patient.getEmail());
        in.setGender(patient.getGender());
        in.setActive(patient.isActive());
        in.setRegisterDate(patient.getRegisterDate());
        in.setAge(patient.getAge());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist (in);
            entityManager.getTransaction().commit();
            return Optional.of(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean remove(Long id){
        Optional<Patient> optionalPatient = findById(id);
        if (optionalPatient.isPresent()){
            Patient p = optionalPatient.get();

            try{
                entityManager.getTransaction().begin();
                entityManager.remove(p);
                entityManager.getTransaction().commit();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return true;
    }
}
