package com.example.ubuntu_master.chucknorrisjoke;

import android.provider.DocumentsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPicturesUrl("http://thecatapi.com/api/images/get?format=xml&results_per_page=10&type=jpg");

//        Ion.with(this).load("http://thecatapi.com/api/images/get?format=xml&results_per_page=10").asString().setCallback(new FutureCallback<String>() {
//            @Override
//            public void onCompleted(Exception e, String result) {
////                try {
////                    JSONObject xmlJSONObj = XML.toJSONObject(result);
////                    String jsonPrettyPrintString = xmlJSONObj.toString();
////                    System.out.println(jsonPrettyPrintString );
////                } catch (JSONException e1) {
////                    e1.printStackTrace();
////                }
//
//
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = null;
//                try {
//                    builder = factory.newDocumentBuilder();
//                } catch (ParserConfigurationException e1) {
//                    e1.printStackTrace();
//                }
//                Document document = null;
//                try {
//                    document = builder.parse(new InputSource(new StringReader(result)));
//                } catch (SAXException e1) {
//                    e1.printStackTrace();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//
//                String rootElement = document.getDocumentElement().getAttribute("url");
////                String requestQueueName = getString("url", rootElement);
//
//
//                System.out.println("@@@");
////                System.out.println(requestQueueName );
//                System.out.println(rootElement );
//
//
//
//
//                Log.v("ion", result);
//            }
//        });
//
//        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);






    }

    protected String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }

    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public void downloadImagesButton(View v) {
        GridLayout g = (GridLayout)findViewById(R.id.image_grid);
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.tennis_ball);


        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(iv);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);
//
//        iv.setLayoutParams(params);


//        iv.getLayoutParams().height = (int) getResources().getDimension(R.dimen.image_height);
//        iv.getLayoutParams().width = (int) getResources().getDimension(R.dimen.image_width);
        g.addView(iv);
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

    private ArrayList<String> getPicturesUrl(String url){
        final ArrayList<String> picturesUrls = new ArrayList<>();
        Ion.with(this).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                try {
                    JSONObject xmlJSONObj = XML.toJSONObject(result);
                    String jsonPrettyPrintString = xmlJSONObj.toString();
//                    System.out.println(jsonPrettyPrintString );
                    JSONObject json1 = new JSONObject(jsonPrettyPrintString );
                    JSONObject json2 = new JSONObject(json1.getString("response"));
                    JSONObject json3 = new JSONObject(json2.getString("data"));
                    JSONObject json4 = new JSONObject(json3.getString("images"));
                    JSONArray jsonArray = json4.getJSONArray("image");
                    for(int i = 0 ;i < jsonArray.length(); i++) {
                        JSONObject json5 = new JSONObject(jsonArray.get(i).toString());
//                        json5.getString("url");
                        picturesUrls.add(json5.getString("url"));
//                        picturesUrls.add(jsonArray.get(i).toString().replace("\"", ""));
//                        System.out.println(jsonArray.get(i));
                    }
                    for(String s : picturesUrls){
                        System.out.println(s);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        });
        return picturesUrls;
    }





}
