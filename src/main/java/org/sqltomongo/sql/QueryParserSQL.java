package org.sqltomongo.sql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParserSQL {

    /**
     * Parses string SQL query by space and sets into {@link QueryDataSQL}
     *
     * @param inputRequest
     * @return
     */
    public QueryDataSQL parseQuery(String inputRequest) {

        QueryDataSQL queryDataSQL = new QueryDataSQL();
        String delimeter = " ";
        String[] splitArrayQueryBySpaces = inputRequest.split(delimeter);

        Map<String, String> queryDataSQLMap = new HashMap<>();
        List<String> list =  Arrays.asList(splitArrayQueryBySpaces);

        for(QueryKeyWordsSQLEnum keyWord : QueryKeyWordsSQLEnum.values()) {

            if (list.contains(keyWord.getName())) {
                String value = list.get(list.indexOf(keyWord.getName()) + keyWord.getValuePosition());
                queryDataSQLMap.put(keyWord.getName(), value);
            }
        }
        queryDataSQL.setData(queryDataSQLMap);

        return queryDataSQL;
    }
}
