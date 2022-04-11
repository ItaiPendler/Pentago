package com.example.pentago;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

    // שומר את המשחק לקובץ טקסט שנראה כך:
    // 0.0.0.0.0.0n0.0.0.0.0.0n0.0.0.0.0.0n;1;name1.name2;
    public void saveGame() {
        try { // קוד שעלול לגרום לאפליקציה לקרוס
            FileOutputStream fos = gameUI.openFileOutput(saveFileName, Context.MODE_PRIVATE);// לנסות למצוא את הקובץ שלנו
            OutputStreamWriter osw = new OutputStreamWriter(fos); // בונים אובייקט שיתן לנו לכתוב לקובץ
            BufferedWriter writer = new BufferedWriter(osw); // בונים אובייקט שיכתוב לקובץ

            String boardString = this.board.toString();//הופכים את הלוח למחרוזת שנוכל לשמור בקובץ
            writer.append(boardString + ";");// שומרים את הלוח בקובץ ומוסיפים נקודה פסיק לסימול סוף חלק

            String currentPlayerString = String.valueOf(this.currentPlayer);// הופך את המספר של השחקן הנוכחי למחרוזת שנשמור בקובץ
            writer.append(currentPlayerString + ";");// כותבים את השם של השחקן הנוכחי לקובץ

            String players = this.gameUI.getPlayers();// משיגים את שמות השחקנים במחרוזת שנשמור לקובץ
            writer.append(players + "\n");// כותבים את שמות השחקנים לקובץ
            // אנו כותבים \n בסוף השורה האחרונה כדי להראות שנגמר הקובץ

            writer.close(); //  לשמור את הקובץ
            osw.close(); //  לשמור את הקובץ
            fos.close();// לשמור את הקובץ
            Toast.makeText(this.gameUI, "המשחק נשמר", Toast.LENGTH_SHORT).show();
        } catch (Exception e) { // אילו שגיאות היו
            e.printStackTrace();
            Toast.makeText(this.gameUI, "המשחק לא נשמר", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadGame(){
        String strLine; // חלקי המידע לפני הפיצול
        String board="";// הלוח אם נמחק מהקובץ
        String currentPlayer = "1";// השחקן הנוכחי אם נמחק מהקובץ
        String players="מישהו.מישהו אחר"; // שמות השחקנים חלופיים למקרה שהשמות המקוריים נמחקו
        String [] fields; // חלקי המידע אחרי פיצול
        try { // קוד שעלול לגרום לאפליקציה לקרוס
            FileInputStream fis = gameUI.openFileInput(saveFileName);// נחפש את הקובץ שבו שמרנו במצב קריאה
            InputStreamReader isr = new InputStreamReader(fis); // בונים אובייקט שיתן לנו לקרוא את הקובץ
            BufferedReader reader = new BufferedReader(isr); // בונים אובייקט שיקרא את הקובץ

            strLine = reader.readLine();
            if(strLine!=null){// אם הקובץ לא ריק
                fields = strLine.split(";"); // תפצל את השורה כל פעם שיש ;
                board = fields[0];//לוח המשחק
                currentPlayer = fields[1];// השחקן הנוכחי
                players = fields[2]; // שמות השחקנים
            }
            reader.close(); // שומר את הקובץ בלי שינויים
            isr.close(); // שומר את הקובץ בלי שינויים
            fis.close(); // שומר את הקובץ בלי שינויים
        }catch (Exception e){// מטפל בשגיאות אם היו
            e.printStackTrace();
        }

        this.board = new Board(board);// מכניסים את הלוח מהקובץ למשחק שלנו
        this.currentPlayer = Integer.parseInt(currentPlayer); // הופכים את מספר השחקן הנוכחי מstring לint
        this.gameUI.setPlayers(players); // מחליפים את שמות השחקנים בשמות מהקובץ
        this.gameUI.drawBoard(this.board);// לצייר מחדש את הלוח
        this.gameUI.markPlayer(this.currentPlayer);// מסמנים את השחקן הנוכחי
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
