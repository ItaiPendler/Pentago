package com.example.pentago;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.pentago.GameUI;
import com.example.pentago.R;


public class SMSReceiver extends BroadcastReceiver implements
        AlertDialog.OnClickListener
{
    private Bundle bundle;
    private SmsMessage currentSMS;
    private GameUI ma;

    public SMSReceiver(GameUI ma)
    {
        this.ma = ma;
        Log.d("SMSReceiver", "Constructor");
    }


    @Override
    public void onReceive(Context context, Intent intent)// הפעולה תפעל עבור כל SMS שמגיע לטלפון
    {
        String message = "";
        String senderNo = "";
        System.out.println("do you get it?");
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            bundle = intent.getExtras();
            if (bundle != null)
            {
                Object[] pdu_Objects = (Object[]) bundle.get("pdus");
                if (pdu_Objects != null) {
                    System.out.println("something");
                       for (Object aObject : pdu_Objects)
                    {

                        currentSMS = getIncomingMessage(aObject, bundle);//בונים אובייקט שנוח לעבוד איתו לSMS

                        senderNo = currentSMS.getDisplayOriginatingAddress(); // מקבלים את מספר הטלפון ששלח לנו את ההודעה
                        System.out.println("another thing" + senderNo);
                        message += currentSMS.getDisplayMessageBody(); // לוקח את הטקסט של הSMS ומכניס אותו להודעה
                    }

                    String msg = "קיבלת סמס ממספר: " + senderNo + "\nהתוכן הוא: " + message; // בונים את המסר שנציג בדיאלוג
                    showDialog(context, "קיבלת SMS", msg);//מציגים את הדיאלוג

                    // בודקים אם מותר לנו לשלוח הודעה
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (context.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
                        {
                            SmsManager smsMgr = SmsManager.getDefault();

                            // שולחים הודעה אוטומטית למי ששלח לנו את ההודעה
                            smsMgr.sendTextMessage(senderNo, null, "לא יכול/ה לענות, משחק/ת פנטגו", null, null);
                        }
                        else
                            Log.e("SMSReceiver", "No permissions to send SMS - ignoring");
                    }

                }
            }
        }
    }


    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle)// בונה אובייקט נוח לעבוד איתו של SMS
    {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }

    private void showDialog(Context context, String title, String msg)// בונה ומציגה את הדיאלוג
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setCancelable(true);
        System.out.println("yet anothr thing");
        builder.setPositiveButton("ראיתי, תודה", this);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which)//בלחיצה על הכפתור
    {
        dialog.dismiss(); // הדיאלוג יעלם
    }
}