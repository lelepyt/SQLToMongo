package org.sqltomongo.mongo;

import java.util.Objects;

public class MongoQueryResult {

    public static final int MAX_SIZE_FOR_ARRAY = 1000;

    private String[][] mongoCollectionMatrix = null;
    private String[] mongoCollectionArray = null;

    public String[][] getMongoCollectionMatrix() {

        return mongoCollectionMatrix;
    }

    public String[] getMongoCollectionArray() {
        return mongoCollectionArray;
    }

    public void setMongoCollectionMatrix(String[][] mongoCollectionMatrix) {

        this.mongoCollectionMatrix = mongoCollectionMatrix;
    }

    public void setMongoCollectionArray(String[] mongoCollectionArray) {

        this.mongoCollectionArray = mongoCollectionArray;
    }

    private void printMongoCollectionArray() {

        for (int i = 0; i < mongoCollectionArray.length; i++) {
            if (mongoCollectionArray[i] != null) {
                System.out.println(mongoCollectionArray[i]);
            }
        }
    }

    private void printMongoCollectionMatrix() {

        for (int iteratorForColumns = 0; iteratorForColumns < mongoCollectionMatrix[iteratorForColumns].length
                - 1; iteratorForColumns++) {

            for (int iteratorForRow = 0; iteratorForRow < mongoCollectionMatrix[iteratorForRow].length - 1; iteratorForRow++) {
                if (mongoCollectionMatrix[iteratorForColumns][iteratorForRow] != null) {
                    System.out.println(mongoCollectionMatrix[iteratorForColumns][iteratorForRow]);
                }
            }
        }
    }

    public void printResultToConsole() {

        if (!Objects.isNull(mongoCollectionMatrix) && !Objects.isNull(mongoCollectionArray)) {
            throw new RuntimeException("Found two results to print, expected only one.");

        } else
            if (!Objects.isNull(mongoCollectionMatrix)) {
            printMongoCollectionMatrix();

        } else if (!Objects.isNull(mongoCollectionArray)) {
            printMongoCollectionArray();

        } else {
            System.out.println("Empty result.");
        }
    }
}
