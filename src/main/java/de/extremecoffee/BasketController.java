package de.extremecoffee;

import de.extremecoffee.basket.Basket;
import de.extremecoffee.basket.BasketItem;
import de.extremecoffee.basket.Item;
import de.extremecoffee.basket.ItemId;
import jakarta.transaction.Transactional;
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
  @POST
  @Transactional
  @Path("/createEphemeral")
  public UUID createEphemeral() {
    var basket = new Basket();
    basket.persist();
    return basket.id;
  }

  @POST
  @Transactional
  @Path("/{basketId}/update")
  @APIResponse(
      responseCode = "200",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = Basket.class)))
  @APIResponse(responseCode = "404")
  public Response update(ItemId itemId, @RestQuery Integer quantity, UUID basketId) {
    Basket basket = Basket.findById(basketId);
    Item item = Item.findById(itemId);

    if (basket == null) {
      return Response.status(404)
          .entity("{\"message\": \"Basket with id " + basketId + " not found\"}")
          .build();
    }

    if (item == null) {
      return Response.status(404).entity("{\"message\":\"Item does not exist\"}").build();
    }

    for (var basketItem : basket.basketItems) {
      if (basketItem.item.equals(item)) {
        if (quantity == 0) {
          basket.basketItems.remove(basketItem);
          basketItem.delete();
          return Response.ok(basket).entity(basket).build();
        }
        basketItem.quantity = quantity;
        return Response.ok(basket).build();
      }
    }

    if (quantity == 0) {
      return Response.ok(basket).build();
    }

    BasketItem basketItem = new BasketItem();
    basketItem.quantity = quantity;
    basketItem.item = item;
    basketItem.basket = basket;
    basketItem.persist();
    basket.basketItems.add(basketItem);
    return Response.ok(basket).build();
  }

  @GET
  @Path("/{id}")
  public Basket getById(UUID id) {
    return Basket.findById(id);
  }
}
