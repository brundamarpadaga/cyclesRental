package com.talentsprint.cycleshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.talentsprint.cycleshop.entity.Cart;
import com.talentsprint.cycleshop.entity.CartItem;
import com.talentsprint.cycleshop.entity.Cycle;
import com.talentsprint.cycleshop.entity.User;
import com.talentsprint.cycleshop.repository.CartItemRepository;
import com.talentsprint.cycleshop.repository.CartRepository;
import com.talentsprint.cycleshop.repository.CycleRepository;
import com.talentsprint.cycleshop.repository.UserRepository;

@Service
public class CartService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CycleRepository cycleRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	
	public void addToCart(long cycleId , int quantity, String username) {
		
		
		
	    Optional<User> user = userRepository.findByName(username);

	    if (user != null) {
	    	
	        Cart cart = cartRepository.findByUserAndInBagIsFalse(user.get());
	        if(cart == null) {
	        	cart = new Cart();
	        	cart.setUser(user.get());
	        	cart.setCartItems(new ArrayList<>());
	        	cart.setInBag(false);
	        }
	        Optional<Cycle> cycle = cycleRepository.findById(cycleId);
	        
	        if (cycle != null && quantity > 0) {
	          
	        	CartItem existingCartItem = findExistingCartItemByBrand(cart, cycle.get().getBrand());

	        	if (existingCartItem != null) {
                    // If an existing CartItem is found, increase its quantity
	        		cart.getCartItems().remove(existingCartItem);
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                    cart.getCartItems().add(existingCartItem);
                } else {
	        	
		            CartItem cartItem = new CartItem();
		            cartItem.setCycle(cycle.get());
		            cartItem.setQuantity(quantity);
		            //cartItem.setCart(cart);
		            cart.getCartItems().add(cartItem);
                }
	            cartRepository.save(cart);
	        }
	    }
	    calculateCartCosts(username);
	}
	
	public void removeFromCart(int cycleId, String name) {
		User user = userRepository.findByName(name).orElseGet(null);

        if (user != null) {    
            Cart cart = cartRepository.findByUserAndInBagIsFalse(user);
            if (cart != null) {
                CartItem cartItem = cart.getCartItems().stream().filter(item -> item.getCycle().getId()==cycleId).findFirst().orElseGet(null);

                if (cartItem != null) {  
                    cart.getCartItems().remove(cartItem);
                    cartRepository.save(cart);
                }
            }
        }
        calculateCartCosts(name);
	}
	
	
	 public void removeAllItemsFromCart(String username) {
	        // Find the currently logged-in user based on their username
	        User user = userRepository.findByName(username).orElseGet(null);
	        if (user != null) {
	            Cart cart = cartRepository.findByUserAndInBagIsFalse(user);
	            if (cart != null) {
	                cart.getCartItems().clear();
	                cartRepository.save(cart);
	            }
	        }
	        calculateCartCosts(username);
	    }
	private CartItem findExistingCartItemByBrand(Cart cart, String brand) {
        // Iterate through the cart items to find an existing CartItem with the same brand
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getCycle().getBrand().equals(brand)) {
                return cartItem;
            }
        }
        return null; // No existing CartItem with the same brand found
    }
	
	public void calculateCartCosts(String username) {
        User user = userRepository.findByName(username).orElseGet(null);

        if (user != null) {
            Cart cart = cartRepository.findByUser(user);

            if (cart != null) {
                double totalCost = 0.0;

                for (CartItem cartItem : cart.getCartItems()) {
                    double subtotalCost = cartItem.getQuantity() * cartItem.getCycle().getPrice();
                    totalCost += subtotalCost;
                }
                cart.setTotalCost(totalCost);
                cartRepository.save(cart);
            }
        }
    }
	
	public List<CartItem> showCartItems(String username){
		
		User user = userRepository.findByName(username).orElseGet(null);

        if (user != null) {
            Cart cart = cartRepository.findByUserAndInBagIsFalse(user);
            if (cart != null) {
            		System.out.println(cart.getCartItems());
            		return cart.getCartItems();
            }
        }

        return Collections.emptyList();
    }

	public void checkout(String username , Map<String,Integer> borrowedDays) {
		User user = userRepository.findByName(username).orElseGet(null);
		System.out.println(borrowedDays);
		if(user != null) {
			Cart cart = cartRepository.findByUserAndInBagIsFalse(user);
			if(cart != null) {	
				cart.getCartItems().forEach(item -> {
	                int cycleId = (int) item.getId();
	                System.out.println(cycleId);
	                if (borrowedDays.containsKey(String.valueOf(cycleId))) {
	                    int days = borrowedDays.get(String.valueOf(cycleId));
	                    item.setDays(days);
	                    item.getCycle().setNumBorrowed(item.getCycle().getNumBorrowed() + item.getQuantity());
	                    item.getCycle().setStock(item.getCycle().getStock()-item.getQuantity());
	                }
				});
				cart.setInBag(true);
			}
			cartRepository.save(cart);
		}
	}
	
	public List<Cart> returnCheckoutItems(String username){
		User user = userRepository.findByName(username).orElseGet(null);

        if (user != null) {
            List<Cart> carts = cartRepository.findByUserAndInBagIsTrue(user);
           return carts;
        }

        return Collections.emptyList();
	}

	
	public void returnCycle(String username,int cartId,long cycleId) {
		User user = userRepository.findByName(username).orElseGet(null);
		//Cycle cycle = cycleRepository.findById(cycleId).get();
		
		if(user != null ) {
			Cart cart = cartRepository.findById(cartId).get();
			if(cart!=null) {
				List<CartItem> cartItems = cart.getCartItems();
				cartItems = cartItems.stream().peek(item -> {
					if(item.getCycle().getId()==cycleId) {
						if(item.getQuantity()>0)
						{
							item.setQuantity(item.getQuantity()-1);
							Cycle cycle = cycleRepository.findById(cycleId).get();
							cycle.setNumBorrowed(cycle.getNumBorrowed()-1);
							cycle.setStock(cycle.getStock()+1);
							cycleRepository.save(cycle);
						}
					}
				}).collect(Collectors.toList());
				for(CartItem item : cartItems)
					if(item.getQuantity() == 0)
					{
						cartItems.remove(item);
						cartItemRepository.delete(cartItemRepository.getByCycleId(cycleId));
						
					}
				cart.setCartItems(cartItems);	
			}
			cartRepository.save(cart);
		}
		
	}
}
