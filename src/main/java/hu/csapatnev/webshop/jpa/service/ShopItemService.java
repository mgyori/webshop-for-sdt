package hu.csapatnev.webshop.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.csapatnev.webshop.jpa.dao.interfaces.IShopItemDao;
import hu.csapatnev.webshop.jpa.model.ShopItem;

@Service
@Transactional
public class ShopItemService {

	@Autowired
    private IShopItemDao dao;
	
    public ShopItemService() {
        super();
    }

    // API

    public void create(final ShopItem entity) {
        dao.create(entity);
    }

    public ShopItem findOne(final long id) {
        return dao.findOne(id);
    }

    public List<ShopItem> findAll() {
        return dao.findAll();
    }
    
    public List<ShopItem> getBestOf(int num) {
    	return dao.getBestOf(num).get();
    }
}
