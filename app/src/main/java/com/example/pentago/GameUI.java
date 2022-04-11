package com.example.pentago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameUI extends AppCompatActivity implements View.OnClickListener {

    // יוצרים מערך דו מימדי של הכפתורים שעל המסך
    // (הכפתורים בלוח משחק)
    private Button[][] caftorim = new Button[Board.MisparShurot][Board.MisparAmudot];
    private GameManager GameMgr;
    private String name1, name2;

    // 1 לשמאל למעלה
    // 2 לימין למעלה
    // 3 לשמאל למטה
    // 4 לימין למטה
    private int boardChosenToTurn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ui);
        Intent intent = this.getIntent();
        name1 = intent.getStringExtra("name1");
        name2 = intent.getStringExtra("name2");
        TextView playerName1 = findViewById(R.id.playerName1);
        playerName1.setText(name1);
        TextView playerName2 = findViewById(R.id.playerName2);
        playerName2.setText(name2);
        Toast.makeText(this, name1, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, name2, Toast.LENGTH_SHORT).show();
        GameMgr = new GameManager(this);


        initButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idNivchar = item.getItemId();
        switch(idNivchar){
            case R.id.restart:
                break;
            case R.id.save:
                this.GameMgr.saveGame();
                break;
            case R.id.load:
                this.GameMgr.loadGame();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void initButtons()//הפעולה מקשרת בין הכפתורים למסך
    {
        for (int sh = 0; sh < Board.MisparShurot; sh++) {
            for (int am = 0; am < Board.MisparAmudot; am++) {
                String stringId = "button" + sh + "_" + am;// יוצרים את התבנית של buttoni_k

                int id = getResources().getIdentifier(stringId, "id", getPackageName());
                caftorim[sh][am] = findViewById(id); // מוצא את הכפתור עצמו
                caftorim[sh][am].setOnClickListener(this);// השורה הזאת מגדירה שכל לחיצה תהיה מהפעולה של onClick
                final int finalShura = sh;
                final int finalAmuda = am;
//                לחיצה ארוכה על כפתור
                caftorim[sh][am].setOnLongClickListener(view -> {
                    if (GameMgr.getValue(finalShura, finalAmuda) == 0) {
                        return false;
                    }
                    if (GameMgr.getValue(finalShura, finalAmuda) == -1) {
                        Toast.makeText(this, name1, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, name2, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
            }
        }
    }


    public void drawBoard(Board board) {
        for (int sh = 0; sh < Board.MisparShurot; sh++) {
            for (int am = 0; am < Board.MisparAmudot; am++) {
                int plNum = board.getValue(sh, am);
                if (plNum == 1)
                    caftorim[sh][am].setBackgroundResource(R.drawable.whitetile);
                if (plNum == -1)
                    caftorim[sh][am].setBackgroundResource(R.drawable.blacktile);
                if (plNum == 0)
                    caftorim[sh][am].setBackgroundResource(R.drawable.emptytile);
            }
        }
    }

    public void markPlayer(int playerNumber) {
        if (playerNumber == -1) {
            findViewById(R.id.TorButton).setBackgroundColor(Color.BLACK);
        } else {
            findViewById(R.id.TorButton).setBackgroundColor(Color.WHITE);
        }
    }


    @Override
    public void onClick(View view) {
        //buttonI_K
        String buttonName = getResources().getResourceEntryName(view.getId());// מוצא את שם הכפתור הנלחץ

        // I_K
        String lastThree = buttonName.substring(buttonName.length() - 3);// לוקח את שלושת התוים האחרונים

        //[I,K]
        String[] points = lastThree.split("_");// לוקח את i ואת k

        int i = Integer.parseInt(points[0]);// הופך את i למספר
        int k = Integer.parseInt(points[1]);// הופך את k למספר

        boolean didMakeMove = GameMgr.makeMove(i, k);

    }

    public void afterPress() {
        findViewById(R.id.boardButtons).setVisibility(View.VISIBLE);
    }

    public void chooseBoardToTurn(View v) {
        findViewById(R.id.boardButtons).setVisibility(View.GONE);

        int chosenButtonId = v.getId();

        if (chosenButtonId == R.id.b1) {
            this.boardChosenToTurn = 1;
        } else if (chosenButtonId == R.id.b2) {
            this.boardChosenToTurn = 2;
        } else if (chosenButtonId == R.id.b3) {
            this.boardChosenToTurn = 3;
        } else if (chosenButtonId == R.id.b4) {
            this.boardChosenToTurn = 4;
        }
        findViewById(R.id.directionButtons).setVisibility(View.VISIBLE);
    }

    public void turnBoard(View v) {
        findViewById(R.id.directionButtons).setVisibility(View.GONE);
        int chosenButtonId = v.getId();
        boolean didWin;
        if (chosenButtonId == R.id.coutner_clockwise) {
            didWin = this.GameMgr.turnBoard(boardChosenToTurn, -1);
        } else {
            didWin = this.GameMgr.turnBoard(boardChosenToTurn, 1);
        }

        if (didWin == true) {
            Intent intent = new Intent(this, Win.class);
            startActivity(intent);
        }
    }

    public String getPlayers(){
        return this.name1+"."+this.name2;// מחזירים את השמות של השחקנים מופרדים בנקודה
    }

    public void setPlayers(String players){
        String[] playerArray = players.split("\\.");
        this.name1 = playerArray[0];
        this.name2 = playerArray[1];

        TextView playerName1 = findViewById(R.id.playerName1);
        playerName1.setText(name1);
        TextView playerName2 = findViewById(R.id.playerName2);
        playerName2.setText(name2);
    }

}