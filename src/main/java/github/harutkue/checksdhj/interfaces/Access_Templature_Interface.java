package github.harutkue.checksdhj.interfaces;

import java.util.Map;

import github.harutkue.checksdhj.interfaces.getnet.getdns;
import java.util.HashMap;
import java.util.List;
//Interfaceのみのやつ。
//
//javac Access_Templature_Interface.java Redirect_Githubpages.java....
//
public interface Access_Templature_Interface{
    //methodの宣言
    //受け取ったものをぶち込む
    //ここに定数および、処理を規定して、判断を行う
    //
    public boolean CheckMethods(String Domain);
    public String Main_Access(String Access_Domain);
    public List<String> getProviderService();
    //Aレコードの取得
    //public String Check_Arecords(String Domain){}
}

//様々なクラス処理の実行
