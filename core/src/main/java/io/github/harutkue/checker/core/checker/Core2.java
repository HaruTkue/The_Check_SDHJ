package io.github.harutkue.checker.core.checker;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.*;
import java.util.regex.Pattern;
//import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
//import java.net.URLClassLoader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.MulticastChannel;
import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;


//jsonロード用
import org.json.JSONObject;
import org.json.JSONArray;

//exsearch確認
import io.github.harutkue.checker.core.exsearch.*;

public class Core2 {
    //CNAMEレコード取得
    public static CheckerRecords GetCnameRecord(String DomainData){
        try{
            Lookup lookup = new Lookup(DomainData, Type.CNAME);
            lookup.run();
            if(lookup.getResult() == Lookup.SUCCESSFUL){
                Record[] records = lookup.getAnswers();
                for(Record record:records){
                    CNAMERecord CNAME = (CNAMERecord) record;
                    String ToString = CNAME.getTarget().toString();
                    return new CheckerRecords(ToString,"CNAME", DomainData);
                }
            }else{
                CheckerRecords Rec = GetARecord(DomainData);
                return Rec;
            }
        }catch(Exception e){
            //DNSでError -アクセス×
            e.printStackTrace();
            return new CheckerRecords("101","Error",DomainData);
        }
        return null;
    }
    //ARecord取得
    public static CheckerRecords GetARecord(String DomainData){
        try{
            Lookup lookup = new Lookup(DomainData, Type.A);
            lookup.run();
            if(lookup.getResult() == Lookup.SUCCESSFUL){
                Record[] records = lookup.getAnswers();
                for(Record record:records){
                    ARecord A = (ARecord) record;
                    String ToString = A.getName().toString();
                    return new CheckerRecords(ToString,"A", DomainData);
                }
            }else{
                return new CheckerRecords("2","Nothing",DomainData);
            }
        }catch(Exception e){
            //DNSでエラー発生
            e.printStackTrace();
            return new CheckerRecords("101","Error",DomainData);
        }
        return null;
    }
    //単独検知処理
    public static Map<String,String> OneSearch(CheckerRecords CheckDatas){
        try{
            //定義ファイル読み出し
            InputStream IS = Core2.class.getClassLoader().getResourceAsStream("checkfile/DefineCheck.json");
            String JsonString = new String(IS.readAllBytes(),StandardCharsets.UTF_8);
            JSONArray DataList = new JSONArray(JsonString);

            Map<String,String> RetrunValue = new HashMap<>();

            int CheckCount = DataList.length();
            System.out.println("検知処理IS読み出し完了");
            for (int i = 0; i< CheckCount; i++){
                JSONObject CheckService = DataList.getJSONObject(i);
                String ServiceName = CheckService.getString("Name");


                JSONArray CheckValues = null;
                Boolean ServicePatten = CheckService.getBoolean("PassStatus");

                if(CheckDatas.RecordType() == "CNAME"){
                    CheckValues = CheckService.getJSONArray("CNAMEIdentifier");
                }else if(CheckDatas.RecordType() =="A"){
                    CheckValues = CheckService.getJSONArray("ARecordIdentifier");
                }else if(CheckDatas.RecordType() == "Error" || CheckDatas.RecordType() =="Nothing"){
                    //データをさっさと作ってreturnする
                    Map<String,String> Result = new HashMap<>();
                    Result.put(CheckDatas.DomainData(),CheckDatas.Record());
                    return Result;
                }else{
                    
                }
                JSONArray CheckPatterns = CheckValues;
                
                //検知処理開始
                if(CheckValues == null){
                    continue;
                }

                for (int j=0; j < CheckValues.length(); j++){
                    Pattern CHECKPATTERN = Pattern.compile(CheckPatterns.getString(j));
                    if(CHECKPATTERN.matcher(CheckDatas.Record()).matches()){
                        //パターンマッチ後

                        //合致した場合にて
                        if(ServicePatten){
                            String CehckStr = CheckPatterns.getString(j);
                            Map<String,String> Value = SearchAns3(CheckDatas);
                            //ここで構築して消す
                            RetrunValue = Value;
                            //Returnして終了
                            return RetrunValue;
                        }else{
                            //分岐 NSearch可否を判定する
                            String SearchFP = CheckService.getString("fingerprint");


                            //NSearchの可否で状態を問う
                            if(SearchFP.equals("NSearch")){
                                //NSearch実行
                                return SearchN(CheckDatas);

                            }else{
                                ExSearch ex = new ExSearch();
                                String CheckValue  = ex.guideData(CheckDatas, ServiceName, SearchFP);

                                String FirstValue  =null;
                                Map<String,String> values  = new HashMap<>();
                                if(CheckValue == null){
                                    //空のデータ
                                    
                                }
                                if (CheckValue.contains(":OK")){
                                    //putで成否を記録
                                    FirstValue = CheckValue.substring(0, CheckValue.indexOf(":OK"));
                                    values.put(FirstValue,"1");
                                }else if(CheckValue.contains(":NG")){
                                    FirstValue = CheckValue.substring(0, CheckValue.indexOf(":OK"));
                                    values.put(FirstValue,"0");
                                }else if(CheckValue.contains(":Nt")){
                                    FirstValue = CheckValue.substring(0,CheckValue.indexOf(":Nt"));
                                    values.put(FirstValue,"2");
                                }
                                return values;
                            }

                            //EXの場合はこの処理に追加して状態を特定できるようにする.

                        }
                        
                    }else{
                        System.out.println("通過");
                        continue;
                    }
                    //この場合は検知ステータスが全くなかったケース
                    
                }
                Map<String,String> Value = new HashMap<>();
                Value.put(CheckDatas.DomainData(),"3");
                continue;
            }
            return null;
        }catch (IOException e){
            //どこかのエラー
            e.printStackTrace(); 
            Map<String,String> ErrorRq = new HashMap<>();
            ErrorRq.put(null,"301");

            return ErrorRq;
        }
    }

