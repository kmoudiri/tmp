package com.pfizer.restapi.resource;

import com.pfizer.restapi.exception.BadEntityException;
import com.pfizer.restapi.exception.NotFoundException;
import com.pfizer.restapi.model.Patient;
import com.pfizer.restapi.representation.PatientRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface PatientResource {

    @Get("json")
    public PatientRepresentation getPatient() throws NotFoundException;

    @Delete
    public void remove() throws NotFoundException;

    @Put("json")
    public PatientRepresentation store(PatientRepresentation patientReprIn)
            throws NotFoundException, BadEntityException;


}
