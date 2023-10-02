package com.talentsprint.cycleshop.controller;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.talentsprint.cycleshop.entity.Cart;
import com.talentsprint.cycleshop.entity.CartItem;
import com.talentsprint.cycleshop.entity.Cycle;
import com.talentsprint.cycleshop.entity.User;
import com.talentsprint.cycleshop.repository.UserRepository;
import com.talentsprint.cycleshop.service.CartService;
import com.talentsprint.cycleshop.service.CycleService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CycleRestController {
    
    @Autowired
    private CycleService cycleService;
    
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CartService cartService;

    @GetMapping("/health")
    public String checkhealth() {
        return "healthy";
    }

    @GetMapping("/cycle/list")
    public List<Cycle> all() {
        return cycleService.listAvailableCycles();
    }
    
    @PostMapping("/add")
    @ResponseBody
    public String addCycle(@RequestBody Cycle cycle) {
        return cycleService.addCycle(cycle);
    }
    
    @PostMapping("/cycle/{id}/addtocart")
    public void addToCart(@PathVariable int id, @RequestParam int count){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		cartService.addToCart(id, count, username);
    }
    
    @PostMapping("/cycle/{id}/restock")
    public String restock(@PathVariable int id, @RequestParam int count) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return cycleService.restockBy(id, count);
    }
    
    @PostMapping("/cycle/{id}/removefromcart")
    public void removeFromCart(@PathVariable int id) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		cartService.removeFromCart(id, username);
    }
    @PostMapping("/cycle/removeall")
    public void removeAll() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		cartService.removeAllItemsFromCart(username);
    }
    
    @GetMapping("/cycle/displaycart")
    public List<CartItem> displayCart(){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return cartService.showCartItems(username);
    }
    
    @PostMapping("/cycle/checkout")
	 public void checkout(@RequestBody  Map<String,Integer> borrowedDays) {
    	System.out.println(borrowedDays);
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
	    cartService.checkout(username, borrowedDays);
	    
	    }
    @GetMapping("/cycle/checkedOutCycles")
    public List<Cart> checkedOutCycles(){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
    	return cartService.returnCheckoutItems(username);
    }
    
    @PostMapping("/cycle/{cartId}/{cycleId}/return")
    public void returnCycles(@PathVariable long cycleId, @PathVariable int cartId) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		cartService.returnCycle(username,cartId,cycleId);
    	
    }
    
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {

        try {

            if (userRepository.existsByName(user.getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }

    }
    
    private boolean userMatchesPassword(User user, String password) {
        return user.getPassword().equals(password);
    }
}
