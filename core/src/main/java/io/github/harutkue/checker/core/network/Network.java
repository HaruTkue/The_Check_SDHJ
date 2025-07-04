package io.github.harutkue.checker.core.network;

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


import io.github.harutkue.checker.core.interfaces.RedirectGithubpages;
import io.github.harutkue.checker.core.interfaces.RedirectRender;
import io.github.harutkue.checker.core.interfaces.RedirectVercel;
import io.github.harutkue.checker.core.interfaces.RedirectWordpress;

public class Network {
    public void Get_CNAME(String CheckDomain) {
        List<String> CNAME_list = new ArrayList<String>();
        try {
            // lookupの指定
            System.out.println("処理を開始します。");
            Lookup lookup = new Lookup(CheckDomain, Type.CNAME);
            lookup.run();

            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                // CNAMEレコードが発見されたときs
                Record[] records = lookup.getAnswers();
                for (Record record : records) {
                    CNAMERecord cname = (CNAMERecord) record;
                    String ToString = cname.getTarget().toString();

                    Search_prot(ToString,CheckDomain);
                }
            } else {
                // 存在していないことと恐らく脆弱性がなさそうであると報告する。
                System.out.println("CNAMEが存在しませんでした:" + CheckDomain);
            }
        } catch (TextParseException e) {
            System.out.println("処理に不具合が発生しました。");
            e.printStackTrace();
        }
    }

    // ファイル内のリストを作成する 読み取りまで
    public static List<Class<?>> ListInClass() throws IOException, ClassNotFoundException {
        // IOException-をthrowすることで例外規定の動作。
        List<Class<?>> DoClassList = new ArrayList<Class<?>>();
        File DirectPath = new File("src/main/java/github/harutkue/checksdhj/interfaces");
        // 存在しない場合の対策
        if (!DirectPath.exists() || !DirectPath.isDirectory()) {
            System.out.println("クラスが見つかりませんでした。");
            throw new IllegalArgumentException("Invalid");
        }
        // Urlを取得する
        URL url = DirectPath.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[] { url });
        for (File file : DirectPath.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = file.getName().replace(".class", "");
                // org.Interface.から変更有
                // github.harutkue.checksdhj.interfaces.
                Class<?> append_Class = classLoader.loadClass("github.harutkue.checksdhj.interfaces." + className);
                DoClassList.add(append_Class);
            }
        }

        // クラスローダーを閉じる
        classLoader.close();
        return DoClassList;
    }

    public static List<Class<?>> Listic() {
        List<Class<?>> class_list = new ArrayList<>();

        //class_list.add(redirectcf.class);

        //class_list.add(Redirect_Http.class);

        class_list.add(RedirectGithubpages.class);
        class_list.add(RedirectWordpress.class);
        class_list.add(RedirectRender.class);
        class_list.add(RedirectVercel.class);

        return class_list;
    }
    //
    /*
    public void Search_prot(List<String> CNAME_list) {
        // Interfacesを使用した Classを取得する
        List<Class<?>> DoClassList = new ArrayList<>();
        try {
            List<Class<?>> ClassList = Listic();
            DoClassList = ClassList;
        } catch (Exception e) {
            // 適切に処理できなかった場合
            e.printStackTrace();
            System.out.println("Interface内のクラスを読み取ることができませんでした。");
        }
        // 事前に処理結果を保存するリストを作成する。
        List<String> Access_Result = new ArrayList<>();
        // 二重for文で適切なコードを検知する。 -CNAME上
        for (String CNAME : CNAME_list) {
            System.out.println("発見されたCNAME:" + CNAME);
            // それぞれとの適合を確認するためのやつ
            for (Class<?> Check_Class : DoClassList) {
                try {
                    // ここで整合性を確認する -インスタンスを作成

                    Object PlayInstance = Check_Class.getDeclaredConstructor().newInstance();
                    // インスタンス上でMethodsを実行

                    // methodを取得する。
                    Method Check_Method = Check_Class.getMethod("CheckMethods", String.class);
                    boolean Check_Result = (boolean) Check_Method.invoke(PlayInstance, CNAME);
                    // 結果を取得する。
                    if (Check_Result) {
                        Method Check_Access_Method = Check_Class.getMethod("MainAccess", String.class,String.class);
                        // 実行結果を文字列にする
                        String Append_Access_Result = (String) Check_Access_Method.invoke(PlayInstance, CNAME,null);
                        // リストにぶち込む。
                        Access_Result.add(Append_Access_Result);
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
        //ここに判別用の処理を足す。
        printFunction(Access_Result);
        
        

    }*/
    public void Search_prot(String CNAME,String Domain) {
        // Interfacesを使用した Classを取得する
        List<Class<?>> DoClassList = new ArrayList<>();
        try {
            List<Class<?>> ClassList = Listic();
            DoClassList = ClassList;
        } catch (Exception e) {
            // 適切に処理できなかった場合
            e.printStackTrace();
            System.out.println("Interface内のクラスを読み取ることができませんでした。");
        }
        // 事前に処理結果を保存するリストを作成する。
        List<String> Access_Result = new ArrayList<>();
        // 二重for文で適切なコードを検知する。 -CNAME上
        System.out.println("発見されたCNAME:" + CNAME);
            // それぞれとの適合を確認するためのやつ
            for (Class<?> Check_Class : DoClassList) {
                try {
                    // ここで整合性を確認する -インスタンスを作成

                    Object PlayInstance = Check_Class.getDeclaredConstructor().newInstance();
                    // インスタンス上でMethodsを実行

                    // methodを取得する。
                    Method Check_Method = Check_Class.getMethod("CheckMethods", String.class);
                    boolean Check_Result = (boolean) Check_Method.invoke(PlayInstance, CNAME);
                    // 結果を取得する。
                    if (Check_Result) {
                        Method Check_Access_Method = Check_Class.getMethod("MainAccess", String.class,String.class);

                        // 実行結果を文字列にする
                        //ここでエラーが発生している。 しかもなぜか動かない。　そして、DNSJava不使用!
                        String Append_Access_Result = (String) Check_Access_Method.invoke(PlayInstance, CNAME,Domain);
                        // リストにぶち込む。
                        Access_Result.add(Append_Access_Result);
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("適切な動きをしていない場合");
                    e.printStackTrace();
                }

            }
        //ここに判別用の処理を足す。
        printFunction(Access_Result);
        
        

    }

    public void printFunction(List<String> Access_Result){
        //処理を変更
        Pattern okcheck = Pattern.compile(".*:OK.*");
        Pattern ngcheck = Pattern.compile(".*:NG.*");
        if (Access_Result.isEmpty()) {
            System.out.println("対応していないサービスです。");
        }else{
            for(String Result:Access_Result){
                System.out.println(Result);
                //疎通確認が正しくできたか、できてないか
                if(Result.contains(":OK")){
                    System.out.println("OKだった場合の処理");
                    String url_link = Result.substring(0,Result.indexOf(":OK"));
                    String print_text = url_link + "は適切に設定されています。";
                    System.out.println(print_text);
                }else if(Result.contains(":NG")){
                    System.out.println("NGだった場合の処理");
                    String url_link = Result.substring(0,Result.indexOf(":NG"));
                    String print_text = url_link +"は適切に設定されていません!";
                    System.out.println(print_text);
                }else{
                    System.out.println("その他のエラーです");
                }
            }
        }
    }

}
