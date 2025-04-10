package io.github.harutkue.checker.core.checker;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

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
            String Record;
            Record = GetCnameRecord(DomainData.toString());
            //
            System.out.println(Record);
        }else if (DomainData instanceof List<?>){
            //複数のドメインを同時で調べる場合 - eTLD対応
        }
    }
    //CNAMEレコードを取得する際に自動的に行う処理。 --単独も複数も可。
    public String GetCnameRecord(String DomainData){
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
                    return ToString ;
                }
            }else{
                //Aレコードではないかの検知を行う。
                return GetARecord(DomainData);
            }
        }catch(TextParseException e){
            //処理に不具合が発生した。
            e.printStackTrace();
            return "-Error";
        }
        return "--Error";
        
    }
    //Aレコードを取得する処理 --単独も複数も可。
    public String GetARecord(String DomainData){
        try{
            Lookup lookup = new Lookup(DomainData, Type.A);
            lookup.run();
            //Aレコードを取得する
            if(lookup.getResult() == Lookup.SUCCESSFUL){
                Record[] records = lookup.getAnswers();
                for (Record record : records){
                    ARecord A = (ARecord) record;
                    String ToString = A.getName().toString();

                    return "";
                }
            }
        }catch(TextParseException e){
            //不具合発生時に送るやつ
            e.printStackTrace();
            return "-Error";
        }
        return"--Error";
    }
    //検知機能本体-単独用
    public String OneSearch(String Record,String DomainData){
        //単独用の検知処理
        return"";
    }
}
