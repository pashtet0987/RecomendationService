/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.RecomendationService.kafka;

import by.pashkavlushka.RecomendationService.dto.RecomendationDTO;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.stax.StAXResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@Configuration
public class KafkaConfiguration {

    Map<String, Object> consumerProperties(KafkaFileProperties kafkaFileProperties) {
        Map<String, Object> properties = new HashMap();
        properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "recomendations");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaFileProperties.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        properties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class.getName());
        properties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class.getName());
        properties.put(JacksonJsonDeserializer.KEY_DEFAULT_TYPE, "java.lang.Long");
        properties.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, RecomendationDTO.class.getName());
        properties.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
        properties.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "by.pashkavlushka.RecomendationService.dto");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    @Bean
    //@DependsOn({"consumerProperties"})
    public ConsumerFactory<Long, RecomendationDTO> defaultKafkaConsumerFactory(KafkaFileProperties kafkaFileProperties) {
        DefaultKafkaConsumerFactory<Long, RecomendationDTO> factory = new DefaultKafkaConsumerFactory<>(consumerProperties(kafkaFileProperties));
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        ConsumerRecordRecoverer emptyRecoverer = new ConsumerRecordRecoverer() {
            @Override
            public void accept(ConsumerRecord<?, ?> t, Exception u) {
                u.printStackTrace();
            }
        };
        DefaultErrorHandler handler = new DefaultErrorHandler(emptyRecoverer);

        return handler;
    }

    @Bean
    @DependsOn({"defaultKafkaConsumerFactory", "errorHandler"})
    public ConcurrentKafkaListenerContainerFactory<Long, RecomendationDTO> kafkaListenerContainerFactory(@Qualifier("defaultKafkaConsumerFactory") ConsumerFactory<Long, RecomendationDTO> consumerFactory, DefaultErrorHandler handler) {
        ConcurrentKafkaListenerContainerFactory<Long, RecomendationDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(handler);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public NewTopic recomendationsTopic() {
        return new NewTopic("recomendations-topic", 3, (short) 1);
    }

    @Bean
    @Profile("test")
    public DefaultKafkaProducerFactory<Long, RecomendationDTO> kafkaProducerFactory(KafkaFileProperties kafkaFileProperties) {
        Map<String, Object> properties = new HashMap();
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class.getName());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaFileProperties.getBootstrapServers());
        DefaultKafkaProducerFactory<Long, RecomendationDTO> kafkaProducerFactory = new DefaultKafkaProducerFactory<>(properties);
        return kafkaProducerFactory;
    }

    @Bean
    @Profile("test")
    public KafkaTemplate<Long, RecomendationDTO> kafkaTemplate(DefaultKafkaProducerFactory<Long, RecomendationDTO> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @Profile("test")
    @DependsOn({"kafkaFileProperties"})
    public KafkaConsumer<Long, RecomendationDTO> kafkaConsumer(KafkaFileProperties kafkaFileProperties) {
        Map<String, Object> properties = consumerProperties(kafkaFileProperties);
        return new KafkaConsumer<>(properties);
    }

}
