package hu.csapatnev.webshop.jpa.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import hu.csapatnev.webshop.session.CartItem;
import hu.csapatnev.webshop.session.CartItemConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * A rendelés ID-je. Automatikusan generálódik.
	 */
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * Számlázási adatok
	 */
	private String firstName;
	private String lastName;
	private String address;
	
	/**
	 * Szállítási adatok
	 */
	private String ship_firstName;
	private String ship_lastName;
	private String ship_address;
	
	/**
	 * Kapcsolat tartási adatok
	 */
	private String phone;
	private String email;
	
	/**
	 * Fizetési mód
	 */
	private int paymentMethod;
	
	/**
	 * Megrendelt tárgyak
	 */
	@Convert(converter = CartItemConverter.class)
	private List<CartItem> items;
	
	/**
	 * Paypal requestID
	 */
	private String paymentID;
	
	/**
	 * Fizetve
	 */
	private boolean paid;
	
	/**
	 * Létrehozás dátuma.
	 */
	private ZonedDateTime added;
	
	@PrePersist
    protected void onPersist() {
		added = ZonedDateTime.now();
    }
}

