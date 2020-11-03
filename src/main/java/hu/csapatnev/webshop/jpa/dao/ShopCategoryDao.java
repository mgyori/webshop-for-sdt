package hu.csapatnev.webshop.jpa.dao;

import org.springframework.stereotype.Repository;

import hu.csapatnev.webshop.jpa.dao.interfaces.IShopCategoryDao;
import hu.csapatnev.webshop.jpa.model.ShopCategory;

@Repository
public class ShopCategoryDao extends AbstractJpaDao<ShopCategory> implements IShopCategoryDao  {
	public ShopCategoryDao() {
        super();

        setClazz(ShopCategory.class);
    }
}
