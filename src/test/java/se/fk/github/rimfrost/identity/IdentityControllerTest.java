package se.fk.github.rimfrost.identity;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.fk.rimfrost.identity.jaxrsspec.controllers.generatedsource.model.GetIdentityResponse;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class IdentityControllerTest
{
   @Test
   @DisplayName("IDENT-FR-01.1: Identity is returned for valid bearer token")
   void should_return_identity_on_valid_bearer_token()
   {
      var expectedIdtyp = "abcd";
      var expectedvarde = "1234";

      var response = given().contentType(ContentType.JSON).header(createTokenHeader(expectedIdtyp, expectedvarde))
            .get("/identity").then().statusCode(200).extract().body().as(GetIdentityResponse.class);

      assertNotNull(response);
      var identity = response.getIdentity();

      assertNotNull(identity);
      assertEquals(expectedIdtyp, identity.getTypId());
      assertEquals(expectedvarde, identity.getVarde());
   }

   @Test
   @DisplayName("IDENT-FR-01.2: HTTP 401 is returned for request without authorization header")
   void should_return_401_on_auth_header_missing()
   {
      given().contentType(ContentType.JSON).get(("/identity")).then().statusCode(401);
   }

   @Test
   @DisplayName("IDENT-FR-01.3: HTTP 401 is returned for request with non-bearer authorization header")
   void should_return_401_on_wrong_authorization_header()
   {
      given().contentType(ContentType.JSON).auth().basic("abcd", "1234").get("/identity").then().statusCode(401);
   }

   @Test
   @DisplayName("IDENT-FR-01.4: HTTP 401 is returned for request with token not in <id type>:<id value> form")
   void should_return_401_on_malformed_token()
   {
      given().contentType(ContentType.JSON).header(createTokenHeader("abcd")).get("/identity").then().statusCode(401);
   }

   @Test
   @DisplayName("IDENT-FR-01.5: HTTP 401 is returned for request with empty id type in token")
   void should_return_401_on_empty_id_typ_in_token()
   {
      given().contentType(ContentType.JSON).header(createTokenHeader("", "1234")).get("/identity").then().statusCode(401);
   }

   @Test
   @DisplayName("IDENT-FR-01.5: HTTP 401 is returned for request with empty id value in token")
   void should_return_401_on_empty_varde_in_token()
   {
      given().contentType(ContentType.JSON).header(createTokenHeader("abcd", "")).get("/identity").then().statusCode(401);
   }

   private Header createTokenHeader(String idtyp, String varde)
   {
      return createTokenHeader(idtyp + ":" + varde);
   }

   private Header createTokenHeader(String token)
   {
      return new Header("Authorization", "Bearer " + token);
   }
}
