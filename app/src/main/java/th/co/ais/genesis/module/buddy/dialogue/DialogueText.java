package th.co.ais.genesis.module.buddy.dialogue;

import android.util.Base64;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import th.co.ais.genesis.module.buddy.blueprint.SpeakCallbackListener;

public class DialogueText {

    private String key_dialog;

    private String projectId;
    private SessionsSettings sessionsSettings;
    private SessionsClient sessionsClient;
    private SessionName session;
    private String uuid;
    private QueryInput queryInput;
    private GoogleCredentials credentials;

    private SpeakCallbackListener speakCallbackListener;

    public DialogueText(String key_dialog){
        this.key_dialog = key_dialog;

    }


    public void setCallback(SpeakCallbackListener speakCallbackListener){
        this.speakCallbackListener = speakCallbackListener;
    }

    public void initService(){

        try {

            String credential = decodeBase64(key_dialog);
            InputStream credentialStream = new ByteArrayInputStream(credential.getBytes());
            credentials = GoogleCredentials.fromStream(credentialStream);
            projectId = ((ServiceAccountCredentials) credentials).getProjectId();
            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, UUID.randomUUID().toString());

            System.out.println("Complete service");
        }catch (Exception e){
            System.out.println("Error "+e.toString());
            e.printStackTrace();
        }


    }


    public void detectText(String text){

        queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(text).setLanguageCode("en-US")).build();

        DetectIntentRequest detectIntentRequest =
                DetectIntentRequest.newBuilder()
                        .setSession(session.toString())
                        .setQueryInput(queryInput)
                        .build();

        DetectIntentResponse detectIntentResponse = sessionsClient.detectIntent(detectIntentRequest);

        QueryResult queryResult = detectIntentResponse.getQueryResult();

        if (queryResult != null && !queryResult.getQueryText().isEmpty()) {

            String queryText = queryResult.getQueryText();
            String intentDisplayName = queryResult.getIntent().getDisplayName();
            String fulfillmentText = queryResult.getFulfillmentText();
            float Confidence = queryResult.getIntentDetectionConfidence();

            System.out.println("Query Text: "+queryText);
            System.out.println("Detected Intent: " + intentDisplayName);
            System.out.println("Fulfillment Text: " + fulfillmentText);
            System.out.println("Detected Confidence: " + Confidence);

            speakCallbackListener.onResult(fulfillmentText);

        }



    }




    public static String decodeBase64(String str) {
        byte[] decodedBytes = Base64.decode(str,Base64.DEFAULT);
        return new String(decodedBytes);
    }

}
