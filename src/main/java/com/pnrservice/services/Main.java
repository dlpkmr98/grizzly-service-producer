/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pnrservice.services;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author DILIP
 */
public class Main {

    public static String BASE_URI;

    public static HttpServer startServer() throws IOException {
        System.out.println("\"Async resources\" Kafka Producer App");
        
        // Base URI the  HTTP server will listen on
        BASE_URI = "localhost:8080";
        System.out.println("Trying to starting service at: " + BASE_URI);
        URI httpUri = UriBuilder.fromPath("/").scheme("http").host("localhost").port(8080).build();
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(httpUri, create());
        return server;
    }

    /**
     * Main method.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.shutdownNow();
                }
            }));
            server.start();
            System.out.println(String.format("Application started and available at "
                    + "%s\n Stop the application using CTRL+C", BASE_URI));
            Thread.currentThread().join();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }
    
    public static ResourceConfig create() {
        final ResourceConfig resourceConfig = new ResourceConfig()
                .registerClasses(PNRXMLService.class)
                .registerInstances(new LoggingFeature(Logger.getLogger(Main.class.getName()), PAYLOAD_ANY));
        return resourceConfig;

    }

}
