package de.extremecoffee;

import de.extremecoffee.basket.Item;
import de.extremecoffee.basket.ItemId;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class StockConsumer {

  @Incoming("stock")
  @Blocking
  @Transactional
  public void consume(JsonObject p) {
    StockDto stockDto = p.mapTo(StockDto.class);

    ItemId itemId = new ItemId();
    itemId.bagSizeId = stockDto.bagSizeId();
    itemId.productId = stockDto.productId();

    Item item = Item.findById(itemId);
    if (item == null) {
      item = new Item();
      item.bagSizeId = stockDto.bagSizeId();
      item.productId = stockDto.productId();
      item.quantityInStock = stockDto.quantity();
      item.persist();
    }
    item.quantityInStock = stockDto.quantity();
  }
}
