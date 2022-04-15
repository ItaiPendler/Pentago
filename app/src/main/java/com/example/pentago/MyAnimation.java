package com.example.pentago;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MyAnimation extends AppCompatActivity
{
    // מקבל מסרים מה thread ושולח למסך לציור
    MyHandler myHandler;

    //:מפעיל את הפעולה run
    //סופר מספרים מ1 עד 42

    MyThread myThread;

    ImageView dmutImageView;
    private Drawable [] arrDmuyot;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animation);

        //מוצא את הקופסה שמציגה את התמונה על המסך
        dmutImageView = (ImageView) findViewById(R.id.animationImageView);
        arrDmuyot = new Drawable[42];//מערך של התמונות
        for(int i = 1; i < 42; i++)//עובר על המערך וממלא אותו בתמונות
        {
            String drawableName = "final_firework_frame" + i;
            int resID = getResources().getIdentifier(drawableName, "drawable", getPackageName());
            arrDmuyot[i] = getResources().getDrawable(resID);
        }
        myHandler = new MyHandler(this);//יוצרים את ה handler עם קישור למסך
        startAnim();//מפעילים את האנימציה
    }

    //הפעולה מציירת את הדמות שבתמונה לפי המספר שקיבלנו
    public void drawDmut(int newPictureNumber)
    {
        dmutImageView.setImageDrawable(arrDmuyot[newPictureNumber]);
    }

    public void startAnim()
    {
        if(myThread == null)
        {
            myThread = new MyThread(myHandler);
            myThread.start();
        }
    }
}