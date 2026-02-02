package com.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.demo.dto.PaymentVerifyDTO;
import com.demo.model.Order;
import com.demo.service.OrderService;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private OrderService orderService;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;
    
    
    @PostMapping("/razorpay-test")
    public String razorpayTest() throws Exception {

        JSONObject options = new JSONObject();
        options.put("amount", 100);
        options.put("currency", "INR");

        razorpayClient.orders.create(options);
        return "RAZORPAY OK";
    }
    
    @PostMapping("/debug-keys")
    public String debugKeys(
        @Value("${razorpay.key.id}") String id,
        @Value("${razorpay.key.secret}") String secret
    ) {
        return "ID=[" + id + "] SECRET=[" + secret + "]";
    }



    /* ======================
       CREATE RAZORPAY ORDER
       ====================== */
    @PostMapping("/create")
    public Map<String, Object> createPayment(@RequestParam Integer userId) throws Exception {

        int amount = orderService.calculateCartTotal(userId);

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100);
        options.put("currency", "INR");
        options.put("receipt", "rcpt_" + System.currentTimeMillis());

        com.razorpay.Order razorpayOrder =
                razorpayClient.orders.create(options);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", razorpayOrder.get("id"));
        response.put("amount", razorpayOrder.get("amount"));
        response.put("currency", "INR");

        return response;
    }

    /* ======================
       VERIFY PAYMENT
       ====================== */
    @PostMapping("/verify")
    public Order verifyPayment(@RequestBody PaymentVerifyDTO dto) throws Exception {

        String payload =
                dto.getRazorpayOrderId() + "|" + dto.getRazorpayPaymentId();

        boolean isValid = Utils.verifySignature(
                payload,
                dto.getRazorpaySignature(),
                razorpaySecret
        );

        if (!isValid) {
            throw new RuntimeException("Payment verification failed");
        }

        return orderService.placeOrder(
                dto.getUserId(),
                dto.getPaymentMode()
        );
    }
}
