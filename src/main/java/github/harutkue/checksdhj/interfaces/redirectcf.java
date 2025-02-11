package github.harutkue.checksdhj.interfaces;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

import github.harutkue.checksdhj.interfaces.getnet.getdns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;

public class redirectcf implements Access_Templature_Interface{
    public boolean CheckMethods(String Domain){
        //CloudFlareかどうかを確認するやつ。
        //Aレコードを取得する。
        getdns get_records = new getdns();
        List<String> Check_Records = get_records.getArecords(Domain);
        List<String> serverData = get_records.getService(Check_Records);
        for(String  resultdns:serverData){
            if( resultdns == "cloudflare"){
                return true;
            }
        }
        //1個しか対応できない
        return false;

    }
    public String Main_Access(String Access_Domain){
        //プロキシの確認を行う。
        String return_str = "";
        try{
            URL url = new URL("http://"+ Access_Domain);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);

            Map<String,List<String>> header = connection.getHeaderFields();
            connection.disconnect();
            
            if (header.containsKey("Server") && header.get("Server").contains("cloudflare")){
                return_str = "cloudflare:" + Access_Domain + "は適切に設定されています。";
                return return_str;
            }
            if (header.containsKey("CF-Cache-Status")){
                return_str = "cloudflare:" + Access_Domain +"は適切に設定されています";
            }
            if (header.containsKey("CF-RAY")){
                return_str = "cloudflare:" + Access_Domain +"は適切に設定されています。";
            }
        }catch(Exception e){
            
        }
        return_str ="cloudflare:" +Access_Domain +"は適切に設定されていません。";
        return return_str;
    }
       public List<String> getProviderService(){
        List<String> ProviderList = new ArrayList<>();
        //ここに追加するプロバイダを記述する。
        //ProviderList.put("addressの一部","provider")
        ProviderList.add("104.16.");
        ProviderList.add("104.17.");

        return ProviderList;
    }
}
