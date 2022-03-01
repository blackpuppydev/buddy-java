package th.co.ais.genesis.module.buddy.gms;

import android.content.Context;

import java.util.Locale;
import android.speech.tts.TextToSpeech;

public class TextToSpeechRecognize {

    private TextToSpeech textToSpeech;
    private Context context;
    private String langCode;
    private String text;

    public TextToSpeechRecognize(Context context, String langCode, String text){

        this.context = context;

        if(langCode == null){
            this.langCode = "th-TH";
        }else{
            this.langCode = langCode;
        }

        this.text = text;

    }


    public void initRecognize(){

        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == android.speech.tts.TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

    }





}
