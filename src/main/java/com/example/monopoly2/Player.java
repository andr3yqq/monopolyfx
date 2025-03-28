package com.example.monopoly2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Player {
    String name;
    LinkedList<Board> ownedPlaces = new LinkedList<Board>();
    int money;
    int position;
    boolean isBot;
    int id;
    Iterator<Board> position2 = Game.board.iterator();
    int prisonCount;

    public Player(String Name, int Money, boolean Bot, int Id)
    {
        name = Name;
        money = Money;
        position = 0;
        isBot = Bot;
        id = Id;
        position2.next();
        prisonCount = 0;
    }

    public int RollDice()
    {
        Random random = new Random();
        return random.nextInt(5) + 1;
    }

    public void BuyProperty(Board place)
    {
        place.owner = id;
        ownedPlaces.add(place);
        money -= place.price;
    }

    public void SellProperty(Board place)
    {
        place.owner = 0;
        ownedPlaces.remove(place);
        money += (place.price)/2;
    }

    public boolean DoesPlayerWon()
    {
        return money >= Game.moneyToWin;
    }

    public boolean DoesPlayerLose()
    {
        return money < 0;
    }

    public void ResetOwnedBoards(LinkedList<Board> board)
    {
        while (!ownedPlaces.isEmpty())
        {
            board.get(board.indexOf(ownedPlaces.getFirst())).owner = 0;
            ownedPlaces.removeFirst();
        }
    }
}
