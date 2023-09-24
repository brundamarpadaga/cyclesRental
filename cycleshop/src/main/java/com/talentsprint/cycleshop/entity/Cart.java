package com.talentsprint.cycleshop.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> cartItems;

    private int rentalDurationInDays;
    private double totalCost;
    
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;



    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getRentalDurationInDays() {
        return rentalDurationInDays;
    }

    public void setRentalDurationInDays(int rentalDurationInDays) {
        this.rentalDurationInDays = rentalDurationInDays;
    }


    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

}
