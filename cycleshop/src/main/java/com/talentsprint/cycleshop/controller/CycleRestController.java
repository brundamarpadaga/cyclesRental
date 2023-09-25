package com.talentsprint.cycleshop.controller;


import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

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
    public List<Cycle> all(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        System.out.println(jwt.getClaimAsString("scope"));
        return cycleService.listAvailableCycles();
    }
    
    @PostMapping("/cycle/{id}/addtocart")
    public String addToCart(@PathVariable int id, @RequestParam int count){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return cartService.addToCart(id, count, username);
    }
    
    @PostMapping("/cycle/{id}/restock")
    public String restock(@PathVariable int id, @RequestParam int count) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return cartService.restock(id, count);
    }
    
    @PostMapping("/cycle/{id}/removefromcart")
    public String removeFromCart(@PathVariable int id) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return cartService.removeFromCart(id, username);
    }
    @PostMapping("/cycle/removeall")
    public String removeAll() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return cartService.removeAllItemsFromCart(username);
    }
    
    @GetMapping("/cycle/displaycart")
    public List<CartItem> displayCart(){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return cartService.showCartItems(username);
    }
    
    @GetMapping("/cycle/checkout")
	 public void checkout() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
	    cartService.checkout(username);
	    }
    
    @GetMapping("/registration")
    public String registrationForm(Model model) {
        return "userRegistration";
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

 
    
   

    @GetMapping("/login")
    public String LoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String LoginonSubmit(@RequestParam String username, @RequestParam String password, Model model) {

        Optional<User> user = userRepository.findByName(username);
        if (user != null && userMatchesPassword(user.get(), password)) {
            return "redirect:/restock";
        } else {
            model.addAttribute("error", "Invalid Crudentials");
            return "login";
        }
    }
    private boolean userMatchesPassword(User user, String password) {
        return user.getPassword().equals(password);
    }
}
