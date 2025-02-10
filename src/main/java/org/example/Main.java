package org.example;

import org.Packed_Net.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;




public class Main {
    public static void main(String[] args) {
        //作成
        Network get_Connection = new Network();
        Scanner scanner = new Scanner(System.in);
        System.out.println("確認を行うドメインを入力してください");

        String CheckDomain = scanner.nextLine();
        System.out.println(CheckDomain + "の設定を検索します。");
        scanner.close();

        //cnameを取得する
       get_Connection.Get_CNAME(CheckDomain);
       //デバッグ用の文字列
       /*
       List<String>list_Domain = new ArrayList<>();
       list_Domain.add("google.com");
       list_Domain.add("yahoo.com");
       list_Domain.add("me-ycpi-cf-www.g06.yahoodns.net");
       list_Domain.add("choice.ameba.jp/matching-app");
       list_Domain.add("code-raisan.github.io/page/");
       get_Connection.Search_prot(list_Domain);
       */
    }

}
