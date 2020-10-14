package hu.csapatnev.webshop.jpa.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hu.csapatnev.webshop.jpa.dao.model.ShopItem;

@Repository
public class ShopItemDao extends AbstractJpaDao<ShopItem> implements IShopItemDao  {
	public ShopItemDao() {
        super();

        setClazz(ShopItem.class);
    }
	
	@Transactional
	public Optional<List<ShopItem>> getBestOf(int num) {
		try {
			return Optional.of(entityManager.createQuery("SELECT s FROM ShopItem s ORDER By s.added DESC", ShopItem.class).setMaxResults(num).getResultList());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
