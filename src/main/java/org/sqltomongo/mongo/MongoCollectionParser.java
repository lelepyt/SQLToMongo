package org.sqltomongo.mongo;

import org.sqltomongo.sql.QueryDataSQL;
import java.util.Arrays;
import java.util.Collections;

import static org.sqltomongo.mongo.MongoQueryResult.MAX_SIZE_FOR_ARRAY;

public class MongoCollectionParser {

    private final QueryDataSQL queryDataSQL;

    public MongoCollectionParser(QueryDataSQL queryDataSQL) {
        this.queryDataSQL = queryDataSQL;
    }

    /**
     * Parses output and gets collection data.
     *
     * @param allElementInTable
     * @param numberOfItems
     * @return
     */
    public MongoQueryResult parseMongoOutput(String[] allElementInTable, int numberOfItems) {

        int i = 0;
        String selectedWord;
        String delimeter = "}";
        String subStr2[] = new String[MAX_SIZE_FOR_ARRAY];
        while (i <= numberOfItems) {
            selectedWord = allElementInTable[i];
            String subStr[] = selectedWord.split(delimeter);
            subStr2[i] = subStr[1];
            if (i == numberOfItems - 1) {
                return parseFieldsOfCollection(subStr2, numberOfItems);
            }
            i++;

        }
        // Return empty result.
        return new MongoQueryResult();
    }

    private MongoQueryResult parseFieldsOfCollection(String[] subStr2, int numberOfItems) {

        int i = 0;
        String selectedWord;
        String delimeter = ",";
        String matrixWithAllElements[][] = new String[MAX_SIZE_FOR_ARRAY][MAX_SIZE_FOR_ARRAY];
        while (i < numberOfItems) {
            selectedWord = subStr2[i];
            String arrayForSorting[] = selectedWord.split(delimeter);
            for (int iteratorForMatrix = 0; iteratorForMatrix < arrayForSorting.length; iteratorForMatrix++) {
                matrixWithAllElements[i][iteratorForMatrix] = arrayForSorting[iteratorForMatrix];
            }
            i++;
        }

        return processCollectionData(matrixWithAllElements);
    }

    /**
     * Processes the mongo collection based on SQL query.
     *
     * @param matrixWithAllElement
     * @return
     */
    private MongoQueryResult processCollectionData(String[][] matrixWithAllElement) {

        MongoQueryResult mongoQueryResult = new MongoQueryResult();

        String[][] mongoCollectionMatrix = new String[MAX_SIZE_FOR_ARRAY][MAX_SIZE_FOR_ARRAY];
        String[] mongoCollectionArray = new String[MAX_SIZE_FOR_ARRAY];

        String tableName = null;
        String whatSelect = null;

        if(queryDataSQL.getWhere()!=null && !queryDataSQL.getWhere().isEmpty()) {
            String[] where = parseStringForWhere(queryDataSQL.getWhere());
            tableName = where[0];
            whatSelect = where[1];
            tableName = tableName.concat("\"");
            whatSelect = whatSelect.concat("\"");
        }

        if(queryDataSQL.getWhere() == null) {
            // Simple select, return result.
            return setSelectItem(mongoCollectionArray, matrixWithAllElement);

        } else if (queryDataSQL.getSelect().equals("*") && queryDataSQL.getWhere() != null) {
            mongoCollectionMatrix = transferAllElementsFromArrayForSelect(matrixWithAllElement, mongoCollectionMatrix, whatSelect);
        }

        mongoCollectionMatrix = checkIfWhereHasOperatorsAndProcess(matrixWithAllElement, tableName, whatSelect, mongoCollectionMatrix);

        if (queryDataSQL.getOrderBy()!= null && !queryDataSQL.getOrderBy().isEmpty()) {
            mongoCollectionMatrix = sotrMatrixForOrderBy(mongoCollectionMatrix, queryDataSQL.getOrderBy(), queryDataSQL.getOrderByForAscOrDesc());
        }
        mongoCollectionArray = convertElementsFromMatrixInArray(mongoCollectionMatrix, mongoCollectionArray, queryDataSQL.getSelect());

        if (queryDataSQL.getSkip() != null && !queryDataSQL.getSkip().isEmpty()) {
            mongoCollectionArray = parseArrayForSkip(mongoCollectionArray, queryDataSQL.getSkip());
        }
        if (queryDataSQL.getLimit() != null && !queryDataSQL.getLimit().isEmpty()) {
            mongoCollectionArray = parseArrayForLimit(mongoCollectionArray, queryDataSQL.getLimit());
        }

        if (queryDataSQL.getLimit() == null && queryDataSQL.getSkip() == null
                && queryDataSQL.getOrderBy() == null && queryDataSQL.getSelect().equals("*")) {
            mongoQueryResult.setMongoCollectionMatrix(mongoCollectionMatrix);

        } else {
            mongoQueryResult.setMongoCollectionArray(mongoCollectionArray);

        }

        return mongoQueryResult;
    }

