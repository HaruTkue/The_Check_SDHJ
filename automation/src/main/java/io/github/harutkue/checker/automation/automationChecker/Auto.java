package io.github.harutkue.checker.automation.automationChecker;

import org.xbill.DNS.*;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Record;
public class Auto {
    public void Request(String RequestDomain){
        //処理の流れ
        //入力内容の検証を実施する----

        //CNAMEレコードを取得する(Module別なのでCoreから読み出すものではないのでは?)
        String Record = GetRequestCNAME(RequestDomain);
        //まず入力内容の検証を行う。そもそもCNAMEレコードが取得できない時点で処理から外す。
        //既存のjsonと比較してた入っていないか確かめる
        //できれば正しいクラウドサービスのリクエストかどうかを確かめたい

        //この地点で不正なデータはなるべくはじく。また、より細かくドメインを確認する仕組みが必要かな

        //その後通常のケースとそうでないケースをCurlの要領で確認
        //ダメだった場合には弾く。

        //もしも正しかった場合には、GithubActionsに処理をぶん投げる。
        //できれば、Aレコードまで取得したい
        //ダメだった場合にはあれです。
        
    }
    //CNAMEレコードを取得する
    public String GetRequestCNAME(String RequestDomain){
        try{
            Lookup lookup = new Lookup(RequestDomain,Type.CNAME);
            lookup.run();
            if(lookup.getResult() == Lookup.SUCCESSFUL){
                Record[] records = lookup.getAnswers();
                for(Record record:records){
                    CNAMERecord cname  = (CNAMERecord) record;
                    String ToString = cname.getTarget().toString();
                    return ToString;
                }
            }else{
                System.out.println("適切な形式で入力してください。");
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
