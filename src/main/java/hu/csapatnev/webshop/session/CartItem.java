package hu.csapatnev.webshop.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import hu.csapatnev.webshop.jpa.model.ShopItem;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ShopItem item;
	private int count;
}
