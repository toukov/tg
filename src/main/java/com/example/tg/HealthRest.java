package com.example.tg;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/v1.0/health")
public class HealthRest {
    @Inject
    private DataBaseProvider dbProvider;

    /**
     * Health check end point. Also checks database connection.
     * @return 200 if all ok.
     */
    @GET
    public Response getHealth() {
        boolean dataBaseOk;
        try {
            dataBaseOk = Optional.ofNullable(
                    dbProvider.getMongo()
                            .getDatabase(EnvironmentalConstants.MONGODB_DATABASE_NAME)
                            .listCollectionNames()
                            .first()).isPresent();
        } catch (Exception e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }

        if (dataBaseOk) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }
}
