package kr.anima.xd.s.jsontest;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt= (TextView) findViewById(R.id.txt);
    }

    public void clickBtn(View v){

        AssetManager manager=getAssets();
        try {

//            InputStream is=manager.open("jsons/test.json"); // 대문자, 한글 상관없으나 한글은 지양
//            InputStreamReader isr=new InputStreamReader(is);
//            BufferedReader reader=new BufferedReader(isr);
//            StringBuffer buffer=new StringBuffer();
//            String line=reader.readLine();
//            while (line!=null){
//                buffer.append(line);
//                line=reader.readLine();
//            } // while
//
////            txt.setText(buffer.toString());
//            // json을 관리하는 객체
////            JSONObject jsonObject=new JSONObject(buffer.toString()); // json 객체 하나일 때 {}
////            String name= jsonObject.getString("name"); // 같은 키값이면 덮어쓰기 됨
////            int age=jsonObject.getInt("age");
//
//            JSONArray jsonArray=new JSONArray(buffer.toString()); // json 배열일 때 []
//            for(int i=0; i<jsonArray.length(); i++){
//                JSONObject jsonObject=jsonArray.getJSONObject(i);
//                String name= jsonObject.getString("name"); // 같은 키값이면 덮어쓰기 됨
//                int age=jsonObject.getInt("age");
//                txt.append(name+age+"\n");
//            }


            InputStream is=manager.open("jsons/test2.json"); // 대문자, 한글 상관없으나 한글은 지양
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader reader=new BufferedReader(isr);
            StringBuffer buffer=new StringBuffer();
            String line=reader.readLine();
            while (line!=null){
                buffer.append(line);
                line=reader.readLine();
            } // while

//            txt.setText(buffer.toString());
            // json 객체 안에 json으로 값을 가지고 있을 때
            JSONObject jsonObject=new JSONObject(buffer.toString());
            String name= jsonObject.getString("name");
            JSONObject item=jsonObject.getJSONObject("item");
            int no=item.getInt("no");
            int age=item.getInt("age");
            String location=item.getString("location");

            txt.setText(no+name+age+location+"\n");

            JSONArray contacts=jsonObject.getJSONArray("contacts");
            for(int i=0; i<contacts.length(); i++){
                JSONObject cons=contacts.getJSONObject(i);
                String homeName=cons.getString("name");
                String number=cons.getString("number");
                txt.append(homeName+number+"\n");
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // clickBtn

    public void clickSave(View v){

        String name="Lee";
        int age=30;


        try {
            // 내 기기에 data.json 으로 파일 저장
            FileOutputStream fos=openFileOutput("data.json", MODE_APPEND);
            PrintWriter writer=new PrintWriter(fos);
//            writer.print(name);
//            writer.println(age);

//            String jsonStr="{\""+name+\":"+name+"}"; // .............. ㄱ-
            JSONObject data=new JSONObject();
            data.put("name", name);
            data.put("age", age);

            String jsonStr=data.toString();
            writer.print(jsonStr);

            writer.flush();
            writer.close();

            txt.setText(jsonStr);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    } // clickSave

    public void clickLoad(View v){

        // 인터넷 퍼미션 추가
        final String serverUrl="http://sooong03.dothome.co.kr/170719/data.json";
        new Thread(){
            @Override
            public void run() {

                try {
                    URL url=new URL(serverUrl);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);

                    InputStream is=connection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader reader=new BufferedReader(isr);
                    StringBuffer buffer=new StringBuffer();

                    String line=reader.readLine();
                    while (line!=null){
                        buffer.append(line);
                        line=reader.readLine();
                    } // while

                    JSONObject jsonObject=new JSONObject(buffer.toString());
                    final String name=jsonObject.getString("name");
                    final String msg=jsonObject.getString("msg");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.append(name+msg);
                        }
                    });


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }.start();

    } // click Load

}
