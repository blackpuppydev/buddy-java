package th.co.ais.genesis.module.buddy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;


import th.co.ais.genesis.module.buddy.blueprint.ResultListener;
import th.co.ais.genesis.module.buddy.blueprint.SpeakCallbackListener;
import th.co.ais.genesis.module.buddy.dialogue.DialogueText;
import th.co.ais.genesis.module.buddy.gms.FaceDetect;
import th.co.ais.genesis.module.buddy.gms.SpeechToText;
import th.co.ais.genesis.module.buddy.gms.TextToSpeechRecognize;
import th.co.ais.genesis.module.buddy.media.PlaySong;

public class MainActivity extends AppCompatActivity {

    private SpeechToText speechToText;
    private DialogueText dialogueText;
    private TextToSpeechRecognize textToSpeechRecognize;

    //UI
    private TextView textView;
    private String key_dialog;
    private ImageView detectFace;
    private VideoView mainShow;

    //Result
    private String textSpeech = "เปิดวีดีโอ";
    private String textDialog;

    private ArrayList<String> for_time;

    private PlaySong playSong;

    //wifi
    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        for_time = new ArrayList<>();
        for_time.add("set time");
        for_time.add("ตั้งเวลา");

        textView = findViewById(R.id.show_text);
        detectFace = findViewById(R.id.imageViewTest);
        mainShow = findViewById(R.id.mainShow);

//        mainShow.setVideoPath();


//        BitmapDrawable drawable = (BitmapDrawable) detectFace.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();

//        startFaceDetect(bitmap);

        //request permission

        key_dialog = end_coding();


//        startDialog();
//        testTTS();
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("onResume");

        startSpeechToText();
    }

    public void startSpeechToText(){

        speechToText = new SpeechToText(this,"th-TH");

        speechToText.initRecognize(new ResultListener() {
            @Override
            public void onResult(String text, float confidence) {
                System.out.println("Result : "+text+" , "+confidence);
                //mock config
                textSpeech = text;

                if(textSpeech.equals("เปิดเพลง")){
                    startPlaySong();
                }else if(textSpeech.equals("เปิดวีดีโอ")){
                    //play with webView
                    speechToText.stopRecognize();
                    playVideoStandard();
                }else{
                    startDialog();
                }

                for(int i=0;i<for_time.size();++i){
                    if(textSpeech.equalsIgnoreCase(for_time.get(i))){
                        speechToText.stopRecognize();
                        Intent intentSchedule = new Intent(MainActivity.this,Schedule.class);
                        startActivity(intentSchedule);
                    }
                }


                textView.setText(textSpeech);
//                startTextToSpeech();
            }
        });

        speechToText.startRecognize();

    }


    public void startDialog(){

         dialogueText = new DialogueText(key_dialog);

         dialogueText.setCallback(new SpeakCallbackListener() {
             @Override
             public void onResult(String fulfillment) {
                 textDialog = fulfillment;
                 startTextToSpeech();
             }
         });

         dialogueText.initService();

    }

    public void startTextToSpeech(){

        System.out.println("startTextToSpeech");
//        dialogueText.detectText(textSpeech);
        textToSpeechRecognize = new TextToSpeechRecognize(this,"th-TH",textDialog); //textDialog
        textToSpeechRecognize.initRecognize();


    }

    public void startPlaySong(){

        playSong = new PlaySong(MainActivity.this);

//        playSong.startSong();

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.play_song);
        dialog.setCancelable(true);

        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSong.stopSong();
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void startFaceDetect(Bitmap bitmap){

        FaceDetect faceDetect = new FaceDetect(this);
        faceDetect.initFaceDetect();


    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private String end_coding(){

        String res = null;
        InputStream is = getResources().openRawResource(R.raw.test_agent);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1];
        try {
            while ( is.read(b) != -1 ) {
                baos.write(b);
            };
            res = baos.toString();
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] encodedBytes = java.util.Base64.getEncoder().encode(res.getBytes());
        System.out.println("encodedBytes " + new String(encodedBytes));
        return new String(encodedBytes);

    }



    public void playVideoStandard(){

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.play_video);
        dialog.setCancelable(true);

        WebView webView = dialog.findViewById(R.id.videoPlayer);
        Button btn_exit = dialog.findViewById(R.id.btn_cancel);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.youtube.com/watch?v=4qzUJphkVZs");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //speechToText.startRecognize();
                dialog.cancel();
            }
        });

        dialog.show();
    }


    public void connectWifiForRemote(){








    }



}