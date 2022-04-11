package com.example.pentago;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GameManager {

    protected Board board;
    protected int currentPlayer;
    private GameUI gameUI;

    public GameManager(GameUI _gameUI)
    {
        this.gameUI = _gameUI;
        this.board = new Board(); // יוצרים את הלוח
        this.currentPlayer = 1;
        gameUI.markPlayer(currentPlayer);
    }

    public int getValue(int row, int col)
    {
        return this.board.getValue(row,col);
    }

    public boolean makeMove(int shura, int amuda)// i,k
    {
        boolean ifDidMove= board.makeMove(shura,amuda, currentPlayer);// מנסה לעשות את המהלך
        if(ifDidMove==true)// בודק האם המהלך נעשה
        {
            gameUI.drawBoard(board);
            gameUI.afterPress();
            return true;
        }
        return false;
    }

    public void saveGame(){
        try {
            FileOutputStream fos = new FileOutputStream("a file");
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);

            writer.append(this.board.toString() + ";");
            writer.append(this.currentPlayer + ";");
            writer.append(this.gameUI.getPlayers());

            writer.close();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(){
        String strLine, board="", currentPlayer="1", players="misho.mishoacher";
        String [] fields;
        try {
            FileInputStream fis = new FileInputStream("a file");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);

            strLine = reader.readLine();
            if(strLine!=null){
                fields = strLine.split(";");
                board = fields[0];
                currentPlayer = fields[1];
                players = fields[2];
            }
            reader.close();
            isr.close();
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.board = new Board(board);
        this.currentPlayer = Integer.parseInt(currentPlayer);
        this.gameUI.setPlayers(players);
    }


    public void restart()
    {
        board.initBoard();
        currentPlayer = 1;
        gameUI.drawBoard(board);
        gameUI.markPlayer(currentPlayer);
    }

    public boolean turnBoard(int boardChosenToTurn, int turnDirection){
        board.turnBoard(boardChosenToTurn, turnDirection);
        boolean didWin =board.Checkwin(currentPlayer);
        gameUI.drawBoard(board);
        currentPlayer = currentPlayer*-1;// מחליפה את השחקן
        gameUI.markPlayer(currentPlayer);

        return didWin;
    }
}
