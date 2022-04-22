package com.microservicelab1.ordersvc;

import com.microservicelab1.ordersvc.domain.VerifyOrderResult;
import com.microservicelab1.ordersvc.rpc.HttpClient;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@Slf4j
@RestController
public class OrderSvcApp {
    public static final String ORDER_CREATE = "order-create";
    private final AtomicInteger concSessions = new AtomicInteger();

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private HttpClient httpClient;

    @Value("${restaurantsvc.host}")
    private String restaurantSvcHost;

    @Value("${restaurantsvc.port}")
    private String restaurantSvcPort;

    @Value("${restaurantsvc.orderverify.uri}")
    private String orderVerifyUri;

    @Value("${kitchensvc.host}")
    private String kitchenSvcHost;

    @Value("${kitchensvc.port}")
    private String kitchenSvcPort;

    @Value("${kitchensvc.createticket.uri}")
    private String createTicketUri;

    @Value("${order-val:ordersvc}")
    private String configVal;


    @PostConstruct
    public void init() {
        meterRegistry.gauge("conc_sessions", concSessions, AtomicInteger::doubleValue);
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderSvcApp.class, args);
    }

    @GetMapping(path = "/order/create/{consumerId}")
    @Timed(percentiles = .5)
    public ResponseEntity<String> createOrder(final @PathVariable(name = "consumerId") String consumerId) {
        concSessions.incrementAndGet();
        log.info("Received create order request.");
        String res = null;
        try {
            res = String.format("%s:%s:%s", configVal,
                    createTicket(consumerId),
                    orderVerify("aabb"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        concSessions.decrementAndGet();
        return ResponseEntity.ok(res);
    }

    public String fallback(String consumerId, RuntimeException e) {
        log.info("Order create req fallbacked here due to dep services down.");
        return "create order had been fallbacked";
    }

    private String createTicket(final String consumerId) throws IOException {
        String url = String.format("http://%s:%s/%s", kitchenSvcHost, kitchenSvcPort, createTicketUri);
        return httpClient.doPostAndGetString(url);
    }

    private String orderVerify(final String orderId) throws IOException {
        String url = String.format("http://%s:%s/%s", restaurantSvcHost, restaurantSvcPort, orderVerifyUri);
        return httpClient.doGetWithType(url, VerifyOrderResult.class).getReason();
    }
}
