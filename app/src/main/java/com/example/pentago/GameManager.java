package com.example.pentago;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class GameManager {
    private final String saveFileName ="saveFile";
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
            FileOutputStream fos = gameUI.openFileOutput(saveFileName, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            String boardString = this.board.toString();
            writer.append(boardString + ";");
            String currentPlayerString = String.valueOf(this.currentPlayer);
            writer.append(currentPlayerString + ";");
            String players = this.gameUI.getPlayers();
            writer.append(players +"\n");

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
            FileInputStream fis = gameUI.openFileInput(saveFileName);
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
        this.gameUI.drawBoard(this.board);
        this.gameUI.markPlayer(this.currentPlayer);
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
