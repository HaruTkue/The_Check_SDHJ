package io.github.harutkue.checker.core.checker;
//チェック時に使用する取得レコード,レコードのタイプ、元ドメインのデータ
public record CheckerRecords(String Record,String RecordType,String DomainData) {}
