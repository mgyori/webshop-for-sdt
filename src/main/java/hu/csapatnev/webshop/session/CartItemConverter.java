package hu.csapatnev.webshop.session;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Autowired;

import hu.csapatnev.webshop.jpa.service.ShopItemService;

public class CartItemConverter implements AttributeConverter<List<CartItem>, String> {
	@Autowired
    private ShopItemService shopItems;
	
	private static final String SPLIT_CHAR = ";";
	
	@Override
	public String convertToDatabaseColumn(List<CartItem> attribute) {
		List<String> list = new ArrayList<String>();
		for (CartItem item : attribute)
			list.add(item.getItem().getId() + ":" + item.getCount());
		return String.join(SPLIT_CHAR, list.toArray(new String[list.size()]));
	}

	@Override
	public List<CartItem> convertToEntityAttribute(String dbData) {
		List<CartItem> list = new ArrayList<CartItem>();
		
		String[] args = dbData.split(SPLIT_CHAR);
		for (String data : args) {
			System.out.println(data);
			String[] s_args = data.split(":");
			try {
				list.add(new CartItem(shopItems.findOne(Integer.parseInt(s_args[0])), Integer.parseInt(s_args[1])));
			} catch(Exception e) {
				
			}
		}
		return list;
	}

}
