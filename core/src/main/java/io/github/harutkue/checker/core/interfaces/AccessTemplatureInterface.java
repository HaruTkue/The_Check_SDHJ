package io.github.harutkue.checker.core.interfaces;


import java.util.List;
//Interfaceのみのやつ。
//
//javac Access_Templature_Interface.java Redirect_Githubpages.java....
//
public interface AccessTemplatureInterface{
    //methodの宣言
    //受け取ったものをぶち込む
    //ここに定数および、処理を規定して、判断を行う
    //
    public boolean CheckMethods(String Domain);
    public String MainAccess(String AccessDomain,String Domain);
    public List<String> getProviderService();
    //Aレコードの取得
    //public String Check_Arecords(String Domain){}
}

//様々なクラス処理の実行
