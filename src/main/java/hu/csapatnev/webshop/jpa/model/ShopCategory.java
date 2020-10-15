package hu.csapatnev.webshop.jpa.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShopCategory implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * A kategória ID-je. Automatikusan generálódik.
	 */
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * A kategória neve.
	 */
	@Column(nullable = false)
	private String name;
}
