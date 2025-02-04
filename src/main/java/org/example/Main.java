package org.example;


import org.xbill.DNS.*;

import java.net.InetAddress;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("確認を行うドメインを入力してください");

        String CheckDomain = scanner.nextLine();
        System.out.println(CheckDomain +"の設定を検索します。");
        scanner.close();

    }
}