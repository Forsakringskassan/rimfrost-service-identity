package se.fk.github.rimfrost.identity.presentation.rest;

import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fk.rimfrost.identity.jaxrsspec.controllers.generatedsource.IdentityControllerApi;
import se.fk.rimfrost.identity.jaxrsspec.controllers.generatedsource.model.GetIdentityResponse;
import se.fk.rimfrost.identity.jaxrsspec.controllers.generatedsource.model.Idtyp;

/**
 * Identity service that returns an identity from a bearer token of the form {@code <typId>:<varde>}.
 */
@Path("")
@ApplicationScoped
public class IdentityController implements IdentityControllerApi
{
   private static final String BEARER_PREFIX = "Bearer ";

   private static final Logger LOGGER = LoggerFactory.getLogger(IdentityController.class);

   @Inject
   RoutingContext routingContext;

   @Override
   public GetIdentityResponse getIdentity()
   {
      var authorizationHeader = routingContext.request().headers().get(HttpHeaders.AUTHORIZATION);

      if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX))
      {
         throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
      }

      var token = authorizationHeader.substring(BEARER_PREFIX.length());

      var colonIdx = token.indexOf(':');
      if (colonIdx <= 0 || colonIdx + 1 >= token.length())
      {
         throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
      }

      var idtyp = new Idtyp();
      idtyp.setTypId(token.substring(0, colonIdx));
      idtyp.setVarde(token.substring(colonIdx + 1));

      var response = new GetIdentityResponse();
      response.setIdentity(idtyp);
      return response;
   }
}
