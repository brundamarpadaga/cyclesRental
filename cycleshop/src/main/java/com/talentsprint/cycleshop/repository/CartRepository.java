package com.talentsprint.cycleshop.repository;


import org.springframework.data.repository.CrudRepository;

import com.talentsprint.cycleshop.entity.Cart;
import com.talentsprint.cycleshop.entity.User;

public interface CartRepository extends CrudRepository<Cart , Integer> {
	public Cart findByUser(User user);

}
