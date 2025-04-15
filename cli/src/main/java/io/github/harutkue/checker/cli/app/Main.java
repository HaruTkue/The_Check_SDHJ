package io.github.harutkue.checker.cli.app;
import io.github.harutkue.checker.core.*;
import io.github.harutkue.checker.core.network.Network;
import java.util.Scanner;

public class Main {
        public static void main(String[] args){
            Network GetAns = new Network(); //インスタンス作成　--今後新しい処理を作成した際に変更する可能性あり。

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
            GetAns.Get_CNAME(CheckDomain);
            
    }
}
