package hu.csapatnev.webshop.jpa.dao.interfaces;

import java.util.List;
import java.util.Optional;

import hu.csapatnev.webshop.jpa.model.ShopItem;

public interface IShopItemDao {
	ShopItem findOne(long id);
    List<ShopItem> findAll();
    void create(ShopItem entity);
    ShopItem update(ShopItem entity);
    void delete(ShopItem entity);
    void deleteById(long entityId);
    public Optional<List<ShopItem>> getBestOf(int num);
    public Optional<ShopItem> findByLink(String link);
    public Optional<List<ShopItem>> getByCategory(int category, int num);
}
