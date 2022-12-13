package de.extremecoffee;

import java.util.UUID;

import org.jboss.resteasy.reactive.RestQuery;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import de.extremecoffee.basket.Basket;
import de.extremecoffee.basket.BasketItem;
import de.extremecoffee.basket.Item;
import de.extremecoffee.basket.ItemId;

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
  public Basket update(ItemId itemId, @RestQuery Integer quantity, UUID basketId) {
    Basket basket = Basket.findById(basketId);
    Item item = Item.findById(itemId);

    if (basket == null) {
      throw new NotFoundException();
    }

    if (item == null) {
      // TODO throw not found exception syncronize with products service
      item = new Item();
      item.productId = itemId.productId;
      item.bagSizeId = itemId.bagSizeId;
      item.persist();
    }

    for (var basketItem : basket.basketItems) {
      if (basketItem.item.equals(item)) {
        if(quantity == 0){
          basket.basketItems.remove(basketItem);
          basketItem.delete();
          return basket;
        }
        basketItem.quantity = quantity;
        return basket;
      }
    }

    if(quantity == 0){
      return basket;
    }

    BasketItem basketItem = new BasketItem();
    basketItem.quantity = quantity;
    basketItem.item = item;
    basketItem.basket = basket;
    basketItem.persist();
    basket.basketItems.add(basketItem);
    return basket;
  }

  @GET
  @Path("/{id}")
  public Basket getById(UUID id) {
    return Basket.findById(id);
  }
}
