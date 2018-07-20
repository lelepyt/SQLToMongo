package org.sqltomongo.sql;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueryParserSQLTest {

    @Test
    public void parseQuerySimpleColumnSelectTest() {

        final String sqlQueryToTest = "select name from test";
        final String columnNameExpected = "name";

        QueryParserSQL queryParserSQL = new QueryParserSQL();
        QueryDataSQL queryDataSQL = queryParserSQL.parseQuery(sqlQueryToTest);

        Assertions.assertEquals(columnNameExpected, queryDataSQL.getSelect());
    }

}
