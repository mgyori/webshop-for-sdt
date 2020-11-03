package hu.csapatnev.webshop.jpa.repositories;

import org.springframework.stereotype.Repository;

import hu.csapatnev.webshop.jpa.model.ShopItem;
import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface ShopItemRepository extends PagingAndSortingRepository<ShopItem, Long>{
}
