package hu.csapatnev.webshop.jpa.dao.interfaces;

import java.util.List;
import java.util.Optional;

import hu.csapatnev.webshop.jpa.model.OrderDetails;

public interface IOrderDetailsDao {
	OrderDetails findOne(long id);
    List<OrderDetails> findAll();
    void create(OrderDetails entity);
    OrderDetails update(OrderDetails entity);
    void delete(OrderDetails entity);
    void deleteById(long entityId);
    Optional<OrderDetails> findByPaymentId(String id);
    OrderDetails setPaid(OrderDetails order, boolean state);
}
