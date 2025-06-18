package io.github.harutkue.checker.core.exsearch;
import io.github.harutkue.checker.core.checker.CheckerRecords;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
//jsonロード用
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExSearch {
    public String guideData(CheckerRecords CheckData,String SearchStatus,String SearchFP){
        //どう分岐させる?
        String Ans ="";

        try{
            //Methodを検索する
            Method method = this.getClass().getMethod(SearchStatus,CheckerRecords.class,String.class);
            Ans = (String) method.invoke(this, CheckData,SearchFP);

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return Ans;
    }
    //WordPress用の検知データ
    public String wordPress(CheckerRecords CheckData, String fingerprint){
        //HTMLファイルを検索する
        //チェック用のデータを取る
        String CheckDomainRecord = CheckData.Record().replaceAll("\\.$","");
        String SearhcUrl= "https://" + CheckDomainRecord;
        String ReturnValue="";

        try{
            URL url = new URL(SearhcUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            //結果を探る
            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 400) {
                //このケースの場合にファイル検査をする
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
                );
                
                StringBuilder htmlContent = new StringBuilder();
                String line = reader.readLine();
                while (line != null){
                    htmlContent.append(line).append("\n");
                    line = reader.readLine();
                }
                reader.close();
                //検索
                String htmlData = htmlContent.toString();
                if(htmlData.contains(fingerprint)){
                    ReturnValue = CheckData.DomainData() + ":NG";
                }else{
                    ReturnValue = CheckData.DomainData() + ":OK";
                }
            } else {
                ReturnValue = CheckData.DomainData() + ":NG";
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return ReturnValue;
    }
}