    private MongoQueryResult setSelectItem(String[] mongoCollectionArray, String[][] matrixWithAllElement) {

        String stringForTransformationFromTwoDidmensioalArray;
        int helpsIterator = 0;
        MongoQueryResult mongoQueryResult = new MongoQueryResult();

        if (queryDataSQL.getSelect().equals("*")) {
            mongoQueryResult.setMongoCollectionMatrix(matrixWithAllElement);

        } else {

            for (int iteratorForColumn = 0; iteratorForColumn < matrixWithAllElement[iteratorForColumn].length
                    - 1; iteratorForColumn++) {

                for (int iteratorForRow = 0; iteratorForRow < matrixWithAllElement[iteratorForRow].length - 1; iteratorForRow++) {

                    if ((matrixWithAllElement[iteratorForColumn][iteratorForRow] != null)
                            && (matrixWithAllElement[iteratorForColumn][iteratorForRow].indexOf(queryDataSQL.getSelect()) > 0)) {

                        stringForTransformationFromTwoDidmensioalArray = matrixWithAllElement[iteratorForColumn][iteratorForRow];
                        mongoCollectionArray[helpsIterator] = stringForTransformationFromTwoDidmensioalArray;
                        helpsIterator++;
                    }
                }
            }
            mongoQueryResult.setMongoCollectionArray(mongoCollectionArray);
        }
        return mongoQueryResult;
    }

    /**
     * Copies all fields from matrixWithAllElement in mongoCollectionMatrix which fit the condition.
     *
     * @param matrixWithAllElement
     * @param mongoCollectionMatrix
     * @param whatSelect
     */
    private String[][] transferAllElementsFromArrayForSelect(String[][] matrixWithAllElement, String[][] mongoCollectionMatrix, String whatSelect) {

        int newIteratorForColumn = 0;
        for (int iteratorForColumn = 0; iteratorForColumn < matrixWithAllElement[iteratorForColumn].length - 1; iteratorForColumn++) {

            for (int iteratorForRow = 0; iteratorForRow < matrixWithAllElement[iteratorForRow].length - 1; iteratorForRow++) {

                if ((matrixWithAllElement[iteratorForColumn][iteratorForRow] != null) && (matrixWithAllElement[iteratorForColumn][iteratorForRow].indexOf(whatSelect) > 0)) {

                    for (int newIteratorForRow = 0; newIteratorForRow < matrixWithAllElement[iteratorForRow].length
                            - 1; newIteratorForRow++) {

                        mongoCollectionMatrix[newIteratorForColumn][newIteratorForRow] = matrixWithAllElement[iteratorForColumn][newIteratorForRow];
                    }
                    newIteratorForColumn++;

                }
            }
        }
        return mongoCollectionMatrix;
    }

