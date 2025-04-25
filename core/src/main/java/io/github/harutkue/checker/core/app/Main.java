package io.github.harutkue.checker.core.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import io.github.harutkue.checker.core.checker.Core;
//import io.github.harutkue.checker.core;
import io.github.harutkue.checker.core.network.Network;




public class Main {
    public static void main(String[] args) {
        //DNSjavaが適切に動作しないケースがあるため。
        System.setProperty("jdk.net.hostresolver", "org.xbill.DNS.spi.DnsjavaInetAddressResolverProvider");
        //作成
        /*
        /* 
        if(args.length ==0){
            System.out.println("適切な引数を指定できていません。");
            return;
        }*/
        Network get_Connection = new Network();
        Core newGetConnection =new Core();
        //スキャナ類の処理はいったん使用しない。
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("確認を行うドメインを入力してください");

        String CheckDomain = scanner.nextLine();
        System.out.println(CheckDomain + "の設定を検索します。");
        scanner.close();


        
        //単独のテストケース
        System.out.println("単体データ------------------------------------");
        //リストを作成
        List<HashMap<String,Boolean>> AnswerList = new ArrayList<>();
        AnswerList = newGetConnection.GetRequestValue(CheckDomain);
        System.out.println(AnswerList);
        //出力用のクラス





        //複数データのテスト
        
        System.out.println("複数データ--------------------------");
        List<String> TestData = new ArrayList<>();
        TestData.add("harugpg.shinchokudonan.com");
        TestData.add("harugpg.shinchokudonan.com");
        TestData.add("harugpg.shinchokudonan.com");
        TestData.add("");
        TestData.add("harugpg.shinchokudonan.com");
        AnswerList = newGetConnection.GetRequestValue(TestData);
        System.out.println(AnswerList);
    }

}
