package io.github.harutkue.checksdhj.interfaces;

import java.io.IOException;

import java.util.ArrayList;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.List;
import java.util.Map;


//Aレコードを取得するプログラム(CheckMethodsで用いる)
public class getdns {
    public static List<String> getArecords(String Domain){
        List<String> return_datas = new ArrayList<>();
        try{
            Lookup lookup = new Lookup(Domain,Type.A);
            lookup.setResolver(new SimpleResolver("8.8.8.8"));
            Record[] records = lookup.run();
            if(records != null && records.length > 0){
                for(Record Arecord: records){
                    String tostr = Arecord.rdataToString();
                    return_datas.add(tostr);
                }
                //Aレコードの返却。
                return return_datas;
            }else{
                return_datas.add("Nothing");
                return return_datas;
            }
            
        }catch(IOException e){
            e.printStackTrace();
            return_datas.add("Error");
        }
        return return_datas;
    }
    public static List<String> getService(List<String> check_record){
        List<String> return_datas = new ArrayList<>();
        System.out.println(check_record);
        for(String record: check_record){
            String provider = getServiceProvider(record);
            return_datas.add(provider);
        }
        
        //返り血
        return return_datas;
    }
    public static String getServiceProvider(String record){
        //プロバイダのマップ --キーと対応した値を返す。
        Map<String,String> PROVIDERLIST = providerlist.retProvider();
        for(Map.Entry<String,String> entry: PROVIDERLIST.entrySet()){
            //もし、取得したIPアドレスがの先頭部がプロバイダリストと一致するか
            if (record.startsWith(entry.getKey())){
                return entry.getValue();
            }
        }
        return "Nothing";
    }
    
}
