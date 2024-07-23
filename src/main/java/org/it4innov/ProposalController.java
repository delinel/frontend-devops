package org.it4innov;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Path("/proposal")
public class ProposalController {

    @Location("proposal.html")
    Template proposalTemplate;

    @Inject
    Template errorPage;
    @RestClient
    RemoteMessage remoteMessage;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Blocking
    @Timeout(2000) // Si le service distant ne répond pas dans les 2000ms, on appelle la méthode helloFallback
    @Retry(maxRetries = 3, delay = 400) // 3 tentatives avec un délai de 200ms entre chaque
    @Fallback(fallbackMethod = "getMessageFallback") // Si le service distant n'est pas joignable, on appelle la méthode helloFallback
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.75, delay = 10, delayUnit = ChronoUnit.SECONDS) // Si 75% des
    public TemplateInstance getMessage() {
        ArrayList<Proposal> listProposal = new ArrayList<Proposal>();
        if (!remoteMessage.getMessage().isEmpty())
            remoteMessage.getMessage().forEach( message -> {
                Proposal proposal = Proposal.builder()
                        .id(message.id())
                        .messageText(message.messageText())
                        .firstName(message.firstName())
                        .dateTime(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(message.dateTime()))
                        .build();
                listProposal.add(proposal);
            });

        return proposalTemplate.data("listProposal", listProposal);
    }

    public TemplateInstance getMessageFallback(){

        String message = "Service indisponnible pour le moment, une mise à jour de nos services est en cours " +
                " veuillez reessayer dans quelques minutes!";
        return errorPage.data("message", message);
    }
}
