package github.harutkue.checksdhj.checksdhj;

import java.util.Scanner;

import github.harutkue.checksdhj.networks.network;




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
