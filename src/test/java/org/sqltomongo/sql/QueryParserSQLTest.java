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

    @Test
    public void parseQuerySimpleWhereTest() {

        final String sqlQueryToTest = "select name from test where type=developer and age=23 order by name desc limit 3 skip 2";
        final String columnNameExpected = "name";
        final String collectionNameExpected = "test";
        final String whereExpected = "type=developer";
        final String andExpected = "age=23";
        final String orderByExpected = "name";
        final String descExpected = "desc";
        final String limitExpected = "3";
        final String skipExpected = "2";

        QueryParserSQL queryParserSQL = new QueryParserSQL();
        QueryDataSQL queryDataSQL = queryParserSQL.parseQuery(sqlQueryToTest);

        Assertions.assertEquals(columnNameExpected, queryDataSQL.getSelect());
        Assertions.assertEquals(collectionNameExpected, queryDataSQL.getFrom());
        Assertions.assertEquals(whereExpected, queryDataSQL.getWhere());
        Assertions.assertEquals(andExpected, queryDataSQL.getAnd());
        Assertions.assertEquals(orderByExpected, queryDataSQL.getOrderBy());
        Assertions.assertEquals(descExpected, queryDataSQL.getOrderByForAscOrDesc());
        Assertions.assertEquals(limitExpected, queryDataSQL.getLimit());
        Assertions.assertEquals(skipExpected, queryDataSQL.getSkip());
    }
}
