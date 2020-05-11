package Model.Databases;

/**
 * Manages the database used for admin stuff
 */
import Model.Constants.DatabaseType;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import java.util.ArrayList;
import java.util.List;


public class AdminDatabase {
    private final static String DATABASE_TABLE = DatabaseType.ADMIN_TABLE.name();
    private static DynamoDB dynamoDB;
    private static AmazonDynamoDB client;
    private static Table table;

    /**
     * Creates a connection with the database and gets the table.
     * USERNAME IS THE PRIMARY KEY AND IS USED TO ACCESS DATA ABOUT A PARTICULAR USER
     */


    private static void init() {
        client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable(DATABASE_TABLE);
    }

    @Deprecated
    @SuppressWarnings("unused")
    public void addAdminUser(String adminnumber, String emailAddress, String password)
    {
        try {
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("admin_number", adminnumber)
                            .withString("email", emailAddress)
                            .withString("password",password));

            outcome.getPutItemResult();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static List<String> returnAdminInfo(String admin){
        init();
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("admin_number", admin);
        Item outcome = table.getItem(spec);
        List<String> accountInfo = new ArrayList<>(2);
        accountInfo.add(outcome.getString("email"));
        accountInfo.add(outcome.getString("password"));
        return accountInfo;
    }

    @SuppressWarnings("unused")
    @Deprecated
    public String returnAdminPassword(String admin){
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("admin_number", admin);
        Item outcome = table.getItem(spec);
        String admin_password = outcome.getString("password");
        return admin_password;
    }

}

