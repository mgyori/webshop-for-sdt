package hu.csapatnev.webshop.jpa.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.springframework.cache.annotation.Cacheable;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * Osztály a bolti tárgyak tárolására.
 * @author marko
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShopItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * A tárgy ID-je. Automatikusan generálódik.
	 */
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * A tárgy neve. Nem lehet üres!
	 */
	@Column(nullable = false)
	private String name;
	
	/**
	 * A tárgy ára.
	 */
	private Long price;
	
	/**
	 * A térgy kategóriája.
	 */
	private int category;
	
	/**
	 * A tárgy készlete.
	 */
	private int instock;
	
	/**
	 * Létrehozás dátuma.
	 */
	private ZonedDateTime added;
	
	/**
	 * Tárgy képe.
	 */
	private String image;
	
	@PrePersist
    protected void onPersist() {
		added = ZonedDateTime.now();
    }
}
