package io.github.harutkue.checksdhj.app;

import java.util.Scanner;

import io.github.harutkue.checksdhj.networks.network;




public class Main {
    public static void main(String[] args) {
        //作成
        network get_Connection = new network();
        Scanner scanner = new Scanner(System.in);
        System.out.println("確認を行うドメインを入力してください");

        String CheckDomain = scanner.nextLine();
        System.out.println(CheckDomain + "の設定を検索します。");
        scanner.close();

        //cnameを取得する
       get_Connection.Get_CNAME(CheckDomain);
    }

}
