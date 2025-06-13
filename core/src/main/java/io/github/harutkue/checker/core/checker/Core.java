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

public class Core {
    public List<HashMap<String,Boolean>> GetRequestValue(Object DomainData) {
        // テスト用のやつ
        List<HashMap<String,Boolean>> ReturnList = new ArrayList<>();
        System.out.println("データの種類を調べる処理------");
        System.out.println("value:" + DomainData.toString());
        
        // どのような型が設定されているかを判別する
        if (DomainData instanceof String) {
            // 文字列型の場合(単独データの場合)
            CheckerRecords Record = GetCnameRecord(DomainData.toString());
            System.out.println(Record);
            // 解釈用のList化
            List<String> ViaList = new ArrayList<>();
            ViaList.add(OneSearch(Record));
            ReturnList = ListCreate(ViaList);

            //System.out.println(ReturnList);
            // OneSearch

        } else if (DomainData instanceof List<?> MultiDomainList) {
            // 複数のドメインを同時で調べる場合 - eTLD対応
            List<String> AnsToList = new ArrayList<>();
            //System.out.println("複数ドメイン対応");
            //ドメインリストの回数分実行する。
            for (Object CheckDomains : MultiDomainList){
                int count = 0;
                //("複数探知処理-----");
                //ここにマルチで使用する際のドメインを入力していく。
                String CheckDomainsData = CheckDomains.toString();
                //CheckerRecords
                //ここでnull対策
                CheckerRecords Record = null;
                //debug
                System.out.println(CheckDomainsData);
                if(CheckDomainsData == "" | CheckDomainsData == null){
                    continue;
                }else{
                Record = GetCnameRecord(CheckDomainsData);
                }
                System.out.println(Record);
                List<String> ViaList =new ArrayList<>();
                //Error
                ViaList.add(OneSearch(Record));
                System.out.println("count:"+count+ViaList);
                AnsToList.add(ViaList.get(0));
                //値を取り出す処理は完了 --今後はそれのリスト化などを行う。
            }
            ReturnList = ListCreate(AnsToList);
            //ループ処理が終了した場合に
            return ReturnList;
        }
        return ReturnList;

    }

