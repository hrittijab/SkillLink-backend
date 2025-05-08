package com.skilllink;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@SpringBootTest
class SkilllinkBackendApplicationTests {

    @MockBean
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Test
    void contextLoads() {
    }
}
