package org.Packed_Net;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Network{
    public void Get_CNAME(String CheckDomain){
        List<Name> CNAME_list = new ArrayList<Name>();
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
                    CNAME_list.add(cname.getTarget());
                }
                Search_prot(CNAME_list);
            }else{
                System.out.println("CNAMEが存在しませんでした:"+ CheckDomain);
            }
        } catch (TextParseException e) {
            System.out.println("処理に不具合が発生しました。");
            e.printStackTrace();
        }
        System.out.println("処理を終了します。");
    }
    public void Search_prot(List<Name> CNAME_list){
        System.out.println("サーチ処理に移ります。");
        
    }
    
}

