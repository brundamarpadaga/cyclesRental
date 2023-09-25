package com.talentsprint.cycleshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
//	addtocart
//	removefromcart
//	removeAllFromCart
//	checkout
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CycleRepository cycleRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	
	public String addToCart(long cycleId , int quantity, String username) {
		
		
		
	    Optional<User> user = userRepository.findByName(username);

	    if (user != null) {
	        Cart cart = cartRepository.findByUser(user.get());

	        Optional<Cycle> cycle = cycleRepository.findById(cycleId);

	        if(cycle.get().getNumBorrowed() + quantity <= cycle.get().getStock()) {
	            cycle.get().setNumBorrowed(cycle.get().getNumBorrowed()+quantity);
	            }else {
	            	return "OutOfStock";
	            }
	        
	        if (cart == null) {
                // If the cart doesn't exist, create one
                cart = new Cart();
                cart.setUser(user.get());
                cart.setCartItems(new ArrayList<>());
            }
	        
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
	    return "added to cart";
	}
	
	public String removeFromCart(int cycleId, String name) {
		User user = userRepository.findByName(name).orElseGet(null);

        if (user != null) {
            // Retrieve the user's cart using the CartRepository
            Cart cart = cartRepository.findByUser(user);

            if (cart != null) {
                // Find the CartItem with the specified cycle ID
            	
                CartItem cartItem = cart.getCartItems().stream().filter(item -> item.getCycle().getId()==cycleId).findFirst().orElseGet(null);

                if (cartItem != null) {
                    // Remove the CartItem from the cart
                    cart.getCartItems().remove(cartItem);

                    // Update cart and save changes
                    cartRepository.save(cart);
                }
            }
        }
        calculateCartCosts(name);
        return "cycle removed from cart";
	}
	
	
	 public String removeAllItemsFromCart(String username) {
	        // Find the currently logged-in user based on their username
	        User user = userRepository.findByName(username).orElseGet(null);
	        if (user != null) {
	            Cart cart = cartRepository.findByUser(user);
	            if (cart != null) {
	                cart.getCartItems().clear();
	                cartRepository.save(cart);
	            }
	        }
	        calculateCartCosts(username);
	        return "Cart is emptied";
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
            Cart cart = cartRepository.findByUser(user);
            if (cart != null) {
                return cart.getCartItems().stream().filter(item -> item.isBorrowed() == false).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

	public void checkout(String username) {
		User user = userRepository.findByName(username).orElseGet(null);
		
		if(user != null) {
			Cart cart = cartRepository.findByUser(user);
			if(cart != null) {
				cart.getCartItems().stream().peek(item -> {	
				item.setBorrowed(true);
				item.getCycle().setNumBorrowed(item.getCycle().getNumBorrowed()+item.getQuantity());
				});
			}
			cartRepository.save(cart);
		}
		
	}

	public String restock(int id, int count) {
		
		var cycle = cycleRepository.findById((long) id);
		cycle.get().setStock(cycle.get().getStock()+count);
		cycleRepository.save(cycle.get());
		return "Cycle restocked";
	}
	

}
