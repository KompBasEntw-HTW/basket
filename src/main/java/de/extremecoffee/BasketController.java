package de.extremecoffee;

import de.extremecoffee.basket.Basket;
import de.extremecoffee.basket.ItemId;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/basket")
public class BasketController {
  @Inject BasketService basketService;

  @POST
  @Path("/createEphemeral")
  @APIResponse(
      responseCode = "200",
      description = "Create Basket and returns its UUID",
      content =
          @Content(mediaType = MediaType.TEXT_PLAIN, schema = @Schema(implementation = UUID.class)))
  public UUID createEphemeral() {
    var basketId = basketService.createNewBasket();
    Log.debugf("Create basket with id %s", basketId.toString());
    return basketId;
  }

  @POST
  @Path("/{basketId}/update")
  @APIResponse(
      responseCode = "200",
      description = "Updates specified basket with specified item and quantity, removes item when qauntity is 0",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = Basket.class)))
  @APIResponse(responseCode = "404", description = "Either item or basket not found")
  public Response update(ItemId itemId, @RestQuery Integer quantity, UUID basketId) {
    Basket updatedBasket = basketService.updateItemInBasket(basketId, quantity, itemId);

    if (updatedBasket == null) {
      return Response.status(404)
          .entity("{\"message\": \"Basket with id " + basketId + " not found or item not found\"}")
          .build();
    }
    return Response.ok(updatedBasket).build();
  }

  @GET
  @Path("/{id}")
  public Basket getById(UUID id) {
    return Basket.findById(id);
  }
}
