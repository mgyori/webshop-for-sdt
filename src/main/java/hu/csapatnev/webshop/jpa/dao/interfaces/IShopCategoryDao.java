package hu.csapatnev.webshop.jpa.dao.interfaces;

import java.util.List;

import hu.csapatnev.webshop.jpa.model.ShopCategory;

public interface IShopCategoryDao {
	ShopCategory findOne(long id);
    List<ShopCategory> findAll();
    void create(ShopCategory entity);
    ShopCategory update(ShopCategory entity);
    void delete(ShopCategory entity);
    void deleteById(long entityId);
}
