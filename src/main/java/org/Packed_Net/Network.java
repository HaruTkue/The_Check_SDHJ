package org.Packed_Net;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

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

    //ファイル内のリストを作成する 読み取りまで
    public static List<Class<?>> ListInClass() throws IOException,ClassNotFoundException{
        //IOException-をthrowすることで例外規定の動作。
        List<Class<?>> DoClassList = new ArrayList<Class<?>>();
        File DirectPath = new File("src/main/java/org/Interface/Interfaces");
        //存在しない場合の対策
        if (!DirectPath.exists() || !DirectPath.isDirectory()){
            throw new IllegalArgumentException("Invalid");
        }
        //Urlを取得する
        URL url = DirectPath.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url});

        for (File file: DirectPath.listFiles()){
            if(file.isFile() && file.getName().endsWith(".class")){
                String className = file.getName();
                Class<?> append_Class = classLoader.loadClass(className);
                DoClassList.add(append_Class);
            }
        }

        //クラスローダーを閉じる
        classLoader.close();
        return DoClassList;
    }

    public void Search_prot(List<String> CNAME_list){
        //Interfacesを使用した Classを取得する
        List<Class<?>> DoClassList = new ArrayList<>();
        try{
            List<Class<?>> ClassList = ListInClass();
            System.out.println(ClassList);
            DoClassList = ClassList;
        }catch(Exception e){
            //適切に処理できなかった場合
            e.printStackTrace();
            System.out.println("Interface内のクラスを読み取ることができませんでした。");
        }
        

        //二重for文で適切なコードを検知する。 -CNAME上
        for(String CNAME: CNAME_list){
            //それぞれとの適合を確認するためのやつ
            for(Class<?> Play_Class: DoClassList){
                //ここで整合性を図る。
            }
        }
        
        System.out.println(CNAME_list);
        System.out.println("サーチ処理に移ります。");
    }
    
}

