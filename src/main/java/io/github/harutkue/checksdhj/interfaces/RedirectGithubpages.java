package io.github.harutkue.checksdhj.interfaces;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;



public class RedirectGithubpages implements AccessTemplatureInterface {
    private static final Pattern GITHUB_PAGES = Pattern.compile(".*\\.github\\.io\\.?$");
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
        String url ="http://" + AccessDomain;
        String ReturnValue;
        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);

            //アクセスを仕掛ける。
            connection.connect();

            int responseCode = connection.getResponseCode();
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
