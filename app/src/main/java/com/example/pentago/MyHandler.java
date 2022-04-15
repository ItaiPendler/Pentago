package com.example.pentago;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyHandler extends Handler
{
    private MyAnimation myAnimation;

    public MyHandler(MyAnimation myAnimation)
    {
        super(); //שומר את הקשר למסך אנימציה
        this.myAnimation = myAnimation;
    }

    public void handleMessage(Message msg)//טיפול בהודעה
    {
        super.handleMessage(msg);
        Bundle data = msg.getData();
        int newPictureNumber = data.getInt("newPictureNumber");// מוציאים מההודעה את המספר שהגיע איתה
        myAnimation.drawDmut(newPictureNumber);//שולח למסך לצייר את התמונה
    }
}
