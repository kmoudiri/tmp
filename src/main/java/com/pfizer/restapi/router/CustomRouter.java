package com.pfizer.restapi.router;

import com.pfizer.restapi.resource.PatientListResourceImpl;
import com.pfizer.restapi.resource.PatientResourceImpl;
import com.pfizer.restapi.resource.PingServerResource;
import org.restlet.Application;
import org.restlet.routing.Router;

public class CustomRouter {

    private Application application;

    public CustomRouter(Application application) {
        this.application = application;

    }

    public Router createApiRouter() {

        Router router = new Router(application.getContext());

        router.attach("/patient/{id}", PatientResourceImpl.class);
        router.attach("/patient", PatientListResourceImpl.class);
        router.attach("/patient/", PatientListResourceImpl.class);


        return router;
    }


    public Router publicResources() {
        Router router = new Router();
        router.attach("/ping", PingServerResource.class);
        return router;
    }

}
