package com.pnrservice.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jersey.repackaged.com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.glassfish.jersey.process.JerseyProcessingUncaughtExceptionHandler;

/**
 * Root resource (exposed at "services" path)
 */
@Path("/")
public class PNRXMLService {

    public static final String POST_NOTIFICATION_RESPONSE = "Message sent";
    public static final String SERVICE_STATUS = "status";
    public static final String SERVICE_PNR_XML_SERVICE = "pnrXMLService";
    public static final String SERVICE_PNR_FILE_SERVICE = "pnrFileService";

    private static final ExecutorService QUEUE_EXECUTOR = Executors.newFixedThreadPool(2000, new ThreadFactoryBuilder()
            .setNameFormat("PNRXMLService-%d")
            .setUncaughtExceptionHandler(new JerseyProcessingUncaughtExceptionHandler())
            .build());

    /**
     * Method handling HTTP get requests.
     * @return 
     */
    @GET
    @Path(SERVICE_STATUS)
    @Produces(MediaType.TEXT_PLAIN)
    public String serviceStatus() {
        return "Service is UP !!";
    }

    /**
     * Method handling HTTP POST requests. That send pnr xml data to producer.
     *
     *
     * @param pnrXMLData
     * @param aresponse
     */
    @POST
    @Path(SERVICE_PNR_XML_SERVICE)
    public void processPNRData(final String pnrXMLData, final @Suspended AsyncResponse aresponse) {
        aresponse.setTimeoutHandler(new TimeoutHandler() {
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
            }
        });
        aresponse.setTimeout(20, TimeUnit.SECONDS);
        QUEUE_EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //sending data to pnr producer
                    //PNRProducer.sendDataToPNRProducer(pnrXMLData);
                } catch (Exception ex) {
                    aresponse.resume(ex);
                    return;
                }
                System.out.println("theread++++++" + Thread.currentThread().getName());
            }
        });
        Response response = Response.ok(POST_NOTIFICATION_RESPONSE, MediaType.TEXT_PLAIN).build();
        aresponse.resume(response);
    }

    /* Method handling HTTP POST requests. That send pnr file to producer.
     *
     *
     * @param pnrXMLData
     * @param aresponse
     */
    @POST
    @Path(SERVICE_PNR_FILE_SERVICE)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void processPNRFileData(@Suspended final AsyncResponse aresponse, final String pnrXMLData) throws InterruptedException {
        aresponse.setTimeoutHandler(new TimeoutHandler() {
            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
            }
        });
        aresponse.setTimeout(20, TimeUnit.SECONDS);
        QUEUE_EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //sending data to pnr producer
                    //PNRProducer.sendDataToPNRProducer(pnrXMLData);                   
                } catch (Exception ex) {
                    aresponse.resume(ex);
                    return;
                }
                System.out.println("theread++++++" + Thread.currentThread().getName());
            }
        });
        Response response = Response.ok(POST_NOTIFICATION_RESPONSE, MediaType.TEXT_PLAIN).build();
        aresponse.resume(response);

    }

}
