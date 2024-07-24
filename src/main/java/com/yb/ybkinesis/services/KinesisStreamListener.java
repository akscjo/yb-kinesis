package com.yb.ybkinesis.services;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.Record;
import com.amazonaws.services.kinesis.model.ShardIteratorType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KinesisStreamListener {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final AmazonKinesis kinesisClient = AmazonKinesisClientBuilder.standard().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Adjust these variables as needed
//    private final String streamName = "amchauhan-demo-1";
//    private final String shardId = "shardId-000000000003";

    @Value("${kinesis.streamName}")
    private String streamName;

    @Value("${kinesis.shardId}")
    private String shardId;


    private static final String INSERT_QUERY = "INSERT INTO yb_sales " +
            "(sale_id, item_id, quantity, sale_time, store_id, sale_price, product_name, product_category, state_code) " +
            "VALUES (?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?, ?)";

    @PostConstruct
    public void init() {
        String shardIterator = getShardIterator();
        System.out.println("Shard iterator: " + shardIterator);
        if (shardIterator != null) {
            listenToStream(shardIterator);
        }
    }

    private String getShardIterator() {
        try {
            GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
            getShardIteratorRequest.setStreamName(streamName);
            getShardIteratorRequest.setShardId(shardId);
            getShardIteratorRequest.setShardIteratorType(ShardIteratorType.LATEST.toString());

            return kinesisClient.getShardIterator(getShardIteratorRequest).getShardIterator();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    private void listenToStream(String shardIterator) {
        new Thread(() -> {
            String currentShardIterator = shardIterator;
            while (true) {
                List<Record> records = kinesisClient.getRecords(new GetRecordsRequest().withShardIterator(currentShardIterator)).getRecords();
                executorService.submit(() -> processRecords(records)); // Use processRecords for batch processing
                currentShardIterator = kinesisClient.getRecords(new GetRecordsRequest().withShardIterator(currentShardIterator)).getNextShardIterator();
//                try {
//                    Thread.sleep(1);
//                    currentShardIterator = kinesisClient.getRecords(new GetRecordsRequest().withShardIterator(currentShardIterator)).getNextShardIterator();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
            }
        }).start();
    }

    private void processRecords(List<Record> records) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (Record record : records) {
            String data = new String(record.getData().array(), StandardCharsets.UTF_8);
            System.out.println("Received data: " + data);
            try {
                JsonNode jsonNode = objectMapper.readTree(data);
                batchArgs.add(new Object[]{
                        jsonNode.get("sale_id").asText(),
                        jsonNode.get("item_id").asText(),
                        jsonNode.get("quantity").asInt(),
                        jsonNode.get("sale_time").asText(),
                        jsonNode.get("store_id").asText(),
                        jsonNode.get("sale_price").asDouble(),
                        jsonNode.get("product_name").asText(),
                        jsonNode.get("product_category").asText(),
                        jsonNode.get("state_code").asText()
                });
            } catch (IOException e) {
                System.err.println("Failed to parse JSON record: " + e.getMessage());
            }
        }
        jdbcTemplate.batchUpdate(INSERT_QUERY, batchArgs);
    }
}
