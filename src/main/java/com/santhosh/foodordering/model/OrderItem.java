package com.santhosh.foodordering.model;

import jakarta.persistence.*;

@Entity
@Table(name="orderitems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long orditemid;

    @ManyToOne
    private Order order;

    @ManyToOne
    private FoodItem fooditem;

    private int quantity;
    private double price;

    public Long getOrditemid() {
        return orditemid;
    }

    public void setOrditemid(Long orditemid) {
        this.orditemid = orditemid;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public FoodItem getFooditem() {
        return fooditem;
    }

    public void setFooditem(FoodItem fooditem) {
        this.fooditem = fooditem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
