package com.example.anxinyang.helloworld;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anxin Yang on 6/20/2016.
 */
public class Questions {
    private String question;
    private String[] anwsers=new String[6];
    private boolean[] key=new boolean[6];
    private Questions(JSONObject jQuestion){
        try {
            question= jQuestion.getString("question");
            anwsers[0]=jQuestion.getString("answer_a");
            anwsers[1]=jQuestion.getString("answer_b");
            anwsers[2]=jQuestion.getString("answer_c");
            anwsers[3]=jQuestion.getString("answer_d");
            anwsers[4]=jQuestion.getString("answer_e");
            anwsers[5]=jQuestion.getString("answer_f");
            int i=0;
            while(i<jQuestion.getString("correct").length()){
                if(jQuestion.getString("correct").charAt(i)=='a')key[0]=true;
                if(jQuestion.getString("correct").charAt(i)=='b')key[1]=true;
                if(jQuestion.getString("correct").charAt(i)=='c')key[2]=true;
                if(jQuestion.getString("correct").charAt(i)=='d')key[3]=true;
                if(jQuestion.getString("correct").charAt(i)=='e')key[4]=true;
                if(jQuestion.getString("correct").charAt(i)=='f')key[5]=true;
                i++;
            }
        } catch (Exception e) {
        }
    }
    public static Questions getIns(JSONObject jQuestion){
        return new Questions(jQuestion);
    }
    public String getAnwsers(int index){
        return anwsers[index];
    }
    public String getQuestion(){
        return this.question;
    }
    public boolean checkSummit(boolean[] summit) {
        for(int i=0;i<6;i++){
            if(key[i]!=summit[i]){
                return false;
            }
        }
        return true;
    }
    public boolean getKeys(int index){

        return key[index];
    }
}
