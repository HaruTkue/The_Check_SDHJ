package io.github.harutkue.checker.core.checker;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

//jsonロード用
import org.json.JSONObject;
import org.json.JSONArray;

public class Core {
    public <T> void GetRequestValue(T DomainData){
        //テスト用のやつ
        System.out.println("value:"+ DomainData.toString());
        //どのような型が設定されているかを判別する
        if(DomainData instanceof String) {
            //文字列型の場合(単独データの場合)
            CheckerRecords Record = GetCnameRecord(DomainData.toString());
            System.out.println(Record);
            String Anser = OneSearch(Record);

            System.out.println(Anser);
            //OneSearch
        }else if (DomainData instanceof List<?>){
            //複数のドメインを同時で調べる場合 - eTLD対応
        }
    }
    //CNAMEレコードを取得する際に自動的に行う処理。 --単独も複数も可。
    public CheckerRecords GetCnameRecord(String DomainData){
        //CNAMEレコードを取得する
        try{
            Lookup lookup = new Lookup(DomainData,Type.CNAME);
            lookup.run();

            //CNAMEレコードが見つかった場合
            if(lookup.getResult() == Lookup.SUCCESSFUL){
                Record[] records = lookup.getAnswers();
                for (Record record: records){
                    CNAMERecord CNAME = (CNAMERecord) record;
                    String ToString   = CNAME.getTarget().toString();
                    return new CheckerRecords(ToString,"CNAME",DomainData);
                }
            }else{
                //Aレコードではないかの検知を行う。
                return GetARecord(DomainData);
            }
        }catch(TextParseException e){
            //処理に不具合が発生した。
            e.printStackTrace();
            return null;
        }
        return null;
        
    }
    //Aレコードを取得する処理 --単独も複数も可。
    public CheckerRecords GetARecord(String DomainData){
        try{
            Lookup lookup = new Lookup(DomainData, Type.A);
            lookup.run();
            //Aレコードを取得する
            if(lookup.getResult() == Lookup.SUCCESSFUL){
                Record[] records = lookup.getAnswers();
                for (Record record : records){
                    ARecord A = (ARecord) record;
                    String ToString = A.getName().toString();
                    return new CheckerRecords(ToString,"A",DomainData);
                }
            }
        }catch(TextParseException e){
            //不具合発生時に送るやつ
            e.printStackTrace();
            return null;
        }
        return null;
    }
    //検知機能本体-単独用
    public String OneSearch(CheckerRecords CheckDatas){
        //単独用の検知処理
        String RetrunValue="";
        try{
            //JSONから検知用データの取得
            InputStream IS = Core.class.getClassLoader().getResourceAsStream("checkfile/DifineCheck.json");
            String JsonString = new String(IS.readAllBytes(),StandardCharsets.UTF_8);
            //JSONのデータを取得した。
            JSONArray CheckList = new JSONArray(JsonString);

            //検知処理に移行
            //要素数を取得して検知する
            int CheckCount = CheckList.length();
            System.out.println(CheckList);
            System.out.println(CheckCount);
            //JSONにあるすべてのサービスとの整合をチェックする。
            for(int i=0; i<CheckCount;i++){
                //繰り返しでパターンを取得する。
                JSONObject CheckService = CheckList.getJSONObject(i);
                //必要なデータを取得する
                String ServiceName = CheckService.getString("Name");
                //StatusCodeでの検知が可能かどうか
                Boolean SerivcePattern= CheckService.getBoolean("PassStatus");
                //Check処理に受け渡す専用のCheck用データ
                JSONArray CheckValues =null;
                if (CheckDatas.RecordType() == "CNAME"){
                    CheckValues = CheckService.getJSONArray("CNAMEIdentifier");
                    System.out.println(CheckValues);
                }else if (CheckDatas.RecordType()=="A"){

                }else{

                }
                ///
                /// そもそもpattern判定からはじくケースを作成する -->多分ここにねじ込んだほうが早い;
                /// 
                if(SearchNet(CheckDatas,new CheckServiceRecords(ServiceName,CheckValues))){
                    //正しいと判定してパスする
                    System.out.println("分岐T");
                    continue;
                }else{
                    //正しくないと判定して、ダメというデータを返す。
                    System.out.println("分岐F");
                    RetrunValue = CheckDatas.DomainData()+":NG";
                    
                    return RetrunValue;

                }
            }
            //チェックし終わった場合に、結果を作成する
            RetrunValue = CheckDatas.DomainData()+":OK";
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public Boolean SearchNet(CheckerRecords CheckDatas,CheckServiceRecords ServiceDatas){
        //検知処理を実行する
        JSONArray CheckPatterns  = ServiceDatas.SerivcePattern();
        for(int i=0; i<CheckPatterns.length(); i++){
            //パターンを生成する
            Pattern CHECKPATTERN =Pattern.compile(CheckPatterns.getString(i));
            //パターンとCNAMEレコード/Aレコードが合致しているか
            if(CHECKPATTERN.matcher(CheckDatas.Record()).matches()){
                
            }else{
                
            }
            ///
            /// ここからjava.netを活用してステータスコードを取得する処理
            /// 
            String DomainRecord = CheckDatas.DomainData().replaceAll("\\.$","");
            String url ="https://" + DomainRecord;
            System.out.println(url);
            try{
                //HttpClientの作成
                HttpClient client = HttpClient.newHttpClient();
                URI uri = new URI(url);
                //Requestの作成
                System.setProperty("jdk.httpclient.allowRestrictedHeaders", "host");
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("User-Agent","Java HttpClient")
                    .header("host",CheckDatas.DomainData())
                    .GET()
                    .build();
                //Responseの取得
                HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
                int ResponseCode = response.statusCode();
                //httpのステータスから判定する
                if(ResponseCode >= 200 && ResponseCode <400){
                    return true;
                }else{
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }
    public Boolean Serach(CheckerRecords CheckDatas , CheckServiceRecords ServiceDatas){
        ///
        /// ここからjava.netを活用してステータスコードを取得する処理
        /// 
        String DomainRecord = CheckDatas.DomainData().replaceAll("\\.$","");
        String url ="https://" + DomainRecord;
        System.out.println(url);
        try{
            //HttpClientの作成
            HttpClient client = HttpClient.newHttpClient();
            URI uri = new URI(url);
            //Requestの作成
            System.setProperty("jdk.httpclient.allowRestrictedHeaders", "host");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("User-Agent","Java HttpClient")
                    .header("host",CheckDatas.DomainData())
                    .GET()
                    .build();
                //Responseの取得
                HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
                int ResponseCode = response.statusCode();
                //httpのステータスから判定する
                if(ResponseCode >= 200 && ResponseCode <400){
                    return true;
                }else{
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return null;
    }
}