    /**
     * Checks if we have AND, OR operators in the query.
     *
     * @param matrixWithAllElement
     * @param tableName
     * @param whatSelect
     * @param mongoCollectionMatrix
     * @return
     */
    private String[][] checkIfWhereHasOperatorsAndProcess(String[][] matrixWithAllElement, String tableName, String whatSelect, String[][] mongoCollectionMatrix) {

        if( queryDataSQL.getAnd()!= null && !queryDataSQL.getAnd().isEmpty()){
            transferSomeElementFromArrayForWhere(matrixWithAllElement, mongoCollectionMatrix, tableName, whatSelect);
            String[] andWordArray = parseStringForWhere(queryDataSQL.getAnd());
            String tableNameForAnd = andWordArray[0];
            String  whatSelectForAnd = andWordArray[1];
            mongoCollectionMatrix = transferSelectedElementFromArrayForAnd(mongoCollectionMatrix, tableNameForAnd, whatSelectForAnd);

        } else if (queryDataSQL.getOr() != null && !queryDataSQL.getOr().isEmpty()){
            String [] orWordArray = parseStringForWhere(queryDataSQL.getOr());
            String tableNameForOr = orWordArray[0];
            String whatSelectForOr = orWordArray[1];
            mongoCollectionMatrix = transferSomeElementFromArrayForOr(matrixWithAllElement, mongoCollectionMatrix, tableName, whatSelect, tableNameForOr, whatSelectForOr);

        } else {
            mongoCollectionMatrix = transferSomeElementFromArrayForWhere(matrixWithAllElement, mongoCollectionMatrix, tableName, whatSelect);
        }

        return mongoCollectionMatrix;
    }

    /**
     * Copies an item from matrixWithAllElement in mongoCollectionMatrix if item fits the condition.
     *
     * @param matrixWithAllElement
     * @param mongoCollectionMatrix
     * @param tableName
     * @param whatSelect
     * @return
     */
    private String[][] transferSomeElementFromArrayForWhere(String[][] matrixWithAllElement, String[][] mongoCollectionMatrix, String tableName, String whatSelect) {

        int newIteratorForColumn = 0;
        for (int iteratorForColumn = 0; iteratorForColumn < matrixWithAllElement[iteratorForColumn].length
                - 1; iteratorForColumn++) {

            for (int iteratorForRow = 0; iteratorForRow < matrixWithAllElement[iteratorForRow].length - 1; iteratorForRow++) {

                if ((matrixWithAllElement[iteratorForColumn][iteratorForRow] != null)
                        && (matrixWithAllElement[iteratorForColumn][iteratorForRow].indexOf(tableName) > 0)
                        && (matrixWithAllElement[iteratorForColumn][iteratorForRow].indexOf(whatSelect) > 0)) {

                    for (int newIteratorForRow = 0; newIteratorForRow < matrixWithAllElement[iteratorForRow].length
                            - 1; newIteratorForRow++) {

                        mongoCollectionMatrix[newIteratorForColumn][newIteratorForRow] = matrixWithAllElement[iteratorForColumn][newIteratorForRow];

                    }
                    newIteratorForColumn++;
                }
            }
        }
        return mongoCollectionMatrix;
    }

    private String[][] transferSomeElementFromArrayForOr(String[][] matrixWithAllElement, String[][] mongoCollectionMatrix,
                                                         String tableName, String whatSelect, String tableNameForOr, String whatSelectForOr) {

        int newIteratorForColumn = 0;
        for (int iteratorForColumn = 0; iteratorForColumn < matrixWithAllElement[iteratorForColumn].length - 1; iteratorForColumn++) {

            for (int iteratorForRow = 0; iteratorForRow < matrixWithAllElement[iteratorForRow].length - 1; iteratorForRow++) {

                if ((matrixWithAllElement[iteratorForColumn][iteratorForRow] != null)
                        && (matrixWithAllElement[iteratorForColumn][iteratorForRow].indexOf(tableName) > 0)
                        && (matrixWithAllElement[iteratorForColumn][iteratorForRow].indexOf(whatSelect) > 0)) {

                    for (int newIteratorForRow = 0; newIteratorForRow < matrixWithAllElement[iteratorForRow].length - 1; newIteratorForRow++) {
                        mongoCollectionMatrix[newIteratorForColumn][newIteratorForRow] = matrixWithAllElement[iteratorForColumn][newIteratorForRow];
                    }
                    newIteratorForColumn++;

                } else if ((matrixWithAllElement[iteratorForColumn][iteratorForRow] != null)
                        && (matrixWithAllElement[iteratorForColumn][iteratorForRow].indexOf(tableNameForOr) > 0)
                        && (matrixWithAllElement[iteratorForColumn][iteratorForRow].indexOf(whatSelectForOr) > 0)) {

                    for (int newIteratorForRow = 0; newIteratorForRow < matrixWithAllElement[iteratorForRow].length
                            - 1; newIteratorForRow++) {

                        mongoCollectionMatrix[newIteratorForColumn][newIteratorForRow] = matrixWithAllElement[iteratorForColumn][newIteratorForRow];

                    }
                    newIteratorForColumn++;
                }
            }
        }
        return mongoCollectionMatrix;
    }

