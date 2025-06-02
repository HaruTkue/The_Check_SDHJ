package io.github.harutkue.checker.cli.app;
import io.github.harutkue.checker.core.checker.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
        public static void main(String[] args){
            System.out.println("SDHJChecker CLIです。");
            System.out.println("単独検知:1 複数検知:2 終了:その他のkey");
            
            //CLI開発
            Scanner scan = new Scanner(System.in);
            String ActInput = scan.nextLine();
            //処理の分岐
            if(ActInput == "1"){
                scan.close();
                System.out.println("単独検知処理に移ります");
                OneSearch();
            }else if(ActInput == "2"){
                scan.close();
                System.out.println("複数検知処理に移ります");
                MultiSearch();
            }else{
                System.out.println("プログラムを終了します");
            }
            
            //インスタンス作成　--今後新しい処理を作成した際に変更する可能性あり。

            //実際の入出力の際に使用する --デバッグのために使用しない。
            //引数が存在しない場合に処理を終了する。

            /*if(args.length ==0){
                System.out.println("適切な引数を指定できていません。");
                return;
            }*/
            //実際の入出力の受け取りは args --デバッグ用としてScannerを使用。
            //デバッグ用入力受け取り
            Scanner scanner = new Scanner(System.in);
            String CheckDomain = scanner.nextLine();

            System.out.println("--------------------------------");
            //この箇所にeTLD取得を記述していく。また、その結果をリストに保存して受け渡す。
            //メソッドの実行 --既存のプログラムでは 第一引数のみを指定する。
            List<HashMap<String,Boolean>> AnswerList = new ArrayList<>();
            
            System.out.println(AnswerList);
    }
    public static void OneSearch(){
        //単独での検知処理
        System.out.println("検知したいサブドメインを入力してください");
        Scanner scanner = new Scanner(System.in);
        String CheckDomain = scanner.nextLine();
        scanner.close();
        //検知処理
        Core newGetConnection =new Core(); 
        List<HashMap<String,Boolean>> AnswerList = new ArrayList<>();
        AnswerList = newGetConnection.GetRequestValue(CheckDomain);
        //取得処理
        for (HashMap<String,Boolean> Data : AnswerList){
            String AnswerURI = Data.getKey();
            Boolean AnswerBoolean = Data.get("SecondValue");

        }
    }
    public static void MultiSearch(){
        //複数での検知処理
        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }
}
