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
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientResourceImpl
        extends ServerResource implements PatientResource {

    public static final Logger LOGGER = Engine.getLogger(PatientResourceImpl.class);

    private long id;
    private PatientRepository patientRepository;

    @Override
    protected void doInit() {
        LOGGER.info("Initialising patient resource starts");
        try {
            patientRepository =
                    new PatientRepository(JpaUtil.getEntityManager());
            id = Long.parseLong(getAttribute("id"));

        } catch (Exception e) {
            id = -1;
        }

        LOGGER.info("Initialising patient resource ends");
    }


    @Override
    public PatientRepresentation getPatient()
            throws NotFoundException {
        LOGGER.info("Retrieve a patient");

        // Check authorization
        ResourceUtils.checkRole(this, Shield.ROLE_ADMIN);


        // Initialize the persistence layer.
        PatientRepository patientRepository = new PatientRepository(JpaUtil.getEntityManager());
        Patient patient;
        try {


            Optional<Patient> optionalPatient = patientRepository.findById(id);


            setExisting(optionalPatient.isPresent());
            if (!isExisting()) {
                LOGGER.config("patient id does not exist:" + id);
                throw new NotFoundException("No patient with  : " + id);
            } else {
                patient = optionalPatient.get();
                LOGGER.finer("User allowed to retrieve a patient.");
                //System.out.println(  userId);
                PatientRepresentation result =
                        new PatientRepresentation(patient);


                LOGGER.finer("Patient successfully retrieved");

                return result;

            }


        } catch (Exception ex) {
            throw new ResourceException(ex);
        }

    }

    @Override
    public void remove() throws NotFoundException {

        LOGGER.finer("Removal of patient");

        ResourceUtils.checkRole(this, Shield.ROLE_ADMIN);
        LOGGER.finer("User allowed to remove a patient.");

        try {

            // Delete company in DB: return true if deleted
            Boolean isDeleted = patientRepository.remove(id);

            // If patient has not been deleted: if not it means that the id must
            // be wrong
            if (!isDeleted) {
                LOGGER.config("Patient id does not exist");
                throw new NotFoundException(
                        "Patient with the following identifier does not exist:"
                                + id);
            }
            LOGGER.finer("Patient successfully removed.");

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when removing a patient", ex);
            throw new ResourceException(ex);
        }


    }

    @Override
    public PatientRepresentation store(PatientRepresentation patientReprIn) throws NotFoundException, BadEntityException {
        LOGGER.finer("Update a patient.");

        ResourceUtils.checkRole(this, Shield.ROLE_ADMIN);
        LOGGER.finer("User allowed to update a patient.");

        // Check given entity
        ResourceValidator.notNull(patientReprIn);
        ResourceValidator.validate(patientReprIn);
        LOGGER.finer("Company checked");

        try {

            // Convert CompanyRepresentation to Company
            Patient patientIn = patientReprIn.createPatient();
            patientIn.setId(id);

            Optional<Patient> patientOut;

            Optional<Patient> optionalPatient = patientRepository.findById(id);

            setExisting(optionalPatient.isPresent());

            // If patient exists, we update it.
            if (isExisting()) {
                LOGGER.finer("Update patient.");

                // Update patient in DB and retrieve the new one.
                patientOut = patientRepository.update(patientIn);


                // Check if retrieved patient is not null : if it is null it
                // means that the id is wrong.
                if (!patientOut.isPresent()) {
                    LOGGER.finer("Patient does not exist.");
                    throw new NotFoundException(
                            "Patient with the following id does not exist: "
                                    + id);
                }
            } else {
                LOGGER.finer("Resource does not exist.");
                throw new NotFoundException(
                        "Company with the following id does not exist: " + id);
            }

            LOGGER.finer("Company successfully updated.");
            return new PatientRepresentation(patientOut.get());

        } catch (Exception ex) {

            throw new ResourceException(ex);
        }


    }


}
