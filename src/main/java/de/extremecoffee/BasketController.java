package de.extremecoffee;

import de.extremecoffee.basket.Basket;
import de.extremecoffee.basket.ItemId;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.jboss.resteasy.reactive.NoCache;

@Path("/basket")
@SecurityRequirement(name = "Bearer Authentication")
@Authenticated
public class BasketController {
  @Inject BasketService basketService;
  @Inject SecurityIdentity identity;

  @POST
  @NoCache
  @Path("/update")
  @APIResponse(
      responseCode = "200",
      description =
          "Updates users basket with specified item and quantity, removes item when quantity is"
              + " 0",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = Basket.class)))
  @APIResponse(responseCode = "404", description = "One of the items not found")
  public Response update(List<ItemDto> items) {
    Basket updatedBasket =
        basketService.updateItemInBasket(identity.getPrincipal().getName(), items);
    if (updatedBasket == null) {
      return Response.status(404).build();
    }
    return Response.ok(updatedBasket).build();
  }

  @GET
  @NoCache
  @APIResponse(
      responseCode = "200",
      description = "Gets basket of user",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = Basket.class)))
  @APIResponse(
      responseCode = "404",
      description = "User has never added items to basket please update basket once to create it")
  public Response getBasket() {
    Basket basket = basketService.getBasket(identity.getPrincipal().getName());
    if (basket == null) {
      return Response.status(404).build();
    }
    return Response.ok(basket).build();
  }

  @DELETE
  @NoCache
  @APIResponse(
      responseCode = "200",
      description = "Returns basket with item deleted",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = Basket.class)))
  @APIResponse(responseCode = "404", description = "Item does not exists")
  public Response deleteItem(ItemId itemId) {
    Basket basket = basketService.deleteItemFromBasket(identity.getPrincipal().getName(), itemId);
    if (basket == null) {
      return Response.status(404).build();
    }
    return Response.ok(basket).build();
  }
}
