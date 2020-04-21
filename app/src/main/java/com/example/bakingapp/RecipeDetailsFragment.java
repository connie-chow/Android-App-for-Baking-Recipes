package com.example.bakingapp;


import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import retrofit2.Callback;

import static androidx.constraintlayout.widget.Constraints.TAG;

// https://github.com/mitchtabian/Video-Player-RecyclerView/blob/master/app/src/main/java/com/codingwithmitch/recyclerviewvideoplayer/VideoPlayerRecyclerView.java
// https://github.com/mitchtabian/Video-Player-RecyclerView/blob/master/app/src/main/java/com/codingwithmitch/recyclerviewvideoplayer/VideoPlayerRecyclerView.java
// https://www.youtube.com/watch?v=z44CLCafepA  42:55

public class RecipeDetailsFragment extends Fragment implements SurfaceHolder.Callback {

    public static String step_id;
    FrameLayout media_container;
    TextView title;
    ImageView mVideoThumbnail, volumeControl;
    String mVideoURL;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "RecipeDetailsFragment: surfaceCreated()");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "RecipeDetailsFragment: surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "RecipeDetailsFragment: surfaceDestroyed()");
    }

    private enum VolumeState {ON, OFF};
    private VolumeState volumeState;
    private PlayerControlView playerControlView;
    SurfaceHolder surfaceHolder;

    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;


    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    MediaSource videoSource;

    // controlling playback state
    //private VolumeState volumeState;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public RecipeDetailsFragment() {

    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// https://www.youtube.com/watch?v=jAZn-J1I8Eg
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        // exoplayer
        media_container = rootView.findViewById(R.id.media_container);
        // contains all three children: progressBar, thumbnail and volume control

        mVideoThumbnail = rootView.findViewById(R.id.thumbnail);
        // thumbnail is the thumbnail of the video in place of the video when loading
        // mVideoThumbnail needs to be changed...
        title = rootView.findViewById(R.id.title);
        progressBar = rootView.findViewById(R.id.progressBar);
        volumeControl = rootView.findViewById(R.id.volume_control);
        playerControlView = rootView.findViewById(R.id.player_control_view);
        requestManager = initGlide();

        // Get a reference to the ImageView in the fragment layout
        TextView tv = (TextView) rootView.findViewById(R.id.recipe_step_text);


        // Get incoming step ID from RecipeActivity Fragment's Step Adapter
        Bundle extras = getActivity().getIntent().getExtras();
        Bundle arguments = getArguments();
        String step_id = arguments.getString("step_id");
        String recipe_id = arguments.getString("recipe_id");
        //Bundle b = getArguments();
        //String step_id = b.getString("step_id");
        //tv.setText(step_id + step_id + step_id + step_id + step_id );



        RecipeStepViewModel mRecipeStepsViewModel = ViewModelProviders.of(this).get(RecipeStepViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        // how to get recipe id inside fragment?
        mRecipeStepsViewModel.getRecipeStepDetails(recipe_id, step_id).observe(this, new Observer<Steps>() {
            @Override
            public void onChanged(@Nullable final Steps steps) {
                // Update the cached copy of the words in the adapter.
                //mRecipeStepAdapter.setList(steps);
                // steps has thumbnailURL, videoURL, shortDescription, r_id, s_id, description
                tv.setText(steps.getDescription());

                String url = steps.getThumbnailURL();
                url = "https://image.tmdb.org/t/p/w185//xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg";
                mVideoURL = steps.getVideoURL();

                if(mVideoURL.equals("")) {
                    media_container.setVisibility(View.GONE);
                    for (int i = 0; i < media_container.getChildCount(); i++) {
                        View v = media_container.getChildAt(i);
                        v.setVisibility(View.GONE);
                        v.postInvalidate();
                    }
                }

                //Glide.with(rootView)
                //        .load(url) // or URI/path
                //        .into(mVideoThumbnail); //imageview to set thumbnail to

                title.setText(steps.getShortDescription());
                //initPlayer(getContext());



                // expoplayer goes here also
            }
        });



        // Return the rootView
        return rootView;


        // Exoplayer: https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
        //https://exoplayer.dev/hello-world.html

    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Store UI state to the savedInstanceState
        // This bundle will be passed to onCreate on next call
        Log.e(TAG, "RecipeDetailsFragment: onPause(): releasing: ");


        savedInstanceState.putBoolean("playWhenReady", playWhenReady);
        savedInstanceState.putString("playbackPosition", Long.toString(playbackPosition));
        savedInstanceState.putString("currentWindow", Integer.toString(currentWindow));

        super.onSaveInstanceState(savedInstanceState);
    }


    // fragments do'nt have onRestorEInstanceState
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            playbackPosition = Integer.parseInt(savedInstanceState.getString("playbackPosition"));
            playWhenReady = savedInstanceState.getBoolean("playWhenReady");
            currentWindow = Integer.parseInt(savedInstanceState.getString("currentWindow"));
        }
    }


    //https://stackoverflow.com/questions/41848293/google-exoplayer-guide
    private void addVideoView() {
        Log.e(TAG, "RecipeDetailsFragment: addVideoView():videoSurfaceView= " + videoSurfaceView);
        media_container.addView(videoSurfaceView);
        isVideoViewAdded = true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(View.VISIBLE);
        videoSurfaceView.setAlpha(1);
        mVideoThumbnail.setVisibility(View.GONE);
        Player v = videoSurfaceView.getPlayer();
        videoSurfaceView.setUseController(true);
    }


    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }


    private void releasePlayer() {
        removeVideoView(videoSurfaceView);
        if (videoPlayer != null) {
            playWhenReady = videoPlayer.getPlayWhenReady();
            playbackPosition = videoPlayer.getCurrentPosition();
            currentWindow = videoPlayer.getCurrentWindowIndex();
            videoPlayer.release();
            videoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        Log.e(TAG, "RecipeDetailsFragment: onStop() ..releasing: ");
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }


    /*
    Before API Level 24 there is no guarantee of onStop being called. So we have to release the
    player as early as possible in onPause. Starting with API Level 24 (which brought multi and
    split window mode) onStop is guaranteed to be called. In the paused state our activity is still
    visible so we wait to release the player until onStop.
     */
    @Override
    public void onPause() {
        Log.e(TAG, "RecipeDetailsFragment: onPause(): releasing: ");
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }


// https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
    public void initPlayer(Context context) {
/*
        // new for resuming a video
        if(videoPlayer != null) {
            videoPlayer.setPlayWhenReady(playWhenReady);
            videoPlayer.seekTo(currentWindow, playbackPosition);
            videoPlayer.prepare(videoSource, false, false);
        }
*/

        this.context = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        //videoSurfaceView.setVisibility(View.INVISIBLE);
        videoSurfaceView = new PlayerView(this.context);
        Log.e(TAG, "RecipeDetailsFragment: initPlayer(): videoSurfaceView created");
        //videoSurfaceView.setControllerHideOnTouch(true);
        videoSurfaceView.hideController();
        videoSurfaceView.setVisibility(View.INVISIBLE);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        //videoSurfaceView.setUseController(true);


        if(videoSurfaceView != null) {
            SurfaceView v = (SurfaceView) videoSurfaceView.getVideoSurfaceView();
            surfaceHolder = v.getHolder();
            surfaceHolder.addCallback(this);
        }


        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context , trackSelector);
        // Bind the player to the view.
        //videoSurfaceView.setUseController(true);

        videoSurfaceView.setPlayer(videoPlayer);
        setVolumeControl(VolumeState.ON);

        // Attach player to the view.
        playerControlView.setPlayer(videoPlayer);

        // Prepare the player with the dash media source.
        //videoPlayer.prepare(createMediaSource());


        //https://stackoverflow.com/questions/52365953/exoplayer-playerview-onclicklistener-not-working
        //https://codelabs.developers.google.com/codelabs/exoplayer-intro/#5
        //https://exoplayer.dev/ui-components.html
        videoSurfaceView.setOnClickListener(view -> {
        //videoPlayer.getVideoSurfaceView().setOnClickListener(view -> {
            Log.e("GET", "clicked!");
            if(videoPlayer.getPlayWhenReady()){
                videoPlayer.setPlayWhenReady(false);
            }else{
                videoPlayer.setPlayWhenReady(true);
            }
        });

        playVideo();


        videoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.");
                        if (progressBar != null) {
                            progressBar.setVisibility(View.VISIBLE);
                            videoSurfaceView.setUseController(false);
                        }

                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: Video ended.");
                        videoPlayer.seekTo(0);
                        break;
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_READY:
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.");
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        Log.e(TAG, "RecipeDetailsFragment: isVideoViewAdded = " + isVideoViewAdded);
                        if(!isVideoViewAdded){
                            addVideoView();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }


            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }


            @Override
            public void onPlayerError(ExoPlaybackException error) {
                switch (error.type) {
                    case ExoPlaybackException.TYPE_SOURCE:
                        Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                        break;
                    case ExoPlaybackException.TYPE_RENDERER:
                        Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                        break;
                    case ExoPlaybackException.TYPE_UNEXPECTED:
                        Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                        break;
                }
            }

        });
    }



    private View.OnClickListener videoViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleVolume();
        }
    };

    private void toggleVolume() {
        if (videoPlayer != null) {
            if (volumeState == VolumeState.OFF) {
                Log.d(TAG, "togglePlaybackState: enabling volume.");
                setVolumeControl(VolumeState.ON);

            } else if(volumeState == VolumeState.ON) {
                Log.d(TAG, "togglePlaybackState: disabling volume.");
                setVolumeControl(VolumeState.OFF);

            }
        }
    }


    // Player's lifecycle to manage resources, including memory, CPU, network connections, hardware codecs
    // Player has no lifecycle API calls of its own, so we tie it to the lifecycle of the fragment
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            Bundle extras = getActivity().getIntent().getExtras();
            Bundle arguments = getArguments();
            String step_id = arguments.getString("step_id");
            String recipe_id = arguments.getString("recipe_id");
            //initPlayer(getContext());

            RecipeStepViewModel mRecipeStepsViewModel = ViewModelProviders.of(this).get(RecipeStepViewModel.class);

            // Add an observer on the LiveData returned by getAlphabetizedWords.
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground.
            // how to get recipe id inside fragment?
            mRecipeStepsViewModel.getRecipeStepDetails(recipe_id, step_id).observe(this, new Observer<Steps>() {
                @Override
                public void onChanged(@Nullable final Steps steps) {
                    // Update the cached copy of the words in the adapter.
                    //mRecipeStepAdapter.setList(steps);
                    // steps has thumbnailURL, videoURL, shortDescription, r_id, s_id, description
                    //tv.setText(steps.getDescription());

                    String url = steps.getThumbnailURL();
                    url = "https://image.tmdb.org/t/p/w185//xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg";
                    mVideoURL = steps.getVideoURL();

                    if(!mVideoURL.equals("")) {
                        //Glide.with(rootView)
                        //        .load(url) // or URI/path
                        //        .into(mVideoThumbnail); //imageview to set thumbnail to

                        //title.setText(steps.getShortDescription());

                        initPlayer(getContext());
                    }



                    // expoplayer goes here also
                }
            });



            Log.e(TAG, "RecipeDetailsFragment: onStart()");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "RecipeDetailsFragment: onResume()");
        //hideSystemUi();
        if ((Util.SDK_INT < 24 || videoPlayer == null)) {

            initPlayer(getContext());
            /*
            if(videoPlayer != null) {
                videoPlayer.setPlayWhenReady(playWhenReady);
                videoPlayer.seekTo(currentWindow, playbackPosition);
                videoPlayer.prepare(videoSource, false, false);
                videoPlayer.setPlayWhenReady(true);
            }*/

        }
    }


    private void setVolumeControl(VolumeState state){
        volumeState = state;
        if(state == VolumeState.OFF){
            videoPlayer.setVolume(0f);
            animateVolumeControl();
        }
        else if(state == VolumeState.ON){
            videoPlayer.setVolume(1f);
            animateVolumeControl();
        }
    }

    private void animateVolumeControl(){
        if(volumeControl != null){
            volumeControl.bringToFront();
            if(volumeState == VolumeState.OFF){
                requestManager.load(R.drawable.ic_volume_off_grey_24dp)
                        .into(volumeControl);
            }
            else if(volumeState == VolumeState.ON){
                requestManager.load(R.drawable.ic_volume_up_grey_24dp)
                        .into(volumeControl);
            }
            volumeControl.animate().cancel();

            volumeControl.setAlpha(1f);

            volumeControl.animate()
                    .alpha(0f)
                    .setDuration(6000).setStartDelay(1000);
        }
    }



    public void playVideo() {
        /*
        thumbnail = holder.thumbnail;
        progressBar = holder.progressBar;
        volumeControl = holder.volumeControl;
        viewHolderParent = holder.itemView;


         = holder.requestManager;
        frameLayout = holder.itemView.findViewById(R.id.media_container);


*/


        // remove any old surface views from previously playing videos
        //videoSurfaceView.setVisibility(View.INVISIBLE);
        //removeVideoView(videoSurfaceView);
        //videoSurfaceView.setPlayer(videoPlayer);


        // ExoPlayer needs something to play, create a MediaSource
        // used when making http request for the file
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "RecyclerView VideoPlayer"));
        //String mediaUrl = mediaObjects.get(targetPosition).getMedia_url();

        String mediaUrl = mVideoURL;
        if (mediaUrl != null) {
            Log.e(TAG, "RecipeDetailsFragment: videoSource = " + videoSource);
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(mediaUrl));
            videoPlayer.prepare(videoSource);
            videoPlayer.setPlayWhenReady(true);
        }
    }


    // Remove the old player
    private void removeVideoView(PlayerView videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            isVideoViewAdded = false;
            //viewHolderParent.setOnClickListener(null);
        }
    }
}


//https://android.jlelse.eu/android-exoplayer-starters-guide-6350433f256c
//https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
//https://blog.mindorks.com/using-exoplayer-to-play-video-and-audio-in-android-like-a-pro