package com.talentsprint.cycleshop.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.talentsprint.cycleshop.entity.Cart;
import com.talentsprint.cycleshop.entity.User;

public interface CartRepository extends CrudRepository<Cart , Integer> {
	public Cart findByUser(User user);
	
	public Cart findByUserAndInBagIsFalse(User user);

	public List<Cart> findByUserAndInBagIsTrue(User user);
	
	public Cart findFirstByUserAndInBagIsFalse(User user);
	
	
	@Query("SELECT c FROM Cart c WHERE c.user = :user AND c.inBag = false")
    Cart findCartByUserAndNotInBag(@Param("user") User user);

}
