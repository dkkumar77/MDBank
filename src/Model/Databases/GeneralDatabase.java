package Model.Databases;

/**
 * MDBank
 *
 * @author Deepak Kumar
 * Date: 5/11/20
 * Time: 11:46 AM
 * <p>
 * Brief Description:
 */
import Model.Constants.DatabaseType;

import Model.Constants.GeneralDbConstants;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.glue.model.Database;
import javax.xml.crypto.Data;

import java.util.Random;




public class GeneralDatabase {
    private final static String DATABASE_TABLE = DatabaseType.BANK_SERVER_INFO.name();
    private static DynamoDB dynamoDB;
    private static AmazonDynamoDB client;
    private static Table table;
    /**
     * Creates a connection with the database and gets the table.
     * USERNAME IS THE PRIMARY KEY AND IS USED TO ACCESS DATA ABOUT A PARTICULAR USER
     */







    @Deprecated
    @SuppressWarnings("unused")

    public long createUniqueAccountNumber(){
        char[] sequenceWriter = new char[12];
        Random random = new Random();
        sequenceWriter[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < sequenceWriter.length; i++) {
            sequenceWriter[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(sequenceWriter));

    }





    public GeneralDatabase() {
        client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable(DATABASE_TABLE);
    }





    public boolean verifyCredentials(String username, String password) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username);
        try {
            Item outcome = table.getItem(spec);
            String user = outcome.getString(GeneralDbConstants.username.name());
            String pass = outcome.getString(GeneralDbConstants.hashedPassword.name());
            if (user.equalsIgnoreCase(username) && password.equalsIgnoreCase(pass)) {
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }






    public void updateLoggedInQuery(String username){
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username);
        Item outcome = table.getItem(spec);
        boolean loggedInQuery = outcome.getBoolean(GeneralDbConstants.isLoggedOn.name());
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username)
                .withUpdateExpression("set isLoggedIn = :l")
                .withValueMap(new ValueMap().withBoolean(":l",!loggedInQuery))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);

    }




    public boolean avoidDuplicate(String username){
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbConstants.username.name(),username);
        Item outcome = table.getItem(spec);
        if (outcome == null){
            return true;
        }
        return false;
    }



    @Deprecated
    @SuppressWarnings("unused")
    public void quickUpdateLoginLogoffQuery(String username){
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username)
                .withUpdateExpression("set isLoggedIn = :l")
                .withValueMap(new ValueMap().withBoolean(":l",false))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);

    }




    public void updateEmailQuery(String username, String newEmail){
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username)
                .withUpdateExpression("set primaryEmail = :l")
                .withValueMap(new ValueMap().withString(":l",newEmail))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);
    }




    public void updatePasswordQuery(String username, String newPass){
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username)
                .withUpdateExpression("set password = :l")
                .withValueMap(new ValueMap().withString(":l",newPass))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);
    }


    @Deprecated
    @SuppressWarnings("unused")

    public void updateBalance(String username, String amount){
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username)
                .withUpdateExpression("set accountBalance = :l")
                .withValueMap(new ValueMap().withString(":l", amount))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);
    }




    public String grabFullName(String username){
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username);
        Item outcome = table.getItem(spec);
        String f = outcome.getString(GeneralDbConstants.firstName.name());
        String l = outcome.getString(GeneralDbConstants.lastName.name());

        return f + " " + l;
    }




    public String returnEmail(String username){
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username);
        Item outcome = table.getItem(spec);
        String email = outcome.getString(GeneralDbConstants.primaryEmail.name());

        return email;

    }




    public String returnHashedPass(String username){
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbConstants.username.name(), username);
        Item outcome = table.getItem(spec);
        String hashedPass = outcome.getString(GeneralDbConstants.hashedPassword.name());

        return hashedPass;



    }





    public void addUser(String username,long accountnumber, int loginAttempts, String...data){


        String firstName = data[0];
        String lastName = data[1];
        String password = data[2];
        String primaryEmail = data[3];
        String dateOfBirth = data[4];
        String creationDate = data[5];
        String accountBalance = data[6];

        {

            try {
                PutItemOutcome outcome = table
                        .putItem(new Item().withPrimaryKey(GeneralDbConstants.username.name(), username)
                                .withString(GeneralDbConstants.firstName.name(), firstName)
                                .withString(GeneralDbConstants.lastName.name(), lastName)
                                .withString(GeneralDbConstants.hashedPassword.name(), password)
                                .withString(GeneralDbConstants.primaryEmail.name(), primaryEmail)
                                .withString(GeneralDbConstants.dob.name(), dateOfBirth)
                                .withString(GeneralDbConstants.dateCreated.name(), creationDate)
                                .withLong(GeneralDbConstants.accountID.name(), accountnumber)
                                .withInt(GeneralDbConstants.loginAttempts.name(), loginAttempts)
                                .withString(GeneralDbConstants.currentBalance.name() , accountBalance)
                                .withBoolean(GeneralDbConstants.isLoggedOn.name(), false));


                outcome.getPutItemResult();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }


    }



}