    private String[][] transferSelectedElementFromArrayForAnd(String[][] mongoCollectionMatrix, String tableNameForAnd, String whatSelectForAnd) {

        int newIteratorForColumn = 0;
        String[][] arrayForOperationAnd = new String[MAX_SIZE_FOR_ARRAY][MAX_SIZE_FOR_ARRAY];

        for (int iteratorForColumn = 0; iteratorForColumn < mongoCollectionMatrix[iteratorForColumn].length - 1; iteratorForColumn++) {

            for (int iteratorForRow = 0; iteratorForRow < mongoCollectionMatrix[iteratorForRow].length - 1; iteratorForRow++) {

                if ((mongoCollectionMatrix[iteratorForColumn][iteratorForRow] != null)
                        && (mongoCollectionMatrix[iteratorForColumn][iteratorForRow].indexOf(tableNameForAnd) > 0)
                        && (mongoCollectionMatrix[iteratorForColumn][iteratorForRow].indexOf(whatSelectForAnd) > 0)) {

                    for (int newIteratorForRow = 0; newIteratorForRow < mongoCollectionMatrix[iteratorForRow].length - 1; newIteratorForRow++) {

                        arrayForOperationAnd[newIteratorForColumn][newIteratorForRow] = mongoCollectionMatrix[iteratorForColumn][newIteratorForRow];

                    }
                    newIteratorForColumn++;
                }
            }
        }
        return arrayForOperationAnd;
    }

    private String[] convertElementsFromMatrixInArray(String[][] mongoCollectionMatrix, String[] arrayForPrint, String select) {

        String stringForTransformationFromTwoDimensionalArray;
        int helperIterator = 0;
        for (int iteratorForColumn = 0; iteratorForColumn < mongoCollectionMatrix[iteratorForColumn].length - 1; iteratorForColumn++) {

            for (int iteratorForRow = 0; iteratorForRow < mongoCollectionMatrix[iteratorForRow].length - 1; iteratorForRow++) {

                if ((mongoCollectionMatrix[iteratorForColumn][iteratorForRow] != null)
                        && (mongoCollectionMatrix[iteratorForColumn][iteratorForRow].indexOf(select) > 0)) {

                    stringForTransformationFromTwoDimensionalArray = mongoCollectionMatrix[iteratorForColumn][iteratorForRow];
                    arrayForPrint[helperIterator] = stringForTransformationFromTwoDimensionalArray;
                    helperIterator++;

                }
            }
        }
        return arrayForPrint;
    }

    private String[][] sotrMatrixForOrderBy(String[][] mongoCollectionMatrix, String orderBy, String orderByForAscOrDesc) {

        String[] arrayForSearch = new String[MAX_SIZE_FOR_ARRAY];

        arrayForSearch = convertElementsFromMatrixInArray(mongoCollectionMatrix, arrayForSearch, orderBy);

        if (orderBy != null && !orderBy.isEmpty()) {
            arrayForSearch = parseArrayForSortUseOrderBy(arrayForSearch);

            if("asc".equalsIgnoreCase(orderByForAscOrDesc)) {
                    Arrays.sort(arrayForSearch);
                } else {
                    Arrays.sort(arrayForSearch, Collections.reverseOrder());
            }
        }
        return findSelectedItemsInMatrixForOrderBy(mongoCollectionMatrix, arrayForSearch);
    }

