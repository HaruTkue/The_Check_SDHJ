package github.harutkue.checksdhj.interfaces;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;


public class redirectWordpress implements Access_Templature_Interface {
    private static final Pattern WORD_PRESS = Pattern.compile(".*\\.(wordpress\\.com|wpcomstaging\\.com)\\.?$");
    public boolean CheckMethods(String Domain){

        if(WORD_PRESS != null){
            
            if (WORD_PRESS.matcher(Domain).matches()){
                return true;
            }else{
                return false;

            }
        }
        return false;
    }
    public String Main_Access(String Access_Domain){
        String url ="http://" + Access_Domain;
        String Return_Value;
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
                Return_Value = Access_Domain + ":OK";
            }else{
                Return_Value = Access_Domain +":NO";
            }
            return Return_Value;

        }catch (Exception e){
            e.printStackTrace();
        }
        return "return";

    }
    public List<String> getProviderService(){
        return null;
    }
}
