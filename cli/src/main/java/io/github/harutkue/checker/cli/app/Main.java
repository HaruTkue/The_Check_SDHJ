package io.github.harutkue.checker.cli.app;

import io.github.harutkue.checker.core.checker.Core;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    // color定義
    public static final String RESETCOLOR = "\u001B[0m";
    public static final String REDCOLOR = "\u001B[31m";
    public static final String GREENCOLOR = "\u001B[32m";

    private static final Pattern SubDomainPattern = Pattern.compile("^(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+(?:[a-zA-Z]{2,}|co\\.jp|or\\.jp|ne\\.jp|ac\\.jp|go\\.jp)$");
    private static final Pattern DomainPattern = Pattern.compile("^[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]+$");

    // Main
    public static void main(String[] args) {
        if (args.length == 1) {
            System.out.println("検知処理を実行します。");
            String CheckDomain = args[0];
            // 検知処理
            // 不正なデータの場合は終了する
            SingleCLISearch(CheckDomain);
        } else {
            System.out.println("SDHJChecker CLIです。");
            System.out.println("単独検知:1 複数検知:2 終了:その他のkey");

            // CLI開発
            Scanner scan = new Scanner(System.in);
            String ActInput = scan.nextLine();

            System.out.println(ActInput);
            // 処理の分岐
            if (ActInput.equals("1")) {

                System.out.println("単独検知処理に移ります");
                OneSearch();
            } else if (ActInput.equals("2")) {

                System.out.println("複数検知処理に移ります");
                MultiSearch();
            } else {
                System.out.println("プログラムを終了します");
            }

            // インスタンス作成 --今後新しい処理を作成した際に変更する可能性あり。

            // 実際の入出力の際に使用する --デバッグのために使用しない。
            // 引数が存在しない場合に処理を終了する。

            /*
             * if(args.length ==0){
             * System.out.println("適切な引数を指定できていません。");
             * return;
             * }
             */
            // 実際の入出力の受け取りは args --デバッグ用としてScannerを使用。
            // デバッグ用入力受け取り
            /*
             * Scanner scanner = new Scanner(System.in);
             * String CheckDomain = scanner.nextLine();
             * 
             * System.out.println("--------------------------------");
             * //この箇所にeTLD取得を記述していく。また、その結果をリストに保存して受け渡す。
             * //メソッドの実行 --既存のプログラムでは 第一引数のみを指定する。
             * List<HashMap<String,Boolean>> AnswerList = new ArrayList<>();
             * 
             * System.out.println(AnswerList);
             */}
    }

    public static void OneSearch() {
        // 単独での検知処理
        System.out.println("検知したいサブドメインを入力してください");
        Scanner scanner = new Scanner(System.in);
        String CheckDomain;
        
        do {
            // 処理の形式が正式であるかを確認する。
            CheckDomain = scanner.nextLine();
            if (true) {
                // 適合している
                
                System.out.println("正しく入力されました。");
                break;
            }

        } while (true);
        // 検知処理
        scanner.close();
        Core newGetConnection = new Core();
        List<HashMap<String, Boolean>> AnswerList = new ArrayList<>();
        AnswerList = newGetConnection.GetRequestValue(CheckDomain);
        // 取得処理
        for (HashMap<String, Boolean> Data : AnswerList) {
            Map.Entry<String, Boolean> EntryValue = Data.entrySet().iterator().next();
            String AnswerUri = EntryValue.getKey();
            Boolean Ans = EntryValue.getValue();
            // if文で出力を変える
            if (Ans) {
                if (AnswerUri.contains(":Nothing")) {
                    System.out.println(AnswerUri.substring(0, AnswerUri.indexOf(":Nothing")) + "は削除されたか存在していません。");
                    continue;
                }
                System.out.println(GREENCOLOR + AnswerUri + "は正常に設定されています" + RESETCOLOR);
            } else {
                System.out.println(REDCOLOR + AnswerUri + "は正常に設定されていません!" + RESETCOLOR);
            }

        }
    }
    // 誤入力をはじく専用のやつ

    public static void MultiSearch() {
        // 複数での検知処理
        System.out.println("検知を行いたいサブドメインを入力してください");
        System.out.println("入力を終了したい場合には0を入力してください");
        Scanner scanner = new Scanner(System.in);

        List<String> CheckDomains = new ArrayList<>();
        Core newGetConnection = new Core();
        List<HashMap<String, Boolean>> AnswerList = new ArrayList<>();
        String CheckDomain;

        // 0が入力されるまで繰り返す。
        do {
            // 処理の形式が正式であるかを確認する。
            CheckDomain = scanner.nextLine();
            // おなやみ:現状の入力状況を出力しようかな
            if ((SubDomainPattern.matcher(CheckDomain)).matches() || (DomainPattern.matcher(CheckDomain)).matches()) {
                // 適合している
                CheckDomains.add(CheckDomain);
                System.out.println("正しく入力されました。");
            } else if (CheckDomain.equals("0")) {
                // 終了
                break;
            } else {
                // 不適合
                System.out.println("適切な形式で入力してください");
            }

        } while (true);
        // 結果を取得する
        AnswerList = newGetConnection.GetRequestValue(CheckDomains);
        scanner.close();

        // 結果出力
        System.out.println("------------結果-----------");
        for (HashMap<String, Boolean> Data : AnswerList) {
            Map.Entry<String, Boolean> EntryValue = Data.entrySet().iterator().next();
            String AnswerUri = EntryValue.getKey();
            Boolean Ans = EntryValue.getValue();
            // if文で出力を変える
            if (Ans) {
                if (AnswerUri.contains(":Nothing")) {
                    System.out.println(AnswerUri.substring(0, AnswerUri.indexOf(":Nothing")) + "は削除されたか存在していません。");
                    continue;
                }
                System.out.println(GREENCOLOR + AnswerUri + "は正常に設定されています" + RESETCOLOR);
            } else {
                System.out.println(REDCOLOR + AnswerUri + "は正常に設定されていません!" + RESETCOLOR);
            }

        }

    }

    public static void SingleCLISearch(String CheckDomain) {
        // CLI上で直予備の時
        if ((SubDomainPattern.matcher(CheckDomain)).matches() || (DomainPattern.matcher(CheckDomain)).matches()) {
            Core newGetConnection = new Core();
            List<HashMap<String, Boolean>> AnswerList = new ArrayList<>();
            AnswerList = newGetConnection.GetRequestValue(CheckDomain);
            // 取得処理
            for (HashMap<String, Boolean> Data : AnswerList) {
                Map.Entry<String, Boolean> EntryValue = Data.entrySet().iterator().next();
                String AnswerUri = EntryValue.getKey();
                Boolean Ans = EntryValue.getValue();
                // if文で出力を変える
                if (Ans) {

                    if (AnswerUri.contains(":Nothing")) {
                        System.out.println(AnswerUri.substring(0, AnswerUri.indexOf(":Nothing")) + "は削除されたか存在していません。");
                        continue;
                    }
                    System.out.println(GREENCOLOR + AnswerUri + "は正常に設定されています" + RESETCOLOR);
                } else {
                    System.out.println(REDCOLOR + AnswerUri + "は正常に設定されていません!" + RESETCOLOR);
                }
            }
        } else {
            System.out.println("正しいサブドメインが指定されていません");
        }

    }
}
