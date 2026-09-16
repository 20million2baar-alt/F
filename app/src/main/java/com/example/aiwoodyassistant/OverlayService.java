package com.example.aiwoodyassistant;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverlayService extends Service {
    private WindowManager wm;
    private LinearLayout box;

    @Override public void onCreate() {
        super.onCreate();
        wm=(WindowManager)getSystemService(WINDOW_SERVICE);

        box=new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(18,14,18,14);
        box.setBackgroundColor(Color.WHITE);

        TextView t=new TextView(this);
        t.setText("AI Woody");
        t.setTextSize(19);
        t.setTextColor(Color.BLACK);

        TextView status=new TextView(this);
        status.setText("Live assistant ready\nScreen analysis: ready");
        status.setTextColor(Color.DKGRAY);

        Button analyze=new Button(this);
        analyze.setText("Analyze current screen");
        analyze.setOnClickListener(v -> status.setText(
            "Suggestion mode\nCapture received.\nConnect a vision model to identify the board and pieces."));

        Button close=new Button(this);
        close.setText("Close");
        close.setOnClickListener(v -> stopSelf());

        box.addView(t); box.addView(status); box.addView(analyze); box.addView(close);

        WindowManager.LayoutParams p=new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
        p.gravity=Gravity.TOP|Gravity.END; p.x=18; p.y=120;
        wm.addView(box,p);
    }

    @Override public void onDestroy(){ if(box!=null) wm.removeView(box); super.onDestroy(); }
    @Override public IBinder onBind(Intent i){ return null; }
}
