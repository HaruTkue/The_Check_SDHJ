package org.example;

import org.Packed_Net.Network;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;




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
    }

}
