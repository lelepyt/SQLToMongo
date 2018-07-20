package org.sqltomongo.sql;

import java.util.Map;
import java.util.Objects;

public class QueryDataSQL {

    private String limit = null;
    private String skip = null;
    private String orderBy = null;
    private String orderByForAscOrDesc = "asc"; // default
    private String where;
    private String and = null;
    private String or = null;
    private String select = null;
    private String from;

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getSkip() {
        return skip;
    }

    public void setSkip(String skip) {
        this.skip = skip;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderByForAscOrDesc() {
        return orderByForAscOrDesc;
    }

    public void setOrderByForAscOrDesc(String orderByForAscOrDesc) {
        this.orderByForAscOrDesc = orderByForAscOrDesc;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getAnd() {
        return and;
    }

    public void setAnd(String and) {
        this.and = and;
    }

    public String getOr() {
        return or;
    }

    public void setOr(String or) {
        this.or = or;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setData(Map<String, String> dataMap) {

        this.setLimit(dataMap.get(QueryKeyWordsSQLEnum.LIMIT.getName()));
        this.setOrderBy(dataMap.get(QueryKeyWordsSQLEnum.ORDER_BY.getName()));
        this.setAnd(dataMap.get(QueryKeyWordsSQLEnum.AND.getName()));
        this.setFrom(dataMap.get(QueryKeyWordsSQLEnum.FROM.getName()));
        this.setOr(dataMap.get(QueryKeyWordsSQLEnum.OR.getName()));
        this.setWhere(dataMap.get(QueryKeyWordsSQLEnum.WHERE.getName()));
        this.setSelect(dataMap.get(QueryKeyWordsSQLEnum.SELECT.getName()));
        this.setSkip(dataMap.get(QueryKeyWordsSQLEnum.SKIP.getName()));

        if (!Objects.isNull(dataMap.get(QueryKeyWordsSQLEnum.DESC.getName()))) {
            this.setOrderByForAscOrDesc(dataMap.get(QueryKeyWordsSQLEnum.DESC.getName()));
        }
    }
}
