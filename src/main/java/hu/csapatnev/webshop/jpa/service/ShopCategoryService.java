package hu.csapatnev.webshop.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.csapatnev.webshop.jpa.dao.interfaces.IShopCategoryDao;
import hu.csapatnev.webshop.jpa.model.ShopCategory;

@Service
@Transactional
public class ShopCategoryService {

	@Autowired
    private IShopCategoryDao dao;

    public ShopCategoryService() {
        super();
    }

    // API

    public void create(final ShopCategory entity) {
        dao.create(entity);
    }

    public ShopCategory findOne(final long id) {
        return dao.findOne(id);
    }

    public List<ShopCategory> findAll() {
        return dao.findAll();
    }
	
}
