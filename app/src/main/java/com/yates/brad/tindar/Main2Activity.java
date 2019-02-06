/**
 * Main2Activity.java
 * Reponsible for the AR portion of the application.
 *
 * @author  Brad Yates & Kennedey Oddom Sok
 * @version 1.0
 * @since   11/19/2018
 */

package com.yates.brad.tindar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = Main2Activity.class.getName();

    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ViewRenderable viewRenderable;
    private ModelRenderable modelRenderable;
    private AnchorNode active;
    ScrollView profView;
    ScrollView matchView;
    String selectedName;

    String[] names = { "Kelly", "Jess", "Rachel","Monica","Lisa","Tammy","Phoebe","Lauren","Cassandra","Britney" };
    int[] ages = {20,21,22,23,24,25,26};
    String profile1 = "Just a girl looking for someone fun to hangout with";
    String profile2 = "Looking for a relationship with someone career driven";
    String profile3 = "If you look like Brad Pitt, please date me!";
    String profile4 = "Buy me a pizza and watch Netflix with me";
    String profile5 = "Music is my life! Looking for someone to go to concerts with xD";

    String[] bios ={profile1,profile2,profile3,profile4,profile5};
    ArrayList<String> matchNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make sure OpenGL version is supported
        if (!checkIsSupportedDevice(this)) {
            String errorMessage =  "Sceneform requires OpenGL ES " + MIN_OPENGL_VERSION + " or later";
            Log.e(TAG, errorMessage);
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            finish(); // finish the activity
            return;
        }

        // As soon as we call setContentView, they view will be inflated, including the ARFragment
        // This is why we need to check for OpenGL first
        setContentView(R.layout.activity_main2);
        profView = findViewById(R.id.profView);
        matchView = findViewById(R.id.matchView);
        profView.setVisibility(View.INVISIBLE);
        matchView.setVisibility(View.INVISIBLE);
        setupArScene();
    }

    /**
     * Helper method to set up the AR scene and render views in the scene.
     */
    private void setupArScene() {
        // ARFragment is what is displaying our scene
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        // load the renderables
        build3dModel();

        // handle taps
        handleUserTaps();
    }

    /**
     * Displays a {@link ModelRenderable} on the scene where the user tapped.
     */
    private void handleUserTaps() {
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            // viewRenderable must be loaded
            if (modelRenderable ==  null) {
                return;
            }

            // create the an anchor on the scene
            AnchorNode anchorNode = createAnchorNode(hitResult);

            // add the view to the scene
            addRenderableToScene(anchorNode, modelRenderable);


        });

    }

    /**
     * Creates an anchor on the {@link ArFragment} based on the {@link HitResult}
     *
     * @param hitResult
     *      - the HitResult to base the position of the anchor from.
     * @return
     *      - the created {@link AnchorNode}
     */
    private AnchorNode createAnchorNode(HitResult hitResult) {
        build3dModel();
        // create an anchor based off the the HitResult (what was tapped)
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);

        // attach this anchor to the scene
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        return anchorNode;
    }

    /**
     * Helper method to add a {@link Renderable} object to the scene as a {@link Node}
     *
     * @param anchorNode
     *      - {@link AnchorNode} that was created referencing the {@link ArFragment}
     *
     * @param renderable
     *      - the {@link Renderable} to add.
     *
     * @return the {@link Node} that was created.
     */
    private Node addRenderableToScene(AnchorNode anchorNode, Renderable renderable) {
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());

        TextView profName = findViewById(R.id.profName);
        TextView profAge = findViewById(R.id.profAge);
        TextView profBio = findViewById(R.id.profBio);
        String name = names[new Random().nextInt(names.length)];
        int age = ages[new Random().nextInt(ages.length)];
        String bio = bios[new Random().nextInt(bios.length)];

        // anchor node knows where it fits into our world
        node.setParent(anchorNode);
        node.setRenderable(renderable);
        node.setOnTapListener(new Node.OnTapListener() {
            @Override
            public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent){
                active = anchorNode;
                profName.setText(name);
                selectedName = name;
                profAge.setText(String.valueOf(age));
                profBio.setText(bio);
                profView.setVisibility(View.VISIBLE);
                profView.bringToFront();
            }
        });
        node.select();

        return node;
    }

    /**
     * Creates a {@link ModelRenderable} which will load the helloWorld.sfb from the assets folder.
     *
     * Note: The resources get loaded on the background thread automagically.
     *       'thenAccept' is the callback for completion.
     *
     *       helloWorld.sfb was added to the assets folder by the Sceneform plugin when we imported
     *       the asset
     */
    private void build3dModel() {

        int n = (int) ( Math.random() * 3 + 1); // will return either 1 or 2

        if(n == 1){
            ModelRenderable.builder()
                    .setSource(this, RenderableSource.builder().setSource(
                            this,
                            Uri.parse("https://poly.googleusercontent.com/downloads/82nw7m9p9Ku/5lHUAd5ziya/model.gltf"),
                            RenderableSource.SourceType.GLTF2)
                            .setScale(0.009f)  // Scale the original model to 50%.
                            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                            .build())
                    .setRegistryId("https://poly.googleusercontent.com/downloads/82nw7m9p9Ku/5lHUAd5ziya/model.gltf")
                    .build()
                    .thenAccept(renderable -> modelRenderable = renderable)
                    .exceptionally(throwable -> {
                        Toast.makeText(Main2Activity.this, "Unable to display model",
                                Toast.LENGTH_LONG).show();

                        return null;
                    });
        }
        if(n == 2){
            ModelRenderable.builder()
                    .setSource(this, RenderableSource.builder().setSource(
                            this,
                            Uri.parse("https://poly.googleusercontent.com/downloads/b0vcwiw64Je/cZfXxmJ8OeZ/model.gltf"),
                            RenderableSource.SourceType.GLTF2)
                            .setScale(0.009f)  // Scale the original model to 50%.
                            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                            .build())
                    .setRegistryId("https://poly.googleusercontent.com/downloads/b0vcwiw64Je/cZfXxmJ8OeZ/model.gltf")
                    .build()
                    .thenAccept(renderable -> modelRenderable = renderable)
                    .exceptionally(throwable -> {
                        Toast.makeText(Main2Activity.this, "Unable to display model",
                                Toast.LENGTH_LONG).show();

                        return null;
                    });
        }
        if(n == 3){
            ModelRenderable.builder()
                    .setSource(this, RenderableSource.builder().setSource(
                            this,
                            Uri.parse("https://poly.googleusercontent.com/downloads/9oExgkKugv0/7RcwfwN79e9/model.gltf"),
                            RenderableSource.SourceType.GLTF2)
                            .setScale(0.009f)  // Scale the original model to 50%.
                            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                            .build())
                    .setRegistryId("https://poly.googleusercontent.com/downloads/9oExgkKugv0/7RcwfwN79e9/model.gltf")
                    .build()
                    .thenAccept(renderable -> modelRenderable = renderable)
                    .exceptionally(throwable -> {
                        Toast.makeText(Main2Activity.this, "Unable to display model",
                                Toast.LENGTH_LONG).show();

                        return null;
                    });
        }

    }


    /**
     * Checks to see if Sceneform can run on the device OpenGL version.
     *
     * @param activity
     *      - ${@link Activity} from which to fetch the system services.
     *
     * @return true if Sceneform can run; false otherwise.
     */
    private boolean checkIsSupportedDevice(final Activity activity) {

        ActivityManager activityManager =
                (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager == null) {
            Log.e(TAG, "ActivityManager is null");
            return false;
        }

        String openGlVersion = activityManager.getDeviceConfigurationInfo().getGlEsVersion();

        return openGlVersion != null && Double.parseDouble(openGlVersion) >= MIN_OPENGL_VERSION;
    }

    public void onClick(View view) {
        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setVisibility(View.INVISIBLE);
    }

    public void onClickLike(View view) {
        profView.setVisibility(View.INVISIBLE);
        active.getAnchor().detach();
        int n = (int) ( Math.random() * 2 + 1); // will return either 1 or 2
        if(n == 1){
            matchView.setVisibility(View.VISIBLE);
            matchNames.add(selectedName);
        }
    }

    public void onClickPass(View view) {
        profView.setVisibility(View.INVISIBLE);
        active.getAnchor().detach();
    }

    public void OnClickClose(View view) {
        matchView.setVisibility(View.INVISIBLE);
    }

    public void OnClickChat(View view) {
        Intent intent = new Intent(this, Main3Activity.class);
        Bundle b = new Bundle();
        b.putString("name", selectedName);
        intent.putExtras(b);
        matchView.setVisibility(View.INVISIBLE);
        startActivity(intent);
    }

    public void OnClickMatches(View view) {
        Intent intent = new Intent(this, Main4Activity.class);
        intent.putStringArrayListExtra("matches", matchNames);
        startActivity(intent);
    }
}
