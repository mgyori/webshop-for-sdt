package hu.csapatnev.webshop.jpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    
    public Page<ShopItem> getItems(int page, int limit, String sortBy, int category) {
        Pageable pageableRequest = PageRequest.of(page, limit, Sort.by(sortBy)); 
        Page<ShopItem> items;
        if (category == 0)
        	items = shopItemRepository.findAll(pageableRequest);
        else
        	items = shopItemRepository.findByCategory(category, pageableRequest);
        
        return items;
    }
    
    public ShopItem findByLink(String link) {
    	Optional<ShopItem> o = dao.findByLink(link);
    	if (o.isPresent())
    		return o.get();
    	return null;
    }
    
    public List<ShopItem> getByCategory(int category, int max) {
    	Optional<List<ShopItem>> o = dao.getByCategory(category, max);
    	if (o.isPresent())
    		return o.get();
    	return new ArrayList<>();
    }
}
