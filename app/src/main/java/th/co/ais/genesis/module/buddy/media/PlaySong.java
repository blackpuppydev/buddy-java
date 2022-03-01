package th.co.ais.genesis.module.buddy.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;

import java.io.File;

import th.co.ais.genesis.module.buddy.R;

public class PlaySong {


    private SimpleExoPlayer simpleExoPlayer;
    private MediaPlayer mediaPlayer;
    private Context context;


    public PlaySong(Context context){
        this.context = context;


    }

    public void init(){

        this.simpleExoPlayer = new SimpleExoPlayer.Builder(this.context)
                                    .setTrackSelector(new DefaultTrackSelector(this.context))
                                    .setLoadControl(new DefaultLoadControl()).build();


        this.simpleExoPlayer.setThrowsWhenUsingWrongThread(false);

        this.simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {
                System.out.println("onTimelineChanged : "+timeline);
                switch (reason){
                    case Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED:
                        System.out.println("TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED");
                        return;
                    case Player.TIMELINE_CHANGE_REASON_SOURCE_UPDATE:
                        System.out.println("TIMELINE_CHANGE_REASON_SOURCE_UPDATE");
                        return;
                }
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {

            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {
                if(isLoading){
                    System.out.println("onIsLoadingChanged : load");
                }else{
                    System.out.println("onIsLoadingChanged : not load ");
                }
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                switch (state){
                    case Player.PLAYBACK_SUPPRESSION_REASON_NONE :
                        System.out.println("onPlaybackStateChanged : PLAYBACK_SUPPRESSION_REASON_NONE");
                        return;
                    case Player.PLAYBACK_SUPPRESSION_REASON_TRANSIENT_AUDIO_FOCUS_LOSS :
                        System.out.println("onPlaybackStateChanged : PLAYBACK_SUPPRESSION_REASON_TRANSIENT_AUDIO_FOCUS_LOSS");
                        return;
                }

            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {

                switch (reason){
                    case Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST :
                        System.out.println("onPlayWhenReadyChanged : PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST");
                        return;
                    case Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS :
                        System.out.println("onPlayWhenReadyChanged : PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS");
                        return;
                    case Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_BECOMING_NOISY :
                        System.out.println("onPlayWhenReadyChanged : PLAY_WHEN_READY_CHANGE_REASON_AUDIO_BECOMING_NOISY");
                        return;
                    case Player.PLAY_WHEN_READY_CHANGE_REASON_REMOTE :
                        System.out.println("onPlayWhenReadyChanged : PLAY_WHEN_READY_CHANGE_REASON_REMOTE");
                        return;
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });




    }

    public void startSong(String url){ //find url


        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory();

        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(new MediaItem.Builder().setUri(url).build());

        simpleExoPlayer.setMediaSource(mediaSource);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();


        //
//        if(text.equals("....")){
//            play ... song
//        mediaPlayer = MediaPlayer.create(context, R.raw.kennytitanic); //R.raw.yoursong

//        }

//        mediaPlayer.start();
    }

    public void startSong(File file){

        Uri uriFile = Uri.fromFile(file);

        DataSpec dataSpec = new DataSpec(uriFile);

        FileDataSource fileDataSource = new FileDataSource();

        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return fileDataSource;
            }
        };

        MediaSource mediaSource = new DefaultMediaSourceFactory(factory).createMediaSource(new MediaItem.Builder().setUri(fileDataSource.getUri()).build());

        simpleExoPlayer.setMediaSource(mediaSource);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();

    }

    public void stopSong(){

//        mediaPlayer.stop();
//        mediaPlayer.release();


        if(simpleExoPlayer != null){
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

    }

    public void pauseSong(){
        if(simpleExoPlayer != null){
            simpleExoPlayer.pause();
        }
    }

    public void resumeSong(){
        if(simpleExoPlayer != null){
            simpleExoPlayer.play();
        }
    }




}