    // CNAMEレコードを取得する際に自動的に行う処理。 --単独も複数も可。
    public CheckerRecords GetCnameRecord(String DomainData) {
        // CNAMEレコードを取得する
        System.out.println("CNAMEレコード取得-------");
        try {
            Lookup lookup = new Lookup(DomainData, Type.CNAME);
            lookup.run();
            //バグ なぜかAレコード判定が発生する
            // CNAMEレコードが見つかった場合 
            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                Record[] records = lookup.getAnswers();
                for (Record record : records) {
                    CNAMERecord CNAME = (CNAMERecord) record;
                    String ToString = CNAME.getTarget().toString();
                    return new CheckerRecords(ToString, "CNAME", DomainData);
                }
            } else {
                // Aレコードではないかの検知を行う。
                return GetARecord(DomainData);
            }
        } catch (TextParseException e) {
            // 処理に不具合が発生した。
            e.printStackTrace();
            System.out.println("正しいドメインが入力されていません");
            return null;
        }
        return null;

    }

    // Aレコードを取得する処理 --単独も複数も可。
    public CheckerRecords GetARecord(String DomainData) {
        try {
            Lookup lookup = new Lookup(DomainData, Type.A);
            lookup.run();
            // Aレコードを取得する
            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                Record[] records = lookup.getAnswers();
                for (Record record : records) {
                    ARecord A = (ARecord) record;
                    String ToString = A.getName().toString();
                    return new CheckerRecords(ToString, "A", DomainData);
                }
            }else{
                //その他の形式は自動的にはじく
                return new CheckerRecords(null, "null", DomainData);
            }
        } catch (TextParseException e) {
            // 不具合発生時に送るやつ
            e.printStackTrace();
            System.out.println("正しいドメインが入力されていません");
            System.exit(0);
            return null;
        }
        return null;
    }

    // 検知機能本体-単独用
    public String OneSearch(CheckerRecords CheckDatas) {
        // 単独用の検知処理
        String RetrunValue = null;

        try {
            // JSONから検知用データの取得
            InputStream IS = Core.class.getClassLoader().getResourceAsStream("checkfile/DifineCheck.json");
            String JsonString = new String(IS.readAllBytes(), StandardCharsets.UTF_8);
            // JSONのデータを取得した。
            JSONArray CheckList = new JSONArray(JsonString);

            // 検知処理に移行
            // 要素数を取得して検知する
            int CheckCount = CheckList.length();
            //System.out.println(CheckList);
            //System.out.println(CheckCount);
            // JSONにあるすべてのサービスとの整合をチェックする。
            for (int i = 0; i < CheckCount; i++) {
                // 繰り返しでパターンを取得する。
                JSONObject CheckService = CheckList.getJSONObject(i);
                // 必要なデータを取得する
                String ServiceName = CheckService.getString("Name");
                // StatusCodeでの検知が可能かどうか
                Boolean SerivcePattern = CheckService.getBoolean("PassStatus");
                // Check処理に受け渡す専用のCheck用データ
                JSONArray CheckValues = null;
                //System.out.println(CheckDatas.Record());
                //System.out.println(CheckDatas.RecordType());
                //InvaildData が見つかった場合に処理をパスするやつ
                //nullあるデータがあったら殺す
                if(CheckDatas == null){
                    continue;
                }
                //レコードのタイプを図る -何も取得できなかった場合にパスするようにする
                if (CheckDatas.RecordType() == "CNAME") {
                    CheckValues = CheckService.getJSONArray("CNAMEIdentifier");
                    //System.out.println(CheckValues);
                } else if (CheckDatas.RecordType() == "A") {
                    CheckValues = CheckService.getJSONArray("ARecordIdentifier");
                } else {
                    //不明なケース
                }
                ///
                /// そもそもpattern判定からはじくケースを作成する -->多分ここにねじ込んだほうが早い;
                ///
                JSONArray CheckPatterns = CheckValues;
                //System.out.println(CheckPatterns);
                // サービスのそれぞれの奴と合致するかどうかを確かめる。
                //CheckValuesがnullの場合にはじく
                if(CheckValues == null){
                    continue;
                }
                for (int j = 0; j < CheckValues.length(); j++) {
                    // パターンが合致するかどうかを確かめる。
                    Pattern CHECKPATTERN = Pattern.compile(CheckPatterns.getString(j));
                    //
                    
                    if (CheckDatas.Record() != null) {
                        if (CHECKPATTERN.matcher(CheckDatas.Record()).matches()) {
                            // パターンがマッチした場合に次の処理に移る。
                            //System.out.println("--------パターンマッチ完了------");
                            String CheckStr = CheckPatterns.getString(j);
                            //System.out.println(CheckStr);
                            String Value = SearchAns3(CheckDatas);
                            // デバッグ出力
                            //System.out.println(Value);
                            RetrunValue = Value;
                        } else {
                            // Debug
                            System.out.println("定義ファイルpass");
                            continue;
                        }
                    } else {
                        System.out.println("レコードがないよ!");
                    }
                }

                // もしも、ReturnValueが存在する場合に返り血として出す。
                if (RetrunValue != null) {
                    //System.out.println("返す値が存在している状態");
                    return RetrunValue;
                }
            }
            // チェックし終わった場合に、結果を作成する
            // RetrunValue = CheckDatas.DomainData()+":OK";

            // 結果が存在する場合に
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("正しく処理ができていない場合");

        // すべてが終了した際に、引数をぶち返すやつ。
        return null;
    }

    // 新しい検索処理 javanetを活用したステータス取得
    // 特にホスト名を設定した検知方法

    public String SearchAns1(CheckerRecords CheckData) {
        System.out.println("サーチ処理に移行----------------------");
        //System.out.println(CheckData);
        String CheckDomainRecord = CheckData.Record().replaceAll("\\.$", "");
        // url作成
        String url = "https://" + CheckDomainRecord;
        String RetrunValue;
        //System.out.println(url);
        try {
            //System.out.println("-----------ネットワーク部----------");
            HttpClient client = HttpClient.newHttpClient();
            URI uri = new URI(url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("User-Agent", "Java HttpClient")
                    .header("host", CheckData.DomainData())
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // ステータスコードを取る
            int responseCode = response.statusCode();
            //System.out.println(responseCode);

            if (responseCode >= 200 && responseCode < 400) {
                RetrunValue = CheckData.DomainData() + ":OK";
            } else {
                RetrunValue = CheckData.DomainData() + ":NG";
            }
            return RetrunValue;

        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
            System.out.println("ネットワーク部の処理にエラー発生");
            return null;
        }

    }

    // 予備で作成--
    public String SerachAns2(CheckerRecords CheckData) {
        System.out.println("サーチ処理実行----------------------");
        String CheckDomainRecord = CheckData.Record().replaceAll("\\.$", "");
        String ReturnValue;
        String url = "https://" + CheckDomainRecord;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(true);

            // アクセス処理
            connection.connect();
            int responseCode = connection.getResponseCode();
            System.out.println("取得できたステータスコード:" + responseCode);
            // レスポンスコードによる判定
            if (responseCode >= 200 && responseCode < 400) {
                ReturnValue = CheckData.DomainData() + ":OK";
            } else {
                ReturnValue = CheckData.DomainData() + ":NG";
            }
            return ReturnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //apacheVer
    public String SearchAns3(CheckerRecords CheckData){
        System.out.println("SH3--------");
        //Java21はこれを使わないと×
        String CheckDomainRecord = CheckData.Record().replaceAll("\\.$", "");
        String ReturnValue = null;
        String url = "https://" + CheckDomainRecord;
        System.out.println("リクエスト先url:" + url);
        try(CloseableHttpClient client = HttpClients.createDefault()){
            //ホスト名の指定
            HttpGet request = new HttpGet(url);
            request.setHeader("Host",CheckData.Record());
            System.out.println("リクエスト内容");
            System.out.println(request);
            try(CloseableHttpResponse response = client.execute(request)){
                int responseCode = response.getCode();
                System.out.println("リクエスト結果:" + responseCode);
                //判定
                if (responseCode >= 200 && responseCode < 400){
                    ReturnValue = CheckData.DomainData() + ":OK";
                }else{
                    ReturnValue = CheckData.DomainData() + ":NG";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ReturnValue;
    }
    // 特殊条件のケースでの検知処理

    // 出力結果を再解析し、形式を変更する。
    // リストに内包したHashMap型に変更する。
    public List<HashMap<String, Boolean>> ListCreate(List<String> Result) {
        // 考える箇所・結局データ構造どうすんねん
        List<HashMap<String, Boolean>> DataList = new ArrayList<HashMap<String, Boolean>>();
        System.out.println("作成データの変換--------------------------");
        // 形式を再解釈する。
        for (int i = 0; i < Result.size(); i++) {
            // 二回ぐらい使うかもしれんからとっ得
            String Value = Result.get(i);
            // とるやつ
            String FirstValue = null;
            boolean SecondValue;
            HashMap<String, Boolean> ValueRecord = new HashMap<String, Boolean>();
            // Okの箇所とそれ以外を切り離す。
            if(Value == null){
                //空のデータを出された場合にはじく処理
                continue;
            }
            if (Value.contains(":OK")) {
                //System.out.println(Value);
                //バグ--切り取りがうまくいっていない
                FirstValue = Value.substring(0, Value.indexOf(":OK"));
                SecondValue = true;
                // Hashmapで FirstValueをkeyに,結果をvalueとして保存する。
                ValueRecord.put(FirstValue, SecondValue);
            } else if (Value.contains(":NG")) {
                FirstValue = Value.substring(0, Value.indexOf(":NG"));
                SecondValue = false;
                // Hashmapで FirstValueをkeyに,結果をvalueとして保存する。
                ValueRecord.put(FirstValue, SecondValue);
            } else {
                // 例外発生
                System.out.println("適切なデータ形式ではありません");
            }
            //作成した ValueRecordをListにぶち込む
            DataList.add(ValueRecord);
        }
        System.out.println(DataList);
        return DataList;
    }
}
