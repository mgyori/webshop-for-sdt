package hu.csapatnev.webshop.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hu.csapatnev.webshop.jpa.model.OrderDetails;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
	@Transactional
	@Modifying
	@Query("UPDATE OrderDetails o SET o.paid = :state WHERE o.id = :id")
    void setPaidStatus(@Param("id") Long id, @Param("state") boolean status);
	
}
