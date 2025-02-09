package org.Interface;

import java.net.HttpURLConnection;
import java.net.URL;

//
public class Redirect_Http implements Access_Templature_Interface {
    public boolean CheckMethods(String Test_URL){

        return true;
    }
    public String Main_Access(String Access_URL){
        String url ="http://" + Access_URL;
        String Return_Value;
        try{
            //Httpリクエストのやつ
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode >=200 && responseCode < 400){
                Return_Value = Access_URL+ "との疎通が取れました";
            }else{
                Return_Value = Access_URL+"にはサブドメインハイジャックのリスクがあります!";
            }
            System.out.println(Return_Value);
            return Return_Value;
        }catch (Exception e){
            return "Error";
        }
    }
}