package io.github.harutkue.checker.core.app;

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


        
        //デバッグ用
        get_Connection.Get_CNAME(CheckDomain);
        System.out.println("------------------------------------");
        newGetConnection.GetRequestValue(CheckDomain);
        
    }

}
