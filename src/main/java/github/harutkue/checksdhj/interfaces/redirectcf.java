package github.harutkue.checksdhj.interfaces;

import java.io.IOException;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import github.harutkue.checksdhj.interfaces.getnet.getdns;

import java.util.ArrayList;
import java.util.List;

public class redirectcf implements Access_Templature_Interface{
    public boolean CheckMethods(String Domain){
        //CloudFlareかどうかを確認するやつ。
        //Aレコードを取得する。
        getdns get_records = new getdns();
        List<String> Check_Records = get_records.getArecords(Domain);
        List<String> serverData = get_records.getService(Check_Records);
        return true;

    }
    public String Main_Access(String Access_Domain){
        return "A";
    }
}
