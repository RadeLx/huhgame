package com.example.huhgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class Welcome extends AppCompatActivity {
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        updateProgress(null);

        ImageView finish = findViewById(R.id.draw);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progress == 6) {
                    TextView coin = (TextView) findViewById(R.id.huhcount);
                    coin.setText("0");

                    MediaPlayer soundEffect = MediaPlayer.create(Welcome.this, R.raw.draw);
                    soundEffect.start();

                    AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);

// 创建 LinearLayout 作为对话框的容器
                    LinearLayout layout = new LinearLayout(Welcome.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.setPadding(50, 30, 50, 30);  // 根据需要调整内边距

// 创建 ImageView 并添加到 LinearLayout
                    ImageView imageView = new ImageView(Welcome.this);
                    imageView.setImageResource(R.drawable.cat0); // 替换为你的图片资源
                    imageView.setAdjustViewBounds(true);
                    imageView.setMaxHeight(500);  // 根据需要调整最大高度
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    layout.addView(imageView);

// 创建 TextView 并添加到 LinearLayout
                    TextView textView = new TextView(Welcome.this);
                    textView.setText("您得到了 Huh 貓"); // 替换为奖品说明
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setTextSize(16);
                    layout.addView(textView);

// 设置 LinearLayout 为 AlertDialog 的内容
                    builder.setView(layout);

// 配置其他 AlertDialog 选项
                    builder.setTitle("獲得了新貓貓！");
                    builder.setPositiveButton("開心的收下", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateProgress(null);
                        }
                    });

// 显示对话框
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        ImageView finish2 = findViewById(R.id.welcome_finish2);
        finish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progress == 12) {
                    MediaPlayer soundEffect = MediaPlayer.create(Welcome.this, R.raw.touch);
                    soundEffect.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);

                    LinearLayout layout = new LinearLayout(Welcome.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.setPadding(50, 30, 50, 30);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);

                    VideoView videoView = new VideoView(Welcome.this);
                    LinearLayout.LayoutParams videoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
                    videoView.setLayoutParams(videoParams);
                    Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.massage);
                    videoView.setVideoURI(videoUri);
                    layout.addView(videoView);

                    TextView textView = new TextView(Welcome.this);
                    textView.setText("你幫你的 Huh 貓按摩，他很開心。\n每次互動金幣 +1"); // 替换为奖品说明
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setTextSize(16);
                    layout.addView(textView);

// 设置 LinearLayout 为 AlertDialog 的内容
                    builder.setView(layout);

// 配置其他 AlertDialog 选项
                    builder.setTitle("幫貓貓按摩");
                    builder.setPositiveButton("好耶！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateProgress(null);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    videoView.start();

                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                        }
                    });

                }
            }
        });
    }
        public void updateProgress (View view){
            MediaPlayer soundEffect = MediaPlayer.create(Welcome.this, R.raw.button01b);
            soundEffect.start();
            String[] tutorial = {
                    "歡迎遊玩HuH！我會一步一步指引你遊戲玩法。",
                    "這是一個照顧貓貓的遊戲",
                    "畫面上方是你擁有的金幣數量",
                    "用金幣可以獲得更多的貓貓",
                    "現在我送你100元，你可以抽取你的第一隻貓貓",
                    "請點擊右下角的抽獎按鈕",
                    "等到遊戲正式開始，你可以點擊貓貓賺錢",
                    "在那之前，",
                    "先跟你介紹一個功能。",
                    "你可以用左下角的按鈕與貓貓互動。",
                    "互動也需要金幣",
                    "這次免費讓你試試看吧！",
                    "你已經了解遊戲玩法了!",
                    "GO"
            };
            TextView tutor = (TextView) findViewById(R.id.tutorial);
            tutor.setText(tutorial[progress]);
            if (progress == 2) {
                TextView coin = (TextView) findViewById(R.id.huhcount);
                coin.setText("0");
            }
            if (progress == 4) {
                TextView coin = (TextView) findViewById(R.id.huhcount);
                coin.setText("100");
            }
            if (progress == 5) {
                Button btn = (Button) findViewById(R.id.next);
                btn.setVisibility(View.INVISIBLE);
            }
            if (progress == 6){
                Button btn = (Button) findViewById(R.id.next);
                btn.setVisibility(View.VISIBLE);
            }
            if (progress == 11){
                Button btn = (Button) findViewById(R.id.next);
                btn.setVisibility(View.INVISIBLE);
            }
            if (progress == 12){
                Button btn = (Button) findViewById(R.id.next);
                btn.setVisibility(View.VISIBLE);
                btn.setText("開始");
            }
            if (progress >= 13){
                finish();
            }
            progress++;
        }

    }