    private String[][] findSelectedItemsInMatrixForOrderBy(String[][] mongoCollectionMatrix, String[] arrayForSearch) {

        String selectedWordFromArray;
        String[][] arrayForSelectedRow = new String[MAX_SIZE_FOR_ARRAY][MAX_SIZE_FOR_ARRAY];
        int newIteratorForColumn = 0;
        for (int i = 0; i < arrayForSearch.length; i++) {
            selectedWordFromArray = arrayForSearch[i];
            for (int iteratorForColumn = 0; iteratorForColumn < mongoCollectionMatrix[iteratorForColumn].length - 1; iteratorForColumn++) {

                for (int iteratorForRow = 0; iteratorForRow < mongoCollectionMatrix[iteratorForRow].length - 1; iteratorForRow++) {

                    if ((mongoCollectionMatrix[iteratorForColumn][iteratorForRow] != null)
                            && (mongoCollectionMatrix[iteratorForColumn][iteratorForRow].indexOf(selectedWordFromArray) > 0)) {

                        for (int newIteratorForRow = 0; newIteratorForRow < mongoCollectionMatrix[iteratorForRow].length
                                - 1; newIteratorForRow++) {

                            arrayForSelectedRow[newIteratorForColumn][newIteratorForRow] = mongoCollectionMatrix[iteratorForColumn][newIteratorForRow];
                        }
                        newIteratorForColumn++;
                    }
                }
            }

        }
        return arrayForSelectedRow;
    }

    private String[] parseStringForWhere(String string) {

        int i = 1;
        String delimeter = "=";
        String[] dividedString = string.split(delimeter);
        if (i >= dividedString.length) {
            delimeter = "'";
            dividedString = string.split(delimeter);
        }
        return dividedString;
    }

    private String[] parseArrayForSkip(String[] arrayForPrint, String skipWord) {

        int skipInt = Integer.parseInt(skipWord);
        String[] arrayForPrintAndTransfer = new String[arrayForPrint.length];
        for (int i = 0; i < arrayForPrint.length; i++) {
            if (i >= skipInt && arrayForPrint[i] != null) {
                arrayForPrintAndTransfer[i] = arrayForPrint[i];
            }
        }
        return arrayForPrintAndTransfer;
    }

    private String[] parseArrayForLimit(String[] arrayForPrint, String limitWord) {

        int limitInt = Integer.parseInt(limitWord);
        String[] arrayForPrintAndTransfer = new String[arrayForPrint.length];
        for (int i = 0; i < limitInt; i++) {
            if (arrayForPrint[i] != null) {
                arrayForPrintAndTransfer[i] = arrayForPrint[i];
            } else if (arrayForPrint[i] == null && limitInt < arrayForPrint.length) {
                limitInt++;
            }
        }
        return arrayForPrintAndTransfer;
    }

    private String[] parseArrayForSortUseOrderBy(String[] arrayForPrint) {

        int iteratorForArray = 0;
        int i = 0;
        String selectedWord;
        String delimeter = ":";
        String[] secondDividedString = new String[MAX_SIZE_FOR_ARRAY];

        while (i < arrayForPrint.length - 1) {
            if (arrayForPrint[i] != null) {
                selectedWord = arrayForPrint[i];
                String resultString = selectedWord.replace("\"", "");
                resultString = resultString.replace(" ", "");
                String[] dividedString = resultString.split(delimeter);
                for (int j = 0; j < dividedString.length; j++) {
                    if (dividedString[j++] != null) {
                        secondDividedString[i] = dividedString[j++];
                        ++iteratorForArray;
                    } else if (dividedString[j] != null) {
                        secondDividedString[i] = dividedString[j];
                    }

                }
            }
            i++;
        }
        String[] arrayForPrintAndTransfer = new String[iteratorForArray];
        for (int iteratorFor = 0; iteratorFor <= iteratorForArray; iteratorFor++) {
            if (secondDividedString[iteratorFor] != null) {
                arrayForPrintAndTransfer[iteratorFor] = secondDividedString[iteratorFor];
            }
        }
        return arrayForPrintAndTransfer;
    }
}