package com.pfizer.restapi.resource;

import com.pfizer.restapi.exception.BadEntityException;
import com.pfizer.restapi.exception.NotFoundException;
import com.pfizer.restapi.model.Patient;
import com.pfizer.restapi.repository.PatientRepository;
import com.pfizer.restapi.repository.util.JpaUtil;
import com.pfizer.restapi.representation.PatientRepresentation;
import com.pfizer.restapi.resource.util.ResourceValidator;
import com.pfizer.restapi.security.ResourceUtils;
import com.pfizer.restapi.security.Shield;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientListResourceImpl

        extends ServerResource implements PatientListResource
{

    public static final Logger LOGGER = Engine.getLogger(PatientResourceImpl.class);


    private PatientRepository patientRepository ;


    @Override
    protected void doInit() {
        LOGGER.info("Initialising patient resource starts");
        try {
            patientRepository =
                    new PatientRepository (JpaUtil.getEntityManager()) ;

        }
        catch(Exception e)
        {

        }

        LOGGER.info("Initialising patient resource ends");
    }




    public List<PatientRepresentation> getPatients() throws NotFoundException{

        LOGGER.finer("Select all patients.");

        // Check authorization
        ResourceUtils.checkRole(this, Shield.ROLE_ADMIN);

        try{

            List<Patient> patients =
                    patientRepository.findAll();
            List<PatientRepresentation> result =
                    new ArrayList<>();




            patients.forEach(patient ->
                    result.add (new PatientRepresentation(patient)));


            return result;
        }
        catch(Exception e)
        {
            throw new NotFoundException("patients not found");
        }


    }





    @Override
    public PatientRepresentation add
            (PatientRepresentation patientRepresentationIn)
            throws BadEntityException {

        LOGGER.finer("Add a new patient.");

        // Check authorization
        ResourceUtils.checkRole(this, Shield.ROLE_ADMIN);
        LOGGER.finer("User allowed to add a patient.");

        // Check entity

        ResourceValidator.notNull(patientRepresentationIn);
        ResourceValidator.validate(patientRepresentationIn);
        LOGGER.finer("Patient checked");

        try {

            // Convert CompanyRepresentation to Company
            Patient patientIn = new Patient();
            patientIn.setUsername(patientRepresentationIn.getUsername());
            patientIn.setPassword(patientRepresentationIn.getPassword());
            patientIn.setName(patientRepresentationIn.getName());
            patientIn.setSurname(patientRepresentationIn.getSurname());
            patientIn.setEmail(patientRepresentationIn.getEmail());
            patientIn.setGender(patientRepresentationIn.getGender());
            patientIn.setActive(patientRepresentationIn.isActive());
            patientIn.setRegisterDate(patientRepresentationIn.getRegisterDate());
            patientIn.setAge(patientRepresentationIn.getAge());


            Optional<Patient> patientOut =
                    patientRepository.save(patientIn);
            Patient patient = null;
            if (patientOut.isPresent())
                patient = patientOut.get();
            else
                throw new
                        BadEntityException(" Patient has not been created");

            PatientRepresentation result =
                    new PatientRepresentation();
            result.setUsername(patient.getUsername());
            result.setPassword(patient.getPassword());
            result.setName(patient.getName());
            result.setSurname(patient.getSurname());
            result.setEmail(patient.getEmail());
            result.setGender(patient.getGender());
            result.setActive(patient.isActive());
            result.setRegisterDate(patient.getRegisterDate());
            result.setAge(patient.getAge());
            result.setUri("http://localhost:9000/v1/patient/"+patient.getId());

            getResponse().setLocationRef(
                    "http://localhost:9000/v1/patient/"+patient.getId());
            getResponse().setStatus(Status.SUCCESS_CREATED);

            LOGGER.finer("Patient successfully added.");

            return result;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when adding a patient", ex);

            throw new ResourceException(ex);
        }



    }

}
