package org.Packed_Net;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Network{
    public void Get_CNAME(String CheckDomain){
        List<String> CNAME_list = new ArrayList<String>();
        try {
            // lookupの指定
            System.out.println("処理を開始します。");
            Lookup lookup = new Lookup(CheckDomain,Type.CNAME);
            lookup.run();

            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                Record[] records = lookup.getAnswers();
                System.out.println("適切に読むことができました。");
                for (Record record:records){
                    CNAMERecord cname = (CNAMERecord) record;
                    System.out.println(CheckDomain + cname.getTarget());
                    String ToString = cname.getTarget().toString();

                    CNAME_list.add(ToString);
                }

                Search search_class = new Search();
                search_class.Search_prot(CNAME_list);
            }else{
                System.out.println("CNAMEが存在しませんでした:"+ CheckDomain);
            }
        } catch (TextParseException e) {
            System.out.println("処理に不具合が発生しました。");
            e.printStackTrace();
        }
        System.out.println("処理を終了します。");
    }

    //ファイル内のリストを作成する
    public List<Class<?>> ListInClass(){
        List<Class<?>> DoClassList = new ArrayList<Class<?>>();
        DoClassList
        return DoClassList;
    }
    public void Search_prot(List<String> CNAME_list){
        
        System.out.println(CNAME_list);
        System.out.println("サーチ処理に移ります。");
    }
    
}

