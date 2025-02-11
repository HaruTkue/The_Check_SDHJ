package github.harutkue.checksdhj.interfaces;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import javax.naming.directory.InitialDirContext;

import github.harutkue.checksdhj.interfaces.getnet.getdns;

import java.net.InetAddress;
import java.net.Socket;

public class Redirect_Https implements Access_Templature_Interface {
    public boolean CheckMethods(String Domain){
        ///debug
        System.out.println("httpsの処理---------------");
        try{
            InetAddress address = InetAddress.getByName(Domain);
            try (Socket socket = new Socket(address,443)){
                return true;
            }
        }catch (IOException e){
            System.out.println("エラーが発生しました。");
        }
        return false;
    }
    public String Main_Access(String Access_Domain){
        String url ="http://" + Access_Domain;
        
        System.out.println("検索するドメイン:"+ url);
        String Return_Value;
        try{
           

            //Httpリクエストのやつ
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            System.out.println("検索するドメイン:"+ new URL(url));
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);
            
            //アクセスを仕掛ける。
            connection.connect();
            //デバッグ用

            System.out.println(connection.getResponseCode());
            int responseCode = connection.getResponseCode();
            if (responseCode >=200 && responseCode < 400){
                System.out.println("適切に取得できました。");
                Return_Value = Access_Domain+ "との疎通が取れました";
            }else{
                Return_Value = Access_Domain+"にはサブドメインハイジャックのリスクがあります!";
            }
            System.out.println(Return_Value);
            return Return_Value;
        }catch (Exception e){
            return "Error";
        }
    }
}