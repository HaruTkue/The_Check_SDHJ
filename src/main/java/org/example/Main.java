package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("確認を行うドメインを入力してください");

        String CheckDomain = scanner.nextLine();
        System.out.println(CheckDomain +"の設定を検索します。");
        scanner.close();

        try {
            Lookup lookup = new Lookup(CheckDomain, Type.CNAME);
            lookup.run();

            if(lookup.getResult() == Lookup.SUCCESSFUL){
                for (Record record : lookup.getAnswers()){
                    CNAMERecord cname = (CNAMERecord) record;
                    System.out.println(cname.getTarget());
                }
            }
        }catch (TextParseException e){

        }

    }
}