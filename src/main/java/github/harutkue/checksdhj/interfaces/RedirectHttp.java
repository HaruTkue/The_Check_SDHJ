package github.harutkue.checksdhj.interfaces;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.directory.InitialDirContext;

import java.net.InetAddress;
import java.net.Socket;

public class RedirectHttp implements AccessTemplatureInterface {
    
    public boolean CheckMethods(String Domain){
        ///debug
        System.out.println("httpでの処理-----------------");
        try{
            InetAddress address = InetAddress.getByName(Domain);
            try (Socket socket = new Socket(address,80)){
                return true;
            }
        }catch (IOException e){
            System.out.println("適切にhttpを取得することができませんでした。");
        }
        return false;
    }
    public String MainAccess(String AccessDomain,String Domain){
        String url ="http://" + AccessDomain;
        
        System.out.println("検索するドメイン:"+ url);
        String ReturnValue;
        try{
           

            //Httpリクエストのやつ
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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
                ReturnValue = AccessDomain+ "との疎通が取れました:http";
            }else{
                ReturnValue = AccessDomain+"疎通確認ができませんでした(リスクあり)";
            }
            return ReturnValue;
        }catch (Exception e){
            return "Error";
        }
    }
       public List<String> getProviderService(){
        return null;
    }
}