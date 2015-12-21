package com.example.kgreen.wallpaper;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class WallpaperScreen extends AppCompatActivity implements View.OnClickListener {


    public static int[] imageId = new int[]{
            R.drawable.download1,
            R.drawable.download2,
            R.drawable.download3,
            R.drawable.download4,
            R.drawable.download5,
            R.drawable.download6,
            R.drawable.download7,
            R.drawable.download8,
            R.drawable.download9,
            R.drawable.download10,
            R.drawable.download11,
            R.drawable.download12,
            R.drawable.download13,
            R.drawable.download14
    };

    private Button share;
    private Button save;
    private TextView textView2;
    private TextView downloadedPic;
    private Button github;
    private ListView listView;
    private String picPathData;
    SwipeRefreshLayout mSwipeRefreshLayout;


    private int Pos;

    Context context = this;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_screen);

        downloadedPic = (TextView) findViewById(R.id.downloadedPic);
        textView2 = (TextView) findViewById(R.id.textView2);
        github = (Button) findViewById(R.id.github);
        share = (Button) findViewById(R.id.share);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        share.setOnClickListener(this);
        github.setOnClickListener(this);

        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });


        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.black,
                R.color.blue,
                R.color.pink,
                R.color.purple,
                R.color.orange,
                R.color.green);


        List<HashMap<String, Integer>> aList = new ArrayList<HashMap<String, Integer>>();

        for (int i = 0; i < 14; i++) {
            HashMap<String, Integer> hm = new HashMap<String, Integer>();
            hm.put("wallpaper", imageId[i]);
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"wallpaper"};

        // Ids of views in listview_layout
        final int[] to = {R.id.imageView};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.image, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = (ListView) findViewById(R.id.listView);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {

                    wallpaperManager.setResource(imageId[position]);
                    wallpaperManager.suggestDesiredDimensions(1800, 1250);


                    textView2.setBackgroundColor(ContextCompat.getColor(context, R.color.notification));
                    textView2.setText("Your wallpaper has been changed!!");
                    startTimer();

                    Pos = position;
                    position = Pos;

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {

        if (v == share) {
            if (Pos >= 1) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("wallpaper/png");
                Uri uri = Uri.fromFile(new File(getFilesDir(), Uri.parse("android.resource://com.example.kgreen.wallpaper/drawable/download" + Pos).toString()));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());
                startActivity(Intent.createChooser(shareIntent, "Share wallpaper using"));
            } else {

                new AlertDialog.Builder(this)
                        .setMessage("You cannot share a wallpaper because you have not selected one yet.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        }
        if (v == github) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/kgreenlsu"));
            startActivity(intent);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wallpaper_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            sendNotification("");
            return true;
        }

        if (id == R.id.save_image) {
            handleDownload();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    public void handleDownload(){

        if(Pos >= 0) {
            new AlertDialog.Builder(this)
                    .setMessage("Would you like to download the wallpaper you recently selected?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.download1);

                            switch(Pos){

                                case 0:bm = BitmapFactory.decodeResource(getResources(),R.drawable.download1);
                                    break;

                                case 1: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download2);
                                    break;

                                case 2: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download3);
                                    break;

                                case 3: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download4);
                                    break;

                                case 4: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download5);
                                    break;

                                case 5: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download6);
                                    break;

                                case 6: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download7);
                                    break;

                                case 7: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download8);
                                    break;

                                case 8: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download9);
                                    break;

                                case 9: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download10);
                                    break;

                                case 10: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download11);
                                    break;

                                case 11: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download12);
                                    break;

                                case 12: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download13);
                                    break;

                                case 13: bm = BitmapFactory.decodeResource(getResources(),R.drawable.download14);
                                    break;


                            }

                            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

                            String randomStringForPic = "";
                            int length = 12;
                            randomString(length);
                            randomStringForPic = randomString(length);
                            picPathData = randomStringForPic;


                            File file = new File(extStorageDirectory, picPathData + ".png");
                            FileOutputStream outStream;

                            try {
                                outStream = new FileOutputStream(file);
                                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                                outStream.flush();
                                outStream.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Uri imageUri = Uri.fromFile(file);


                            Animation anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(3000); //You can manage the blinking time with this parameter
                            anim.setStartOffset(100);
                            anim.setRepeatMode(Animation.REVERSE);
                            downloadedPic.startAnimation(anim);
                            Toast.makeText(getApplicationContext(), "Your wallpaper has been downloaded",
                                    Toast.LENGTH_LONG).show();


                            //Send a broadcast so that the image that was just taken is saved to the users SD card
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
        else{
            new AlertDialog.Builder(this)
                    .setMessage("You cannot download a wallpaper because you have not selected one yet.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }




    private void sendNotification(String text) {

        // create the intent for the notification
        Intent notificationIntent = new Intent(this, WallpaperScreen.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // create the pending intent
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, flags);

        // create the variables for the notification
        int icon = R.drawable.notify;
        //CharSequence tickerText = "Congrats! " + firstName + " You have  a match!" ;
        CharSequence tickerText = "Images may be subject to copyright";
        CharSequence contentTitle = "Images may be subject to copyright";
        CharSequence contentText = text;

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        // create the notification and set its data
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(icon)
                        .setTicker(tickerText)
                        .setSound(alarmSound)
                        .setContentTitle(contentTitle)
                        .setVibrate(new long[]{1000, 1000})
                        .setContentText(contentText)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

        // display the notification
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        final int NOTIFICATION_ID = 1;
        manager.notify(NOTIFICATION_ID, notification);
    }


    @Override
    public void onBackPressed() {
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "WallpaperScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.kgreen.wallpaper/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "WallpaperScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.kgreen.wallpaper/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private void startTimer() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Button2();
                    }
                },
                3000
        );
    }

    public void Button2() {
        textView2.post(new Runnable() {
            public void run() {
                textView2.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                textView2.setText("Press an image to make it your background");

            }
        });
    }


    public String randomString(int length) {

        //Generates a random name for the image that the user has taken
        //This is the alphabet that I will be using for the name
        final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();


        //Use a string builder to build a sequence of random numbers based on the length that is passed in the parameter
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