    //ホスト云々
    public static Map<String,String> SearchAns3(CheckerRecords CheckData){
        String CheckDomainRecord = CheckData.Record().replaceAll("\\.$", "");
        String url="https://" + CheckDomainRecord;
        Map<String,String> answer = new HashMap<>(); 
        String ReturnValue;
         try(CloseableHttpClient client = HttpClients.createDefault()){
            //ホスト名の指定
            HttpGet request = new HttpGet(url);
            request.setHeader("Host",CheckData.Record());
            //System.out.println("リクエスト内容");
            //System.out.println(request);
            try(CloseableHttpResponse response = client.execute(request)){
                int responseCode = response.getCode();
                //System.out.println("リクエスト結果:" + responseCode);
                //判定
                if (responseCode >= 200 && responseCode < 400){
                    ReturnValue = "1";
                }else{
                    ReturnValue = "0";
                }
                answer.put(CheckData.DomainData(),ReturnValue);
            }
        }catch(Exception e){
            //誤りコード
            answer.put(CheckData.DomainData(),"201");
            e.printStackTrace();
        }
        return answer;
    }
    //一般向け
    public static Map<String,String> SearchN(CheckerRecords CheckData){
        String CheckDomainRecord = CheckData.Record().replaceAll("\\.$", "");
        String url="https://" + CheckDomainRecord;

        String ReturnValue = "";

        Map<String,String> answer = new HashMap<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);

            //connection
            connection.connect();
            int responseCode = connection.getResponseCode();
            //System.out.println("リクエスト結果:" + responseCode);
            //判定
            if (responseCode >= 200 && responseCode < 400){
                ReturnValue = "1";
            }else{
                ReturnValue = "0";
            }

            //ReturnValueの内容からMapを作る
            answer.put(CheckData.DomainData(),ReturnValue);
            
        }catch (IOException e){
            //誤りコード
            answer.put(CheckData.DomainData(),"201");
            e.printStackTrace();
        }
        return answer;

    }
    //DNSリクエスト込みの検知処理
    public static List<Map<String,String>> GetRequestValue(Object DomainData){
        //DNSへのリクエストを行うやつ
        List<Map<String,String>> ReturnList = new ArrayList<>();

        //入力データの型check
        if(DomainData instanceof String){
            //単独データ
            System.out.println("Search分岐");
            CheckerRecords Record = GetCnameRecord(DomainData.toString());
            System.out.println(Record);
            //ViaList がいらないように調節
            ReturnList.add(OneSearch(Record));

        }else if(DomainData instanceof List<?> MultiDomainData){
            List<String> AnsToList = new ArrayList<>();
            List<CheckerRecords> MultiSearchList = new ArrayList<>();
            for (Object CheckDomains : MultiDomainData){
                int count = 0;
                String CheckDoaminsData = CheckDomains.toString();
                
                //存在しないケースを狙う
                CheckerRecords Record = null;
                List<String> ViaList = new ArrayList<>();

                if(CheckDoaminsData == "" | CheckDoaminsData == null){
                    //nullから特定のステータスを用いたものに変更する処理を書こう
                    continue;
                }else{
                    //レコード取得
                    Record = GetCnameRecord(CheckDoaminsData);
                    System.out.println(Record);
                    MultiSearchList.add(Record);
                }
            }
            //作成したCheckerRecordsからMSeacrhを狙う
            ReturnList = MSearch(MultiSearchList);
        }
        return ReturnList;
    }
    //複数データのsearchを行う

    //CheckerRecord成型用
    public static CheckerRecords CreateCheckerRecords(String Record , String RecordType,String CheckDomain){
        CheckerRecords RetrunRecords = new CheckerRecords(Record,RecordType,CheckDomain);
        return RetrunRecords; 
    }

    //成形されたデータ複数検知
    public static List<Map<String,String>> GetValue(List<List<String>> DomainData){
        return null;
    }
    public static List<Map<String,String>> MSearch(List<CheckerRecords> MultiSearchList){
        List<Map<String,String>> answer = new ArrayList<>();
        for(CheckerRecords Records : MultiSearchList){
            //検知処理
            Map<String,String> Result = OneSearch(Records);
            //分岐用意
            answer.add(Result);
        }

        return answer;
    }
    
}
