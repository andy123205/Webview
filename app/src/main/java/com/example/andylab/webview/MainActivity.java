package com.example.andylab.webview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    SurfaceView surfaceView ;
    SurfaceHolder surfaceHolder;
    Button start,stop;
    TextView txv;
    //WebView wv;
    Handler handler = new Handler();

    Bitmap bp;
    Canvas canvas;
    ImageView myImageView;
    int t = 0;

    Timer timer01 =new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv = (TextView)findViewById(R.id.textView);

        myImageView = (ImageView)findViewById(R.id.imageView);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFixedSize(320, 240);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //在 canvas 畫布上貼圖的三個步驟
                //bp = getBitmapFromURL("http://140.121.137.185:7171/?action=snapshot");


                //1. 鎖住畫布
                //canvas = holder.lockCanvas();
                //2. 在畫布上貼圖
                //canvas.drawBitmap(bp,0,0,null);
                //canvas.drawRGB(255,255,0);
                //3. 解鎖並po出畫布
                //holder.unlockCanvasAndPost(canvas);


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });


        start = (Button)findViewById(R.id.button);
        stop = (Button)findViewById(R.id.button2);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(task, 1000);
                //myImageView. setImageBitmap(getBitmapFromURL("http://140.121.137.185:7171/?action=snapshot"));
                //myImageView.setImageBitmap();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(task);
            }
        });

        //wv = (WebView)findViewById(R.id.wv);
        //wv.loadUrl("http://140.121.137.185:7171");
        //wv.loadUrl("http://140.121.137.185:7171/?action=snapshot");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Runnable task = new Runnable() {
        public void run() {
            //t=t+1;
            //txv.setText(String.valueOf(t));
            handler.postDelayed(this, 33);
            //wv.loadUrl("http://140.121.137.185:7171/?action=snapshot");
            //t = t + 1;
            txv.setText("fps");

            //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    String url = params[0];
                    return getBitmapFromURL(url);
                    //return getBitmapFromURL("http://140.121.137.185:7171/?action=snapshot");
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    myImageView.setImageBitmap(result);
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawBitmap(result, 0, 0, null);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    super.onPostExecute(result);
                }
            }.execute("http://140.121.137.185:7171/?action=snapshot");


        }
    };

    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
