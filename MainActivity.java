package com.example.anxinyang.helloworld;

import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Connector connector;
    private boolean answered=false;
    private String token;
    private Questions question;
    private TextView debugTest;
    private Thread thread;

    public String dToken;
    int point=3;

    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        debugTest = (TextView) findViewById(R.id.db_text);

        debugTest.setMovementMethod(new ScrollingMovementMethod());
        debugTest.setText("Start\n");


        connector=Connector.getConnect(this);

        debugTest.append("Connector created.\n");
         mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        token = connector.getToken();
                        debugTest.append("Got Token:"+token+"\n");
                        setContentView(R.layout.activity_question);
                        TextView tQuestion=(TextView)findViewById(R.id.questionBody);
                        tQuestion.setText("Press Start");
                        Button summit=(Button)findViewById(R.id.bSummit);
                        summit.setVisibility(View.INVISIBLE);
                        break;
                    case 0:
                        debugTest.append("Error:Can't get token\n");
                        break;
                    case 2:
                        debugTest.append("Error:Can't get question\n");
                        break;
                    case 3:
                        //TextView tQuestion=(TextView)findViewById(R.id.questionBody);
                        question=Questions.getIns(connector.getQuestion());
                        setupQuestion();


                }


            }
        };



    }
    @Override
    protected void onStart(){
        super.onStart();

    }
    public  void  buttonOnClick(View v) {
        connector.reset();
        debugTest.append("Login button Clicked\n");
        String user=((EditText)findViewById(R.id.nameLogin_t)).getText().toString();
        String pass=((EditText)findViewById(R.id.passwordLogin)).getText().toString();

        connector.requestToken(user,pass);
        debugTest.append("Login as"+user+"\n");

        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                Message msg = new Message();

                while(msg.what!=1&&i<10000) {
                        if (connector.getToken().contains("Error")) {
                            msg.what = 0;
                        } else {
                            msg.what = 1;

                        }
                    i++;
                }

                mHandler.sendMessage(msg);
            }
        });


        thread.start();
    }
    public void getQOnClick(View v){
        Button summit=(Button)findViewById(R.id.bSummit);
        summit.setVisibility(View.VISIBLE);
        answered=false;
        debugTest.append("Requesting Quetion\n");
        connector.reset();
        connector.requestQuestion(token);
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                Message msg = new Message();

                while(msg.what!=3&&i<1000000) {
                    if (connector.getQuestion()==null) {
                        msg.what = 2;
                    } else {
                        msg.what = 3;

                    }
                    i++;
                }

                mHandler.sendMessage(msg);
            }
        });


        thread.start();
    }

    private void setupQuestion(){
        question=Questions.getIns(connector.getQuestion());
        TextView tQuestion=(TextView)findViewById(R.id.questionBody);
        try {
            tQuestion.setText(question.getQuestion());
            tQuestion.append("Error:"+question.getQuestion());
            CheckBox[] checkButtons = new CheckBox[6];
            checkButtons[0] = (CheckBox) findViewById(R.id.cAnwserA);
            checkButtons[1] = (CheckBox) findViewById(R.id.cAnwserB);
            checkButtons[2] = (CheckBox) findViewById(R.id.cAnwserC);
            checkButtons[3] = (CheckBox) findViewById(R.id.cAnwserD);
            checkButtons[4] = (CheckBox) findViewById(R.id.cAnwserE);
            checkButtons[5] = (CheckBox) findViewById(R.id.cAnwserF);
            for(int i=0;i<6;i++){
                checkButtons[i].setText(question.getAnwsers(i));
                checkButtons[i].setChecked(false);
            }
        }catch (Exception e){
            tQuestion.append("Error:"+question.getQuestion());
        }
        try {



            tQuestion.setText(question.getQuestion());
            //tQuestion.setText(question.getString("question"));
        } catch (Exception e){
            tQuestion.setText("Press Start");
        }
    }
    public void summitOnClick(View v){
        boolean[] summit=new boolean[6];
        TextView tQuestion=(TextView)findViewById(R.id.questionBody);
        TextView tPoints=(TextView)findViewById(R.id.tPoints);

        CheckBox[] checkButtons=new CheckBox[6];
        checkButtons[0] =(CheckBox) findViewById(R.id.cAnwserA);
        checkButtons[1] =(CheckBox)findViewById(R.id.cAnwserB);
        checkButtons[2] =(CheckBox)findViewById(R.id.cAnwserC);
        checkButtons[3] =(CheckBox)findViewById(R.id.cAnwserD);
        checkButtons[4] =(CheckBox)findViewById(R.id.cAnwserE);
        checkButtons[5] =(CheckBox)findViewById(R.id.cAnwserF);
        for(int i=0;i<6;i++){
            if(checkButtons[i].isChecked())summit[i]=true;
        }
        if(!answered) {
            if (question.checkSummit(summit)) {
                tQuestion.setText("Correct");
                point++;
                point++;
                answered = true;

            } else {
                tQuestion.setText("Wrong");
                point--;
                answered = true;
            }
        }
        for (int i=0;i<6;i++){
            if(question.getKeys(i))checkButtons[i].setTextColor(Color.GREEN);
        }
        tPoints.setText("Points: "+point);
    }



    public void resetDebug(View v) {
        debugTest.setText("");
    }
}

