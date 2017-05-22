package com.example.ubuntu_master.chucknorrisjoke;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> picturesUrls;
    private ArrayList<ImageView> pictures;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        picturesUrls = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getPicturesUrl("http://thecatapi.com/api/images/get?format=xml&results_per_page=10&type=jpg");

    }


    public void downloadImagesButton(View v) {
//        DownloadImagesTask  dit = new DownloadImagesTask();
//        dit.execute();

        getPicturesUrl("http://thecatapi.com/api/images/get?format=xml&results_per_page=10&type=jpg");

    }

    public void downloadJokeButton(View v){


        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        final TextView jokeText = (TextView)findViewById(R.id.joke_text);


        Ion.with(this)
                .load("http://api.icndb.com/jokes/random")
//                .load("http://norvig.com/big.txt")
                .progressBar(progressBar)
                .progressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
//                        downloadCount.setText("" + downloaded + " / " + total);
                    }
                })
                .asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            jokeText.setText(getJoke(result));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        progressBar.setProgress(0);
                        Log.v("ion", result);
                        if (e != null) {
                            System.out.println("DOWNLOAD ERROR");
                            return;
                        }
                        System.out.println("DOWNLOAD COMPLETE");
                    }
                });


    }

    private String getJoke(String report) throws JSONException {
        JSONObject json = new JSONObject(report);
        JSONObject json2 = new JSONObject(json.getString("value"));
        return json2.getString("joke");
    }

    private void getPicturesUrl(String url){
        Ion.with(this).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                try {

                    JSONObject xmlJSONObj = XML.toJSONObject(result);
                    String jsonPrettyPrintString = xmlJSONObj.toString();
                    JSONObject json1 = new JSONObject(jsonPrettyPrintString );
                    JSONObject json2 = new JSONObject(json1.getString("response"));
                    JSONObject json3 = new JSONObject(json2.getString("data"));
                    JSONObject json4 = new JSONObject(json3.getString("images"));
                    JSONArray jsonArray = json4.getJSONArray("image");
                    for(int i = 0 ;i < jsonArray.length(); i++) {
                        JSONObject json5 = new JSONObject(jsonArray.get(i).toString());
                        picturesUrls.add(json5.getString("url"));
                    }
                    for(String s : picturesUrls){
                        System.out.println(s);
                    }

                    DownloadImagesTask  dit = new DownloadImagesTask();
                    dit.execute();


                } catch (JSONException e1) {
                    e1.printStackTrace();

                }

            }
        });
    }

//    public ArrayList<ImageView> createImageGallery(int n) {
//        ArrayList<ImageView> imageGallery = new ArrayList<>();
//        for(int i = 0; i < n; i++) {
//            imageGallery.add(new ImageView(this));
//        }
//        return imageGallery;
//    }

    public ImageView loadSingleImage(String url){
        ImageView iv = new  ImageView(this);
        Picasso.with(this).load(url).into(iv);





//        Picasso.with(this).load("http://25.media.tumblr.com/tumblr_lpgfaarxXI1qbhms5o1_500.jpg").into(iv);
        return iv;
    }

    public void addViewToGrid(ImageView iv){
        GridLayout g = (GridLayout)findViewById(R.id.image_grid);
        g.addView(iv);
    }

    public void addPicture(ImageView iv){
        pictures.add(iv);
    }

    private class DownloadImagesTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            pictures = new ArrayList<>();












//            for(int i = 0; i< picturesUrls.size(); i++){
////                addViewToGrid(loadSingleImage(picturesUrls.get(i)));
////                pictures.add(loadSingleImage(picturesUrls.get(i)));
////                System.out.println("ala ma kota");
////                addPicture();
//                loadSingleImage(picturesUrls.get(i));
//            }
//            getPicturesUrl("http://thecatapi.com/api/images/get?format=xml&results_per_page=10&type=jpg");
            System.out.println("ala ma kota");


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            for(int i = 0; i< picturesUrls.size(); i++){
                addViewToGrid(loadSingleImage(picturesUrls.get(i)));
//                pictures.add(loadSingleImage(picturesUrls.get(i)));
            }

//            for(ImageView iv : pictures){
//                addViewToGrid(iv);
//            }

            System.out.println("@@@@@@@@@@@@@@@");
            System.out.println(picturesUrls.size());
            System.out.println("@@@@@@@@@@@@@@@");
//            addViewToGrid(loadSingleImage("ada"));
        }

    }

}
