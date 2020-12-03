package hu.csapatnev.webshop.session;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hu.csapatnev.webshop.jpa.model.ShopItem;
import hu.csapatnev.webshop.jpa.service.ShopItemService;

@Component
public class CartItemConverter implements AttributeConverter<List<CartItem>, String> {
    private static ShopItemService shopItems;
	
	private static final String SPLIT_CHAR = ";";
	private static final String SPLIT_CHAR_TWO = ":";
	
	@Autowired
    public void setShopItems(ShopItemService shopItems){
		CartItemConverter.shopItems = shopItems;
    }
	
	@Override
	public String convertToDatabaseColumn(List<CartItem> attribute) {
		List<String> list = new ArrayList<String>();
		for (CartItem item : attribute)
			list.add(item.getItem().getId() + SPLIT_CHAR_TWO + item.getCount());
		return String.join(SPLIT_CHAR, list.toArray(new String[list.size()]));
	}

	@Override
	public List<CartItem> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty())
            return null;
		
		String[] args = dbData.split(SPLIT_CHAR);
		if (args == null || args.length == 0)
            return null;
		
		List<CartItem> list = new ArrayList<CartItem>();
		
		for (String data : args) {
			if (data == null || data.isEmpty())
				break;
			
			String[] s_args = data.strip().split(SPLIT_CHAR_TWO);
			if (s_args.length == 2) {
				ShopItem it = shopItems.findOne(Integer.parseInt(s_args[0]));
				if (it != null) {
					try {
						list.add(new CartItem(it, Integer.parseInt(s_args[1])));
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if (list.size() == 0)
			return null;
		
		return list;
	}

}
