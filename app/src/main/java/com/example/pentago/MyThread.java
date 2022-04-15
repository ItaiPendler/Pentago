package com.example.pentago;

import android.os.Bundle;
import android.os.Message;

public class MyThread extends Thread
{
    private boolean stop;
    private MyHandler myHandler;

    public MyThread(MyHandler _mh)
    {
        this.myHandler = _mh;
    }


    public void run()//כשמפעילים thread (הפעלנו בקובץ thread) אז הפעולה בשם הקבוע run רצה
    {
        stop = false;
        int i = 0;
        while(!stop)
        {
            //סופר מספרים מ1 עד 13
            i++;
            // כשזה סיים לעבור על כל 42 המספרים, זה חוזר למספר 1
            if(i == 42)
                i = 1;

            // שולח את המספר הנוכחי ל handler
            sendCountToHandler(i);

            try
            {
                // במרווח של 50 מילי שניות (0.05 שניות)
                this.sleep(50);
            }

            catch(InterruptedException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    private void sendCountToHandler (int newPictureNumber)//שולח את המספר ל handler בעזרת הודעה
    {
        Message msg = new Message();
        Bundle data = msg.getData();
        data.putInt("newPictureNumber", newPictureNumber);
        myHandler.handleMessage(msg);
    }
}
