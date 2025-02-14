package de.extremecoffee.basket;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
public class BasketItem extends PanacheEntityBase {
  @Id
  @GeneratedValue
  @Schema(readOnly = true)
  public Long id;

  @ManyToOne
  @JoinColumns({@JoinColumn(name = "bagsizeid"), @JoinColumn(name = "productid")})
  public Item item;

  @JsonbTransient
  @ManyToOne
  @JoinColumn(name = "basket_id")
  public Basket basket;

  public Integer quantity;
}
