package com.talentsprint.cycleshop.repository;

import org.springframework.data.repository.CrudRepository;

import com.talentsprint.cycleshop.entity.Cart;
import com.talentsprint.cycleshop.entity.CartItem;

public interface CartItemRepository extends CrudRepository<CartItem,Integer>{

	CartItem getByCycleId(long cycleId);

	//CartItem findByCartAndCycleId(Cart cart, int cycleId);

}
