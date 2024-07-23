package org.it4innov;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.LocalDateTime;
import java.util.List;


@RegisterRestClient(configKey = "code-backend-api")
public interface RemoteMessage {
    record Message( Long id, String firstName, String messageText, LocalDateTime dateTime){};
    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Message> getMessage();

}
