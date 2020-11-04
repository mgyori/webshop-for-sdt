package hu.csapatnev.webshop.jpa.repositories;

import org.springframework.stereotype.Repository;

import hu.csapatnev.webshop.jpa.model.ShopItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface ShopItemRepository extends PagingAndSortingRepository<ShopItem, Long>{
	@Query("SELECT i from ShopItem i where i.category = :category")
	Page<ShopItem> findByCategory(@Param("category") int category, Pageable pageable);
	
	@Query("SELECT i from ShopItem i where i.name like :name")
	Page<ShopItem> findByPartialName(@Param("name") String name, Pageable pageable);
}
