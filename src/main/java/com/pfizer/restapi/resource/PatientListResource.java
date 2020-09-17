package com.pfizer.restapi.resource;

import com.pfizer.restapi.exception.BadEntityException;
import com.pfizer.restapi.exception.NotFoundException;
import com.pfizer.restapi.representation.PatientRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

public interface PatientListResource {

    @Post("json")
    public PatientRepresentation add(PatientRepresentation patientRepresentationIn)
            throws BadEntityException;
    @Get("json")
    public List<PatientRepresentation> getPatients() throws NotFoundException;



}
