package com.example.monopoly2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Board {
    String name;
    char type; // ?, house, casino
    int collection; // food, hotel etc
    int owner;
    int price;
    int tax;
    int baseTax;
    int level;
    int id;
    char displayPos;

    public Board(String Name,char Type, int Street, int Price, int Tax, int Id, char dPos) {
        name = Name;
        type = Type;
        collection = Street;
        owner = 0;
        price = Price;
        baseTax = Tax;
        level = 1;
        tax = baseTax * level;
        id = Id;
        displayPos = dPos;
    }

    public Board(@JsonProperty("name") String Name,@JsonProperty("type") char Type,@JsonProperty("collection") int Street,@JsonProperty("owner") int Owner,@JsonProperty("price") int Price,@JsonProperty("tax") int Tax,@JsonProperty("baseTax") int BaseTax,@JsonProperty("level") int Level,@JsonProperty("id") int Id, @JsonProperty("displayPos") char dPos)
    {
        name = Name;
        type = Type;
        collection = Street;
        owner = Owner;
        price = Price;
        tax = Tax;
        baseTax = BaseTax;
        level = Level;
        id = Id;
        displayPos = dPos;
    }

    public String getName()
    {
        return name;
    }
    public char getType()
    {
        return type;
    }
    public int getCollection()
    {
        return collection;
    }
    public int getOwner()
    {
        return owner;
    }
    public int getPrice()
    {
        return price;
    }
    public int getTax()
    {
        return tax;
    }
    public int getBaseTax()
    {
        return baseTax;
    }
    public int getLevel()
    {
        return level;
    }
    public int getId()
    {
        return id;
    }
    public char getDisplayPos() {return displayPos;}
}