/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pnrservice.producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * @author DILIP
 */
public class PNRProducer {

    public static void sendDataToPNRProducer(String data) {
        //Set kafka producer configuration       
        Producer<String, String> producer = new KafkaProducer<>(ProducerPropertiesLoader.getProducerPropertiesLoader().getProp());
        try {
            if (data.equals("") || data == null) {
            } else {
                final ProducerRecord<String, String> record
                        = new ProducerRecord<>(ProducerPropertiesLoader.getProducerPropertiesLoader().getProp().getProperty("topic.name"), data);
                producer.send(record);
                // System.out.println("send successfully…." );
//                producer.send(record, new Callback() {
//                    @Override
//                    public void onCompletion(RecordMetadata metadata, Exception e) {
//                        if (e != null) {
//                            // log.debug("Send failed for record {}", record, e);
//                            System.out.println("Send failed for record {}" + record.value() + " " + e);
//                        } else {
//                            System.out.println("send successfully…." +record);
//                        }
//                    }
//                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }

    }

}
