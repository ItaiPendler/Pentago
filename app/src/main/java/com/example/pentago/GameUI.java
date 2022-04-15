package com.example.pentago;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameUI extends AppCompatActivity implements View.OnClickListener {
    private final int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    private final int MY_PERMISSIONS_REQUEST_SMS_SEND = 20;

    SMSReceiver smsRcv;

    // יוצרים מערך דו מימדי של הכפתורים שעל המסך
    // (הכפתורים בלוח משחק)
    private Button[][] caftorim = new Button[Board.MisparShurot][Board.MisparAmudot];
    private GameManager GameMgr;
    private String name1, name2;

    // 1 לימין למעלה
    // 2 לשמאל למעלה
    // 3 לימין למטה
    // 4 לשמאל למטה
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

        smsRcv = new SMSReceiver(this);

        // בודקים האם יש לנו את היכולת לקבל SMS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                // אם לא, נבקש אותה
                this.requestPermissions(
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
            }


            // בודקים האם יש לנו את היכולת לשלוח SMS
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // אם לא, נבקש אותה
                this.requestPermissions(
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SMS_SEND);
            }
        }

        initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter inf = new IntentFilter();
        inf.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsRcv, inf);
        Log.d("SMSReceiver Activity", "Registered");
    }


    // פעולה שמופעלת בכל פעם שיש תשובה על יכולות
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // האם ענו על בקשה לקבלת SMS
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            // אם כן, האם נתנו לנו לקבל SMS
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 😊 כן! נתנו לנו
                Toast.makeText(this, "SMS Receive Permissions granted!", Toast.LENGTH_LONG).show();
            } else {
                // לא נתנו לנו 😢
                Toast.makeText(this, "SMS Receive Permissions denied, SMS will be ignored!", Toast.LENGTH_LONG).show();
            }
        }

        // האם ענו לנו על בקשה לשליחת SMS
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_SEND) {

            // אם כן, האם נתנו לנו?
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // כן! נתנו לנו 😊
                Toast.makeText(this, "SMS Send Permissions granted!", Toast.LENGTH_LONG).show();
            } else {
                // לא נתנו לנו ☹
                Toast.makeText(this, "SMS Send Permissions denied, SMS will be ignored!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() // הפעולה האחרונה שנקראת במסך, סוגרת את המסך
    {
        unregisterReceiver(smsRcv);
        this.smsRcv = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idNivchar = item.getItemId();
        if (idNivchar == R.id.restart) {//האם כפתור האיפוס נלחץ?
            GameMgr.restart();
        }
        if (idNivchar == R.id.animation) {// האם כפתור הניצון נלחץ?
            Intent intent = new Intent(this, MyAnimation.class);// אם כן, לעבור למסך הניצחון
            startActivity(intent);
        }
        if (idNivchar == R.id.save) {
            this.GameMgr.saveGame();
        }
        if (idNivchar == R.id.load) {
            this.GameMgr.loadGame();
        }
        return super.onOptionsItemSelected(item);
    }

    public void hashbetKaftor() {
        for (int sh = 0; sh < Board.MisparShurot; sh++) {
            for (int am = 0; am < Board.MisparAmudot; am++) {
                String stringId = "button" + sh + "_" + am;// יוצרים את התבנית של buttoni_k

                int id = getResources().getIdentifier(stringId, "id", getPackageName());
                Button myButton = findViewById(id); // מוצא את הכפתור עצמו
                myButton.setEnabled(false);// משבית את הכפורים אחד אחד בלולאה

            }
        }
    }

    public void hafelKaftor()
    {
        for (int sh = 0; sh < Board.MisparShurot; sh++) {
            for (int am = 0; am < Board.MisparAmudot; am++) {
                String stringId = "button" + sh + "_" + am;// יוצרים את התבנית של buttoni_k

                int id = getResources().getIdentifier(stringId, "id", getPackageName());
                Button myButton = findViewById(id); // מוצא את הכפתור עצמו
                myButton.setEnabled(true);// מפעיל את הכפורים אחד אחד בלולאה

            }
        }
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
        if(didMakeMove)
        {
            hashbetKaftor();
        }


    }

    public void afterPress() {
        findViewById(R.id.boardButtons).setVisibility(View.VISIBLE);//חשיפת הכפתורים לבחירת לוח לסיבוב
    }

    public void chooseBoardToTurn(View v) {
        findViewById(R.id.boardButtons).setVisibility(View.GONE);//העלמת הכפתורים לבחירת לוח לסיבוב

        int chosenButtonId = v.getId();


        if (chosenButtonId == R.id.b1) { // בודקים איזה כפתור נבחר
            // 1 לסיבוב הלוח שנמצא בצד ימין למעלה
            this.boardChosenToTurn = 1;
        } else if (chosenButtonId == R.id.b2) {
            this.boardChosenToTurn = 2;// 2 לסיבוב הלוח שנמצא בצד שמאל למעלה
        } else if (chosenButtonId == R.id.b3) {
            this.boardChosenToTurn = 3;// 3 לסיבוב הלוח שנמצא בצד ימין למטה
        } else if (chosenButtonId == R.id.b4) {
            this.boardChosenToTurn = 4; // 4 לסיבוב הלוח שנמצא בצד שמאל למטה
        }
        findViewById(R.id.directionButtons).setVisibility(View.VISIBLE); // חשיפת הכפתורים לבחירת כיוון סיבוב הלוח
    }

    public void turnBoard(View v) { // סיבוב הלוח
        findViewById(R.id.directionButtons).setVisibility(View.GONE); // העלמת הכפתורים לבחירת כיוון סיבוב הלוח
        boolean didWin;

        int chosenButtonId = v.getId();
        if (chosenButtonId == R.id.coutner_clockwise) {// בודק איזה כפתור נבחר
            didWin = this.GameMgr.turnBoard(boardChosenToTurn, -1);//סיבוב הלוח נגד כיוון השעון (-1) ובדיקת ניצחון
        } else {
            didWin = this.GameMgr.turnBoard(boardChosenToTurn, 1); // סיבוב הלוח עם כיוון השעון (1) ובדיקת ניצחון בelse
        }

        if (didWin == true) {// בדיקה האם ניצחנו
            Intent intent = new Intent(this, MyAnimation.class);// אם כן, לעבור למסך הניצחון
            startActivity(intent);
        }
        hafelKaftor();
    }

    public String getPlayers() {
        return this.name1 + "." + this.name2;// מחזירים את השמות של השחקנים מופרדים בנקודה
    }

    public void setPlayers(String players) {
        String[] playerArray = players.split("\\.");// נפצל לפי נקודה (צריך את הסלאש כדי להגיד שזה נקודה ולא המשמעות בתכנות)
        this.name1 = playerArray[0];
        this.name2 = playerArray[1];

        // מעדכן את המסך בשמות החדשים
        TextView playerName1 = findViewById(R.id.playerName1);
        playerName1.setText(name1);
        TextView playerName2 = findViewById(R.id.playerName2);
        playerName2.setText(name2);
    }

}