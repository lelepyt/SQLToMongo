package org.sqltomongo;

import org.sqltomongo.mongo.MongoQueryExecutor;
import org.sqltomongo.mongo.MongoQueryResult;
import org.sqltomongo.sql.QueryDataSQL;
import org.sqltomongo.sql.QueryParserSQL;

import java.util.Scanner;

public class Console {

    public void consolScanner() {

        Scanner scan = new Scanner(System.in);

        QueryParserSQL queryParserSQL = new QueryParserSQL();
        System.out.println("Write SQL query:");

        String inputRequest = scan.nextLine();
        QueryDataSQL queryDataSQL = queryParserSQL.parseQuery(inputRequest);

        MongoQueryExecutor mongoQueryExecutor = new MongoQueryExecutor();
        MongoQueryResult mongoQueryResult = mongoQueryExecutor.execute(queryDataSQL);

        mongoQueryResult.printResultToConsole();
    }
}
