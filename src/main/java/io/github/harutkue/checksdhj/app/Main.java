package io.github.harutkue.checksdhj.app;

import java.util.Scanner;

import io.github.harutkue.checksdhj.networks.network;




public class Main {
    public static void main(String[] args) {
        //作成
        if(args.length ==0){
            System.out.println("適切な引数を指定できていません。");
        }
        network get_Connection = new network();
        //スキャナ類の処理はいったん使用しない。
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("確認を行うドメインを入力してください");

        String CheckDomain = scanner.nextLine();
        System.out.println(CheckDomain + "の設定を検索します。");
        scanner.close();
        */
        for(String target:args){
            System.out.println("指定されたサブドメイン:" + target);
            //cnameを取得する
            String CheckDomain = target;
            get_Connection.Get_CNAME(CheckDomain);
        }
    }

}
