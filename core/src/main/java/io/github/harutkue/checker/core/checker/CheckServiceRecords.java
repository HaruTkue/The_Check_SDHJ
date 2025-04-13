package io.github.harutkue.checker.core.checker;

import org.json.JSONArray;

//検知時に利用するパターンのデータなど
public record CheckServiceRecords(String ServiceName, JSONArray SerivcePattern){}