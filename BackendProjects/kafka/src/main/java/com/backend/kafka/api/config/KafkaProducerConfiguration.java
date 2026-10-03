package com.backend.kafka.api.config;

import com.backend.kafka.config.KafkaClientProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfiguration {

    @Bean(destroyMethod = "close")
    public Producer<String, String> orderProducer() {
        return new KafkaProducer<>(KafkaClientProperties.producerProperties());
    }
}
