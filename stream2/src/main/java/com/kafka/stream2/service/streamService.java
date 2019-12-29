package com.kafka.stream2.service;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.kafka.stream2.model.Address;
import com.kafka.stream2.model.PatientAddress;

@EnableKafka
@EnableKafkaStreams
@Configuration
public class streamService {
	private String patientTopic="member";
	private String addressTopic="address";
	private String outputTopic="output";
	private String stateStoreName="MemberStore";

	@Bean(name=KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration kStreamConfig(KafkaProperties properties) {
		Map<String,Object> config=new HashMap<>();
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091,localhost:9092,localhost:9093");
		config.put(StreamsConfig.APPLICATION_ID_CONFIG,"streamEditor");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
		return new KafkaStreamsConfiguration(config);
	}
	
	@SuppressWarnings("unused")
	@Bean
	public KTable<String , PatientAddress> kStream(StreamsBuilder streamsBuilder) throws InterruptedException {
		KeyValueBytesStoreSupplier stateStore=Stores.persistentKeyValueStore(stateStoreName);
		//KStream<String, Address> addressStream=streamsBuilder.stream(addressTopic,Consumed.with(Serdes.String(),new JsonSerde<>(Address.class)));
		KTable<String, String> patientTable=streamsBuilder
				.table(patientTopic,Materialized.<String,String>as(stateStore)
				.withValueSerde(Serdes.String())
				.withKeySerde(Serdes.String()));
		System.out.println("lol");
		this.getthis("1",streamsBuilder);
		return null;
		
	}
	
	public void getthis(String key,StreamsBuilder streamsBuilder) throws InterruptedException{
		Topology topology=streamsBuilder.build();
		Properties config=new Properties();
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091,localhost:9092,localhost:9093");
		config.put(StreamsConfig.APPLICATION_ID_CONFIG,"streamEditor");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());
		config.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
		
		KafkaStreams streams=new KafkaStreams(topology,config);
		streams.start();
		String p="test";
		method(streams,key);
		streams.close();
		System.out.println(p);
	}
	public void method(KafkaStreams streams,String key) throws InterruptedException {

		try {
			
			ReadOnlyKeyValueStore<String, String> readOnlyKeyValueStore=streams.store(stateStoreName, QueryableStoreTypes.keyValueStore());
			String p= readOnlyKeyValueStore.get(key);
			System.out.println(key+"::::::"+p);
			}catch(InvalidStateStoreException ignored) {
				Thread.sleep(100);
				method(streams, key);	
			}
		
		
	}
	

}
