package com.example.monopoly2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;


public class jsonLoader {

    static File currentFile = new File("main.json");
    static ObjectMapper mapper = new ObjectMapper();

    static LinkedList<Board> createBasicBoard()
    {
        LinkedList<Board> board = new LinkedList<Board>();
        board.add(new Board("Start",'S',0,0,1000, 0,'t'));
        board.add(new Board("Red1",'H',1,600,60, 1,'t'));
        board.add(new Board("RandomRed", 'R',0,0,0, 2,'t'));
        board.add(new Board("Red2",'H',1,800,80, 3,'t'));
        board.add(new Board("Red3",'H',1,800,80,4,'t'));
        board.add(new Board("RandomRed2", 'R',0,0,0,5,'t'));
        board.add(new Board("Orange1",'H',2,1000,100,6,'t'));
        board.add(new Board("RandomOrange", 'R',0,0,0,7,'t'));
        board.add(new Board("Orange2",'H',2,1200,120,8,'t'));
        board.add(new Board("Orange3",'H',2,1200,120,9,'t'));
        board.add(new Board("Prison", 'P', 0, 0, 0,10,'t'));
        board.add(new Board("Pink1",'H',3,1400,140,11,'r'));
        board.add(new Board("RandomPink", 'R',0,0,0,12,'r'));
        board.add(new Board("Pink2",'H',3,1600,160,13,'r'));
        board.add(new Board("Pink3",'H',3,1600,160,14,'r'));
        board.add(new Board("RandomPink2", 'R',0,0,0,15,'r'));
        board.add(new Board("Yellow1",'H',4,1800,180,16,'r'));
        board.add(new Board("RandomYellow", 'R',0,0,0,17,'r'));
        board.add(new Board("Yellow2",'H',4,2000,200,18,'r'));
        board.add(new Board("Yellow3",'H',4,2000,200,19,'r'));
        board.add(new Board("Lottery", 'L',0,0,0,20,'b'));
        board.add(new Board("Green1",'H',5,2200,220,21,'b'));
        board.add(new Board("RandomGreen", 'R',0,0,0,22,'b'));
        board.add(new Board("Green2",'H',5,2400,240,23,'b'));
        board.add(new Board("Green3",'H',5,2400,240,24,'b'));
        board.add(new Board("RandomGreen2", 'R',0,0,0,25,'b'));
        board.add(new Board("Blue1",'H',6,2600,260,26,'b'));
        board.add(new Board("RandomBlue", 'R',0,0,0,27,'b'));
        board.add(new Board("Blue1",'H',6,2800,280,28,'b'));
        board.add(new Board("Blue1",'H',6,2800,280,29,'b'));
        board.add(new Board("Free Parking",'F',0,0,0, 30,'b'));
        board.add(new Board("Cyan1",'H',7,3000,300,31,'l'));
        board.add(new Board("RandomCyan", 'R',0,0,0,32,'l'));
        board.add(new Board("Cyan2",'H',7,3200,320,33,'l'));
        board.add(new Board("Cyan3",'H',7,3200,320,34,'l'));
        board.add(new Board("RandomCyan2", 'R',0,0,0,35,'l'));
        board.add(new Board("Brown1",'H',8,3400,340,36,'l'));
        board.add(new Board("RandomBrown", 'R',0,0,0,37,'l'));
        board.add(new Board("Brown2",'H',8,3600,360,38,'l'));
        board.add(new Board("Brown3",'H',8,3600,360,39,'l'));

        jsonLoader.SaveJson(board);
        return board;
    }

    static void SaveJson(LinkedList<Board> board)
    {
        try {
            mapper.writeValue(currentFile,board);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static LinkedList<Board> LoadJson()
    {
        LinkedList<Board> board;
        try {
            ObjectReader reader = mapper.readerForListOf(Board.class);
            board = new LinkedList<Board>(reader.readValue(currentFile));
        } catch (IOException e) {
            currentFile = new File("main.json");
            board = new LinkedList<Board>(createBasicBoard());
            //throw new RuntimeException(e);
        }
        return board;
    }


}
