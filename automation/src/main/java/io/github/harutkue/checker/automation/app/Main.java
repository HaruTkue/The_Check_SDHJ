package io.github.harutkue.checker.automation.app;
import io.github.harutkue.checker.core.checker.Core;

import io.github.harutkue.checker.automation.automationChecker.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
//入出力機能だけ
public class Main{
    public static void main(String[] args){
        System.out.println("自動化機能");
        Scanner scanner = new Scanner(System.in);
        System.out.println("リクエストするドメインを入力してください。");
        //RequestDomainを取得する
        String RQDomain =scanner.nextLine();
        Auto CheckRequest = new Auto();
        //リクエスト処理を実行する
        CheckRequest.Request(RQDomain);
    }
}