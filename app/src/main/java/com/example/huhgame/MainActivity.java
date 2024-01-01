package com.example.huhgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Random;

class Event{
    public String name, description;
    public int addbonus = 0, video, sound, timesbonus = 1;
    public Event(String name) {
        this.name = name;
    }
}

public class MainActivity extends AppCompatActivity {
    AppDatabase db;
    UserDao userDao;
    User player;

    int cats_limit = 3, events_limit = 3;
    Cat[] cats = new Cat[101];
    Event[] events = new Event[events_limit];
    int huh = 0, cat = 0, current = 0;
    int[] draw_price = {0, 100, 300, 2500, 5000, 7000, 9000};
    int takecare_price = 120;

    boolean debug = false, loan = false, die = false;
    private VideoView videoView;
    MediaPlayer bgm;

    TextView name, description, value, price;
    ImageView left, right, takecare, draw;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            playOriginalVideo();
            cat = 1;
            bgm.start();
            updatePrice();
            updateHuhCount(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {
            cats[0] = new Cat("Huh 貓");
            cats[0].description = "Huh 貓是一隻很可愛的貓";
            cats[0].value = 1;
            cats[0].bonus = 1;
            cats[0].available = true;
            cats[0].video_wait = R.raw.huh;
            cats[0].video_click = R.raw.click;
            cats[0].img = R.drawable.cat0;

            cats[1] = new Cat("喝水貓");
            cats[1].description = "喝水貓 會咕嚕咕嚕的喝水";
            cats[1].value = 3;
            cats[1].video_wait = R.raw.wcat_wait;
            cats[1].video_click = R.raw.wcat_click;
            cats[1].img = R.drawable.cat1;

            cats[2] = new Cat("Happy 貓");
            cats[2].description = "Happy 貓，他什麼都不會，只會happy happy happy";
            cats[2].value = 10;
            cats[2].video_wait = R.raw.happy_wait;
            cats[2].video_click = R.raw.happy_click;
            cats[2].img = R.drawable.cat2;

            cats[100] = new Cat("Huh 會一直盯著你");
            cats[100].description = "小貓沒錢吃飯，餓死在了你面前 :)";
            cats[100].value = -44444;
            cats[100].video_wait = R.raw.huh;
            cats[100].video_click = R.raw.huh;

            events[0] = new Event("幫貓貓按摩");
            events[0].description = " 按摩，貓貓很開心\n每次互動金幣 +1";
            events[0].addbonus = 1;
            events[0].video = R.raw.massage;
            events[0].sound = R.raw.good;

            events[1] = new Event("一起吃蛋糕");
            events[1].description = " 一起幫多啦A夢慶生!\n互動金幣數量加倍!";
            events[1].timesbonus = 2;
            events[1].video = R.raw.doraemon;
            events[1].sound = R.raw.good;

            events[2] = new Event("有小貓向你借錢");
            events[2].video = R.raw.loan;
            events[2].sound = R.raw.loansound;

            //db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "database").build();
            db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database")
                    .fallbackToDestructiveMigration()
                    .build();
            userDao = db.userDao();

        }
        bgm = MediaPlayer.create(MainActivity.this, R.raw.bgm);
        bgm.start();

        bgm.setLooping(true);
        bgm.setVolume(0.3f, 0.3f);

        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);
        value = (TextView) findViewById(R.id.value);
        price = (TextView) findViewById(R.id.price);
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);

        draw = findViewById(R.id.draw);
        takecare = findViewById(R.id.takecare);
        videoView = (VideoView) findViewById(R.id.huhvideo);

        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                User user = userDao.findUserById(0);
                if (user == null) {
                    user = new User(huh, cat, current, loan, die);
                    user.cats = cats;
                    user.take_careprice = takecare_price;
                    user.id = 0;
                    userDao.insertUser(user);
                }
                player =user;
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                huh = user.huh;
                cat = user.cat;
                current = user.current;
                loan = user.loan;
                die = user.die;
                player = user;
                takecare_price = user.take_careprice;
                cats = user.cats;

                if(cat == 0 && !debug){
                    // goto welcome
                    Intent intent = new Intent(MainActivity.this, Welcome.class);
                    startActivityForResult(intent, 100);
                }
                if(debug){
                    huh = 100000;
                    cat = 1;
                    die = false;
                    loan = false;
                }
                if(die){
                    cat_die();
                }
                updateCat();
                updatePrice();
                updateHuhCount(0);
                playOriginalVideo();

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        playOriginalVideo();  // 播放完毕后重新播放原视频
                    }
                });

                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + cats[current].video_click);
                        videoView.start();
                        updateHuhCount(cats[current].value + cats[current].bonus);
                    }
                });

                takecare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int event_id = 0, randnum;

                        Random rand = new Random();
                        if(loan)
                            randnum = rand.nextInt(100);
                        else
                            randnum = rand.nextInt(130);
                        if(randnum < 10){
                            event_id = 1;
                        }else if(randnum > 115){
                            event_id = 2;
                            loan = true;
                        }else{
                            event_id = 0;
                        }

                        if(current == 2 && cats[current].value == 10){
                            event_id = 1;
                        }
                        //rand the event id


                        if (huh >= takecare_price) {
                            updateHuhCount(-takecare_price);
                            takecare_price += 100;
                            updatePrice();
                            MediaPlayer soundEffect = MediaPlayer.create(MainActivity.this, events[event_id].sound);
                            soundEffect.start();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                            LinearLayout layout = new LinearLayout(MainActivity.this);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout.setPadding(50, 30, 50, 30);
                            layout.setGravity(Gravity.CENTER_HORIZONTAL);

                            VideoView videoView = new VideoView(MainActivity.this);
                            LinearLayout.LayoutParams videoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
                            videoView.setLayoutParams(videoParams);
                            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + events[event_id].video);
                            videoView.setVideoURI(videoUri);
                            layout.addView(videoView);

                            TextView textView = new TextView(MainActivity.this);
                            if(event_id != 1 && event_id != 2)
                                textView.setText("你幫 " + cats[current].name + events[event_id].description);
                            else if(event_id == 2){
                                textView.setText("有小貓向你借錢買貓糧吃\n他答應一定會還你。");
                            }else
                                textView.setText("你和 " + cats[current].name + events[event_id].description);
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextSize(16);
                            layout.addView(textView);
                            builder.setView(layout);

                            builder.setTitle(events[event_id].name);

                            cats[current].bonus += events[event_id].addbonus;
                            cats[current].value *= events[event_id].timesbonus;
                            cats[current].bonus *= events[event_id].timesbonus;
                            updateCat();
                            if(event_id !=2){
                                builder.setPositiveButton("好耶！", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        updateCat();
                                    }
                                });
                            }else{
                                builder.setPositiveButton("借他，小貓一定需要錢", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        huh = 0;
                                        updateHuhCount(-300);
                                        Toast.makeText(MainActivity.this, "你的錢全部都被小貓借走了，還幫你留下了300元的欠債。", Toast.LENGTH_LONG).show();
                                        updateCat();
                                    }
                                });
                                builder.setNegativeButton("不借，一定是騙子", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "你看著小貓虛弱地走了幾步...倒在了你面前，死了。", Toast.LENGTH_LONG).show();
                                        die = true;
                                        cat_die();
                                    }
                                });
                            }


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



                draw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (huh >= draw_price[cat] && cat < cats_limit ){
                            updateHuhCount(-draw_price[cat]);

                            MediaPlayer soundEffect = MediaPlayer.create(MainActivity.this, R.raw.draw);
                            soundEffect.start();

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                            LinearLayout layout = new LinearLayout(MainActivity.this);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout.setPadding(50, 30, 50, 30);  // 根据需要调整内边距

                            ImageView imageView = new ImageView(MainActivity.this);
                            imageView.setImageResource(cats[cat].img); // 替换为你的图片资源
                            imageView.setAdjustViewBounds(true);
                            imageView.setMaxHeight(500);  // 根据需要调整最大高度
                            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            layout.addView(imageView);

                            TextView textView = new TextView(MainActivity.this);
                            textView.setText("您得到了 " + cats[cat].name); // 替换为奖品说明
                            textView.setGravity(Gravity.CENTER_HORIZONTAL);
                            textView.setTextSize(16);
                            layout.addView(textView);

                            builder.setView(layout);

                            builder.setTitle("獲得了新貓貓！");
                            builder.setPositiveButton("開心的收下", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(cat == 2){
                                        Toast.makeText(MainActivity.this, "您可以使用貓咪左右的箭頭，來切換不同貓貓。", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            current = cat;
                            cat += 1;
                            updateCat();
                            updatePrice();
// 显示对话框
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });

                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(current > 0){
                            current -= 1;
                        }else{
                            current = cat - 1;
                        }
                        updateCat();
                    }
                });
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(current < cat - 1){
                            current += 1;
                        }else{
                            current = 0;
                        }
                        updateCat();

                    }
                });
            }

            private void cat_die() {
                TextView alert = findViewById(R.id.alert);
                alert.setText("壞結局：你 殺 死 了 一 隻 貓 。");
                bgm.stop();
                bgm = MediaPlayer.create(MainActivity.this, R.raw.bgm2);
                bgm.setLooping(true);
                bgm.setVolume(1f, 1f);
                bgm.start();
                ImageView bg = findViewById(R.id.bg);
                bg.setImageResource(R.drawable.bg2);
                updateCat();
                left.setVisibility(View.INVISIBLE);
                right.setVisibility(View.INVISIBLE);
                // set red
                name.setTextColor(0xffff0000);
                description.setTextColor(0xffff0000);
                // set invisible
                value.setVisibility(View.INVISIBLE);
                current = 100;

                draw_price = new int[]{44444444,44444444,44444444,44444444,44444444,44444444,44444444};
                takecare_price = 44444444;
                updateCat();
                huh = -44444;
                updateHuhCount(0);
                updatePrice();
            }
        }.execute();
    }

    private void playOriginalVideo() {
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + cats[current].video_wait);
        videoView.start();
    }

    private void updateHuhCount(int cnt) {
        huh += cnt;
        TextView huhcount = (TextView) findViewById(R.id.huhcount);
        huhcount.setText(huh+"");
        if (huh < takecare_price) {
            takecare.setImageResource(R.drawable.notakecare);
        }else{
            takecare.setImageResource(R.drawable.takecare);
        }
        if (huh < draw_price[cat]) {
            draw.setImageResource(R.drawable.nodraw);
        }else{
            draw.setImageResource(R.drawable.draw);
        }
        saveUser(player);
    }

    private void updateCat(){
        name.setText("[" + cats[current].name + "]");
        description.setText(cats[current].description);
        value.setText("每次摸他，他會給你 " + cats[current].value + " + " + cats[current].bonus + " 金幣，謝謝貓貓");
        saveUser(player);
    }

    private void updatePrice() {
        price.setText("互動 " + takecare_price + " 元\n抽獎 " + draw_price[cat] + "元");
        saveUser(player);
    }

    private void saveUser(User user) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... users) {
                if (users == null || users.length == 0 || users[0] == null) {
                    // User 對象為 null，處理錯誤情況
                    return null;
                }
                user.cat = cat;
                user.current = current;
                user.huh = huh;
                user.die = die;
                user.loan = loan;
                user.cats = cats;
                user.take_careprice = takecare_price;
                userDao.updateUser(users[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // 更新完成後的處理，例如提示用戶
            }
        }.execute(user);
    }
}


