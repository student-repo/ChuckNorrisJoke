package com.example.ubuntu_master.chucknorrisjoke;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> picturesUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        picturesUrls = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void downloadImagesButton(View v) {
//        getPicturesUrl("http://thecatapi.com/api/images/get?format=xml&results_per_page=1000&type=jpg");
        getPicturesUrl("http://thecatapi.com/api/images/get?format=xml&results_per_page=10&type=jpg");

    }

    public void downloadJokeButton(View v){


        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        final TextView jokeText = (TextView)findViewById(R.id.joke_text);
        progressBar.setProgress(0);


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
                            progressBar.setProgress(100);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
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
        picturesUrls = new ArrayList<>();
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        Ion.with(this).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                try {
                    int progressBarValue = 0;

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
                        addViewToGrid(loadSingleImage(s, 0, 0));
                        progressBarValue += 100 / picturesUrls.size();
                        System.out.println("PROGRESS BAR VALUE :  " + progressBarValue);
                        progressBar.setProgress(progressBarValue);
                    }


                } catch (JSONException e1) {
                    e1.printStackTrace();

                }

            }
        });
    }


    public ImageView loadSingleImage(String url, int rowNum, int colNum){
        ImageView iv = new  ImageView(this);
        Picasso.with(this).load(url).resize(400, 400).into(iv);
//        Picasso.with(this).load(url).into(iv);

        GridLayout.LayoutParams param =new GridLayout.LayoutParams();
        param.setGravity(Gravity.CENTER);
        iv.setLayoutParams(param);
        return iv;
    }

    public void addViewToGrid(ImageView iv){
        GridLayout g = (GridLayout)findViewById(R.id.image_grid);
        g.addView(iv);
    }

}
