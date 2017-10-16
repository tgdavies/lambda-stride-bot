package org.kablambda.aws.dynamodb;

import java.util.Optional;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.google.common.collect.Lists;


public class DBImpl implements DB {
    private final AmazonDynamoDB client;
    private final DynamoDB dynamoDB;
    private static final String TABLE_NAME = System.getenv("TABLE_NAME");

    public DBImpl() {
        String endPoint = System.getenv("DYNAMODB_ENDPOINT");
        if (endPoint == null || endPoint.length() == 0) {
            final AwsClientBuilder.EndpointConfiguration endpointConfiguration =  new AwsClientBuilder.EndpointConfiguration("http://dynamodb.ap-southeast-2.amazonaws.com", Regions.AP_SOUTHEAST_2.getName());
            final ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setUseTcpKeepAlive(true);
            client = AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(endpointConfiguration)
                    .withClientConfiguration(clientConfiguration)
                    .build();
        } else {
            client = AmazonDynamoDBClientBuilder.standard()
                                                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                                                        System.getenv("DYNAMODB_ENDPOINT"),
                                                        "us-west-2"))
                                                .build();
        }
        dynamoDB = new DynamoDB(client);
    }

    @Override
    public void createTable() {
        try {
            try {
                dynamoDB.getTable(TABLE_NAME).describe();
                return;
            } catch (ResourceNotFoundException e) {
                // table doesn't exist
            } catch (Exception e) {
                throw new RuntimeException("Error checking whether table exists", e);
            }
            System.err.println("Attempting to create table; please wait...");
            Table table = dynamoDB.createTable(TABLE_NAME,
                    Lists.newArrayList(
                            new KeySchemaElement("cloudId", KeyType.HASH)
                    ),
                    Lists.newArrayList(
                            new AttributeDefinition("cloudId", ScalarAttributeType.S)
                    ),
                    new ProvisionedThroughput(10L, 10L)
            );
            table.waitForActive();
            System.err.println("Success.  Table status: " + table.getDescription().getTableStatus());
        } catch (Exception e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    @Override
    public void write(String cloudId, String name, String value) {
        PutItemOutcome o = dynamoDB.getTable(TABLE_NAME).putItem(
                new Item().withPrimaryKey("cloudId", cloudId + "/" + name)
                          .withString(name, value));

    }

    @Override
    public Optional<String> read(String cloudId, String name) {
        Item item = dynamoDB.getTable(TABLE_NAME).getItem("cloudId", cloudId + "/" + name);
        return item == null ? Optional.empty() : Optional.ofNullable(item.getString(name));
    }
}
