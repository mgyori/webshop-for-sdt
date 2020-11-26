package hu.csapatnev.webshop.jpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.csapatnev.webshop.jpa.dao.interfaces.IOrderDetailsDao;
import hu.csapatnev.webshop.jpa.model.OrderDetails;

@Service
@Transactional
public class OrderDetailsService {
	@Autowired
    private IOrderDetailsDao dao;

    public OrderDetailsService() {
        super();
    }

    // API

    public void create(final OrderDetails entity) {
        dao.create(entity);
    }

    public OrderDetails findOne(final long id) {
        return dao.findOne(id);
    }

    public List<OrderDetails> findAll() {
        return dao.findAll();
    }

    public OrderDetails findByPaymentId(String id) {
    	Optional<OrderDetails> o = dao.findByPaymentId(id);
    	if (o.isPresent())
    		return o.get();
    	return null;
    }

	public OrderDetails setPaid(OrderDetails order, boolean state) {
		return dao.setPaid(order, state);
	}
}
