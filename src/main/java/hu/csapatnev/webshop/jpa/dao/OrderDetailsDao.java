package hu.csapatnev.webshop.jpa.dao;

import java.util.Optional;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hu.csapatnev.webshop.jpa.dao.interfaces.IOrderDetailsDao;
import hu.csapatnev.webshop.jpa.model.OrderDetails;
import hu.csapatnev.webshop.jpa.repositories.OrderDetailsRepository;

@Repository
public class OrderDetailsDao extends AbstractJpaDao<OrderDetails> implements IOrderDetailsDao  {
	@Autowired
	OrderDetailsRepository orderDetailsRepository;
	
	public OrderDetailsDao() {
        super();

        setClazz(OrderDetails.class);
    }
	
	@Transactional
	public Optional<OrderDetails> findByPaymentId(String id) {
		try {
			return Optional.of(entityManager.createQuery("SELECT o FROM OrderDetails o WHERE o.paymentID = :id", OrderDetails.class).setParameter("id", id).getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
	
	public OrderDetails setPaid(OrderDetails order, boolean state) {
		order.setPaid(state);
		orderDetailsRepository.setPaidStatus(order.getId(), state);
		return order;
	}
}
