package org.sqltomongo.sql;

public enum QueryKeyWordsSQLEnum {

    SELECT("select", 1), FROM("from", 1), WHERE("where", 1), AND("and", 1), OR("or", 1),
    ORDER_BY("order", 2), DESC("desc", 0), LIMIT("limit", 1), SKIP("skip", 1);

    String name;
    int valuePosition;

    QueryKeyWordsSQLEnum(String name, int valuePosition) {

        this.name = name;
        this.valuePosition = valuePosition;
    }

   public String getName() {

        return name;
   }

   public int getValuePosition() {

        return valuePosition;
   }

}
