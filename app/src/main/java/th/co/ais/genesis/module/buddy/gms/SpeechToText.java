package th.co.ais.genesis.module.buddy.gms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

import th.co.ais.genesis.module.buddy.blueprint.ResultListener;

import static android.speech.SpeechRecognizer.CONFIDENCE_SCORES;
import static android.speech.SpeechRecognizer.ERROR_AUDIO;
import static android.speech.SpeechRecognizer.ERROR_CLIENT;
import static android.speech.SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS;
import static android.speech.SpeechRecognizer.ERROR_NETWORK;
import static android.speech.SpeechRecognizer.ERROR_NETWORK_TIMEOUT;
import static android.speech.SpeechRecognizer.ERROR_NO_MATCH;
import static android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY;
import static android.speech.SpeechRecognizer.ERROR_SERVER;
import static android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT;
import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

public class SpeechToText {

    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;

    private Context context;
    private String langCode;

    private ResultListener resultListener;

    public SpeechToText(Context context, String langCode){

        this.context = context;

        if(langCode == null){
            this.langCode = "th-TH";
        }else{
            this.langCode = langCode;
        }

    }

    public void initRecognize(ResultListener resultListener){

        this.resultListener = resultListener;

        this.mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, langCode);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES,true);

        //another setting

    }

    public void startRecognize(){

//        resultListener.onResult("เปิดวีดีโอ",0.9f);

        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                System.out.println("onReadyForSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                System.out.println("onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                if(error != SpeechRecognizer.ERROR_AUDIO && error != ERROR_INSUFFICIENT_PERMISSIONS){
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                }

                switch (error) { //when error //log error dif warning
                    case ERROR_NETWORK_TIMEOUT: //1
                        System.out.println("ERROR_NETWORK_TIMEOUT");
                        break;
                    case ERROR_NETWORK: //2
                        System.out.println("ERROR_NETWORK");
                        break;
                    case ERROR_AUDIO: //3 not sound
                        System.out.println("ERROR_AUDIO");
                        break;
                    case ERROR_SERVER://4
                        System.out.println("ERROR_SERVER");
                        break;
                    case ERROR_CLIENT: //5
                        System.out.println("ERROR_CLIENT");
                        break;
                    case ERROR_SPEECH_TIMEOUT: //6
                        System.out.println("ERROR_SPEECH_TIMEOUT");
                        break;
                    case ERROR_NO_MATCH: //7
                        System.out.println("ERROR_NO_MATCH");
                        break;
                    case ERROR_RECOGNIZER_BUSY: //8 ***** maybe internet not connect
                        System.out.println("ERROR_RECOGNIZER_BUSY");
                        break;
                    case ERROR_INSUFFICIENT_PERMISSIONS: //9
                        System.out.println("ERROR_INSUFFICIENT_PERMISSIONS");
                        break;
                }
            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matches = results.getStringArrayList(RESULTS_RECOGNITION);
                float[] confidence_score = results.getFloatArray(CONFIDENCE_SCORES);

                if(matches!= null){
                    resultListener.onResult(matches.get(0),confidence_score[0]);
                }

                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    public void stopRecognize(){
        mSpeechRecognizer.destroy();
        mSpeechRecognizer.cancel();
    }

    
    public void song_function(String text){


        if(text.equals("เปิดเพลง")){

        }

        // ** Maybe cutting word

    }


}
