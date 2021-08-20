package com.gshp.thirtybubble;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gshp.bubbles.BubbleLayout;
import com.gshp.bubbles.BubblesManager;
import com.nex3z.notificationbadge.NotificationBadge;

public class MainActivity extends AppCompatActivity {

    private BubblesManager bubblesManager;
    private NotificationBadge mBadge;

    private int MY_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBubble();

        Button btnAdd = findViewById(R.id.btnAddBubble);
        btnAdd.setOnClickListener(view -> addNewBubble());

        //Check permission
        if(Build.VERSION.SDK_INT >= 23)
        {
            if(!Settings.canDrawOverlays(MainActivity.this))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:"+getPackageName()));
                startActivityForResult(intent,MY_PERMISSION);
            }
        }
        else{
            Intent intent = new Intent(MainActivity.this, Service.class);
            startService(intent);
        }
    }

    private void initBubble() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_remove)
                .setInitializationCallback(this::addNewBubble).build();
        bubblesManager.initialize();
    }

    private void addNewBubble() {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.bubble_layout,null);
        mBadge = bubbleView.findViewById(R.id.count);
        mBadge.setNumber(88);

        bubbleView.setOnBubbleRemoveListener(bubble -> {
            Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
        });

        bubbleView.setOnBubbleClickListener(bubble -> {
            bubblesManager.removeBubble(bubbleView);
            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
        });

        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView,60,20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }
}
