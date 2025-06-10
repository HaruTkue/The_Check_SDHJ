package io.github.harutkue.checker.core.interfaces;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

public class RedirectVercel implements AccessTemplatureInterface {
    //vercel-dns.com
    private static final Pattern GITHUB_PAGES = Pattern.compile(".*\\.vercel\\.app\\.?$|.*\\.vercel-dns\\.com\\.?$");
    public boolean CheckMethods(String Domain){
        if(Domain != null){
            
            if (GITHUB_PAGES.matcher(Domain).matches()){
                return true;
            }else{
                return false;

            }
        }
        return false;
    }
    public String MainAccess(String AccessDomain,String Domain){
        String url ="https://" + AccessDomain;
        String ReturnValue;
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "host");
        try{
            //処理部の変更
            HttpClient client = HttpClient.newHttpClient();
            URI uri = new URI(url);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", "Java HttpClient")
                .header("host",Domain)
                .GET()
                .build();
            /* 
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);
            //connectionに追加でヘッダ情報を足す。
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept","application/json");
            connection.setRequestProperty("Host", "example.com");
            
            //keyのデバッグ用
             for (Map.Entry<String, List<String>> entry : connection.getRequestProperties().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
            System.out.println(connection);
            //アクセスを仕掛ける。
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode >= 200 && responseCode <400){
                ReturnValue = AccessDomain + ":OK";
            }else{
                ReturnValue = AccessDomain +":NG";
            }
            return ReturnValue;
            */
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        //リターンをとる
        int responseCode = response.statusCode();
        if(responseCode >= 200 && responseCode <400){
            ReturnValue = AccessDomain + ":OK";
        }else{
            ReturnValue = AccessDomain +":NG";
        }
        return ReturnValue;
        
        }catch (Exception e){
            e.printStackTrace();
        }
        return "return";

    }
    public List<String> getProviderService(){
        return null;
    }
}
