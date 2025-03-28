package com.example.monopoly2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Game{
    public boolean IsGameActive = false;
    static LinkedList<Board> board = new LinkedList<Board>(); // ciklinis TS
    static Player[] players; // visi zaidejai
    static LinkedList<Player> queue = new LinkedList<Player>(); // eile
    static int startMoney = 15000;
    static int moneyToWin = 20000;
    static int prisonPosition = 10;
    static Board prison;
    static int playerCount = 3;
    GameController gameController;


    public Board GetPlaceByPosition(Board curplace)
    {
        return board.get(board.indexOf(curplace));
    }

    public static Iterator<Board> MoveToPlace(int pos, Iterator<Board> curplace)
    {
        for (int i = 0; i < pos - 1; i++)
        {
            if (curplace.hasNext())
            {
                //System.out.println("Board id " + curplace.next().name);
                curplace.next();
            }
            else
            {
                curplace = board.iterator();
                //System.out.println("Board id " + curplace.next().name);
                curplace.next();
            }
        }
        return curplace;
    }

    public Player GetRandomPlaceBuff(Player currentPlayer)
    {
        Random random = new Random();
        int option = random.nextInt(3);
        switch (option) {
            case 0:
            {
                gameController.UpdatePos(currentPlayer.position, prisonPosition, currentPlayer.id);
                currentPlayer.position = prisonPosition;
                currentPlayer.position2 = board.iterator();
                MoveToPlace(prisonPosition+2, currentPlayer.position2);
                currentPlayer.prisonCount++;
                gameController.DisplayText(currentPlayer.name + " goes to prison for 2 turns!");
                break;
            }
            case 1:
            {
                int money = random.nextInt(1000);
                currentPlayer.money -= money;
                gameController.DisplayText(currentPlayer.name + " pays $" + money + " to bank");
                break;
            }
            case 2:
            {
                int money = random.nextInt(1000);
                currentPlayer.money += money;
                gameController.DisplayText(currentPlayer.name + " gets $" + money + " bonus");
                break;
            }

        }
        return currentPlayer;
    }

    public void Lottery(Player currentPlayer)
    {
        if (currentPlayer.money >= 1000)
        {
            Random random = new Random();
            currentPlayer.money -= 1000;
            int money = random.nextInt(5000);
            currentPlayer.money += money;
            gameController.DisplayText(currentPlayer.name + " pays $1000 to play lottery and win $ " + money);
        }
    }

    static void AddPlayer(int id, boolean isBot, String playerName)
    {
        Player player = new Player(playerName, startMoney, isBot, id);
        players[id - 1] = player;
        queue.add(player);
    }

    public void GameTurns()
    {
        Player currentPlayer = queue.getFirst();
        queue.removeFirst();
        Board place;
        gameController.DisplayText(currentPlayer.name + " turn.");
        if (currentPlayer.prisonCount == 1)
        {
            currentPlayer.prisonCount++;
            place = prison;
            gameController.DisplayText(currentPlayer.name + " is in prison " + (3 - currentPlayer.prisonCount) + " turns left");
        }
        else if (currentPlayer.prisonCount == 2)
        {
            currentPlayer.prisonCount = 0;
            place = prison;
            gameController.DisplayText(currentPlayer.name + " is in prison ");
        }
        else
        {
            int[] dice = {1,1};
            if (currentPlayer.isBot)
            {
                dice[0] = currentPlayer.RollDice();
                dice[1] = currentPlayer.RollDice();
            }
            else
            {
                gameController.PlayerThrowDice(currentPlayer,dice);
            }
            gameController.DisplayText("dice1 is " + dice[0]);
            gameController.DisplayText("dice2 is " + dice[1]);
            currentPlayer.position2 = MoveToPlace(dice[0] + dice[1], currentPlayer.position2);
            if (!currentPlayer.position2.hasNext())
            {
                currentPlayer.position2 = board.iterator();
            }
            place = GetPlaceByPosition(currentPlayer.position2.next());
            gameController.DisplayText(currentPlayer.name + " moves to " + place.name);
            gameController.UpdatePos(currentPlayer.position, place.id, currentPlayer.id);
            currentPlayer.position = place.id;
            switch (place.type) {
                case 'H':
                {
                    if (place.owner == 0)
                    {
                        if (currentPlayer.money >= place.price)
                        {
                            if (currentPlayer.isBot)
                            {
                                currentPlayer.BuyProperty(place);
                                gameController.updatePlace(place.id,currentPlayer.id);
                                gameController.DisplayText(currentPlayer.name + " buys " + place.name + " for $" + place.price);
                            }

                            else
                            {
                                gameController.DisplayText("Do you want to buy " + place.name + " for $" + place.price);
                                if (gameController.PlayerProperty(currentPlayer))
                                {
                                    currentPlayer.BuyProperty(place);
                                    gameController.updatePlace(place.id,currentPlayer.id);
                                    gameController.DisplayText(currentPlayer.name + " buys " + place.name + " for $" + place.price);
                                }
                            }
                        }
                    }
                    else if (place.owner != currentPlayer.id)
                    {
                        currentPlayer.money -= place.tax;
                        players[place.owner-1].money += place.tax;
                        gameController.DisplayText(currentPlayer.name + " pays $" + place.tax + " to " +  players[place.owner-1].name);
                    }
                    break;
                }
                case 'R':
                {
                    GetRandomPlaceBuff(currentPlayer);
                    break;
                }
                case 'S':
                {
                    currentPlayer.money += place.tax;
                    break;
                }
                case 'L':
                {
                    Lottery(currentPlayer);
                    break;
                }
                default:
                    break;
            }
        }

        gameController.UpdatePlayerStats(players,playerCount);

        if (currentPlayer.DoesPlayerWon()) {
            gameController.DisplayText(currentPlayer.name + " has won");
            IsGameActive = false;
        }
        else if (currentPlayer.DoesPlayerLose())
        {
            gameController.DisplayText(currentPlayer.name + " has lost");
            for (Board playerPlace : currentPlayer.ownedPlaces)
            {
                gameController.updatePlace(playerPlace.id,0);
            }
            gameController.DeletePos(place.id,currentPlayer.id);
            currentPlayer.ResetOwnedBoards(board);
        }
        else
        {
            queue.addLast(currentPlayer);
        }
        if (IsGameActive && queue.getFirst() == queue.getLast())
        {
            gameController.DisplayText("Game end");
            gameController.DisplayText(queue.getFirst().name + "has won");
            IsGameActive = false;
        }
        players[currentPlayer.id - 1] = currentPlayer;
        gameController.UpdatePlayerStats(players,playerCount);
    }

    public void startGame(String[] PlayerNames, boolean[] PlayerBot, int NumberOfPlayers)
    {
        board = jsonLoader.LoadJson();
        int j = 0;
        while (board.get(j).type != 'P')
            j++;
        prison = board.get(j);
        prisonPosition = j;

        playerCount = NumberOfPlayers;
        players = new Player[playerCount];
        queue.clear();
        for (int i = 0; i < playerCount; i++)
        {
            AddPlayer(i + 1, PlayerBot[i],PlayerNames[i]);
        }

        IsGameActive = true;
        gameController.SetPlayerNames(players,playerCount);
        gameController.UpdatePlayerStats(players,playerCount);
        gameController.loadPlaces(playerCount);
        while (IsGameActive)
        {
            System.out.println();
            GameTurns();
        }
    }

}
