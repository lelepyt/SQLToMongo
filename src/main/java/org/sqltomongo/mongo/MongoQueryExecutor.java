package org.sqltomongo.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import org.sqltomongo.sql.QueryDataSQL;

import static org.sqltomongo.mongo.MongoQueryResult.MAX_SIZE_FOR_ARRAY;

public class MongoQueryExecutor {

    /**
     * Connects to Mongo DB and executes the SQL query.
     *
     * @param queryDataSQL
     * @return
     */
    public MongoQueryResult execute(QueryDataSQL queryDataSQL) {

        MongoProperties mongoProperties = new MongoProperties();
        String[] AllInfFromTable = new String[MAX_SIZE_FOR_ARRAY];

        int numberOfItems = 0;
        try {
            MongoClient mongoClient = new MongoClient(mongoProperties.getHost(), mongoProperties.getPort());
            DB db = mongoClient.getDB(mongoProperties.getCollection());
            String from = queryDataSQL.getFrom();
            DBCollection coll = db.getCollection(from);
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                String a = cursor.next().toString();
                AllInfFromTable[numberOfItems] = a;
                numberOfItems++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        MongoCollectionParser mongoCollectionParser = new MongoCollectionParser(queryDataSQL);
        return mongoCollectionParser.parseMongoOutput(AllInfFromTable, numberOfItems);
    }
}
