package com.example.aiwoodyassistant;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int CAPTURE_REQUEST = 1001;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(36, 50, 36, 36);

        TextView title = new TextView(this);
        title.setText("AI Woody Assistant");
        title.setTextSize(28);
        title.setTextColor(Color.BLACK);

        TextView info = new TextView(this);
        info.setText("\n1. Allow overlay permission.\n2. Allow screen capture.\n3. Start the floating assistant.\n\nThe assistant can inspect captured frames and show manual placement suggestions. It never auto-taps the game.");
        info.setTextSize(16);

        Button overlay = new Button(this);
        overlay.setText("1 • Allow Overlay");
        overlay.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this))
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    android.net.Uri.parse("package:" + getPackageName())));
        });

        Button capture = new Button(this);
        capture.setText("2 • Allow Screen Capture");
        capture.setOnClickListener(v -> {
            MediaProjectionManager m = (MediaProjectionManager)getSystemService(MEDIA_PROJECTION_SERVICE);
            startActivityForResult(m.createScreenCaptureIntent(), CAPTURE_REQUEST);
        });

        Button start = new Button(this);
        start.setText("3 • Start AI Woody");
        start.setOnClickListener(v -> {
            if (Settings.canDrawOverlays(this))
                startService(new Intent(this, OverlayService.class));
        });

        l.addView(title); l.addView(info); l.addView(overlay); l.addView(capture); l.addView(start);
        setContentView(l);
    }

    @Override protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (request == CAPTURE_REQUEST && result == RESULT_OK && data != null) {
            Intent i = new Intent(this, CaptureService.class);
            i.putExtra("resultCode", result);
            i.putExtra("data", data);
            startService(i);
        }
    }
}
