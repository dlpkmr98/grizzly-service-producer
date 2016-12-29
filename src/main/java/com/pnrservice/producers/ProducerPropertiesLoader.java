/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pnrservice.producers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author DILIP
 */
public class ProducerPropertiesLoader {

    static private ProducerPropertiesLoader _instance = null;
    private Properties prop ;
    
    

    static public synchronized ProducerPropertiesLoader getProducerPropertiesLoader() {
        if (_instance == null) {
            _instance = new ProducerPropertiesLoader();
        }
        return _instance;
    }

    public  Properties getProp() {
        return prop;
    }

    private ProducerPropertiesLoader() {
        try {
            //InputStream input = new FileInputStream("resources\\producer.properties");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream input = classLoader.getResourceAsStream("producer.properties"); 
            prop = new Properties();
            prop.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
