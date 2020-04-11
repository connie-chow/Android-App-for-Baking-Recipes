package com.example.bakingapp;


import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

// https://github.com/mitchtabian/Video-Player-RecyclerView/blob/master/app/src/main/java/com/codingwithmitch/recyclerviewvideoplayer/VideoPlayerRecyclerView.java


public class RecipeDetailsFragment extends Fragment {

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

    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    //private RequestManager requestManager;

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
        mVideoThumbnail = rootView.findViewById(R.id.thumbnail);
        title = rootView.findViewById(R.id.title);
        progressBar = rootView.findViewById(R.id.progressBar);
        volumeControl = rootView.findViewById(R.id.volume_control);



        // Get a reference to the ImageView in the fragment layout
        TextView tv = (TextView) rootView.findViewById(R.id.recipe_step_text);
        tv.setText("recipe step details: instructions...");

        // Get incoming step ID from RecipeActivity Fragment's Step Adapter
        Bundle extras = getActivity().getIntent().getExtras();
        Bundle arguments = getArguments();



        String step_id = arguments.getString("step_id");
        String recipe_id = arguments.getString("recipe_id");
        //Bundle b = getArguments();
        //String step_id = b.getString("step_id");
        tv.setText(step_id + step_id + step_id + step_id + step_id );


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
                mVideoURL = steps.getVideoURL();

                Glide.with(rootView)
                        .load(url) // or URI/path
                        .into(mVideoThumbnail); //imageview to set thumbnail to

                title.setText(steps.getShortDescription());
                initPlayer(getContext());



                // expoplayer goes here also
            }
        });




        // Return the rootView
        return rootView;
/*

        // Exoplayer: https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
        //https://exoplayer.dev/hello-world.html




        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_body_part, container, false);

        // Get a reference to the ImageView in the fragment layout
        ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);

        // Set the image to the first in our list of head images
        imageView.setImageResource(AndroidImageAssets.getHeads().get(0));

        // TODO (3) If a list of image ids exists, set the image resource to the correct item in that list
        // Otherwise, create a Log statement that indicates that the list was not found

        // Return the rootView
        return rootView;

 */

    }

    private void addVideoView() {
        media_container.addView(videoSurfaceView);
        isVideoViewAdded = true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(View.VISIBLE);
        videoSurfaceView.setAlpha(1);
        mVideoThumbnail.setVisibility(View.GONE);
    }



    public void initPlayer(Context context) {
        this.context = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        videoSurfaceView = new PlayerView(this.context);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        // Bind the player to the view.
        videoSurfaceView.setUseController(false);
        videoSurfaceView.setPlayer(videoPlayer);
        //setVolumeControl(VolumeState.ON);

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
                            //progressBar.setVisibility(true);
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
                            //progressBar.setVisibility(false);
                        }
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
            public void onPlayerError(ExoPlaybackException error) {

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
        });
    }



    public void playVideo() {
        /*
        thumbnail = holder.thumbnail;
        progressBar = holder.progressBar;
        volumeControl = holder.volumeControl;
        viewHolderParent = holder.itemView;
        requestManager = holder.requestManager;
        frameLayout = holder.itemView.findViewById(R.id.media_container);


*/

        videoSurfaceView.setPlayer(videoPlayer);

        //viewHolderParent.setOnClickListener(videoViewClickListener);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "RecyclerView VideoPlayer"));
        //String mediaUrl = mediaObjects.get(targetPosition).getMedia_url();
        String mediaUrl = mVideoURL;
        if (mediaUrl != null) {
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(mediaUrl));
            videoPlayer.prepare(videoSource);
            videoPlayer.setPlayWhenReady(true);
        }
    }


}


//https://android.jlelse.eu/android-exoplayer-starters-guide-6350433f256c
//https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
//https://blog.mindorks.com/using-exoplayer-to-play-video-and-audio-in-android-like-a-pro