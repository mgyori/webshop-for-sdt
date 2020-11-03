package hu.csapatnev.webshop.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.csapatnev.webshop.jpa.dao.interfaces.IShopItemDao;
import hu.csapatnev.webshop.jpa.model.ShopItem;
import hu.csapatnev.webshop.jpa.repositories.ShopItemRepository;

@Service
@Transactional
public class ShopItemService {

	@Autowired
    private ShopItemRepository shopItemRepository;
	
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
    
    public List<ShopItem> getItems(int page, int limit, String sortBy) {
        Pageable pageableRequest = PageRequest.of(page, limit, Sort.by(sortBy)); 
        Page<ShopItem> items = shopItemRepository.findAll(pageableRequest);
        
        if (items.hasContent())
        	return items.getContent();
        else
        	return null;
    }
}
