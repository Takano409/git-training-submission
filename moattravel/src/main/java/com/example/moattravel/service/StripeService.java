package com.example.moattravel.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.moattravel.form.ReservationRegisterForm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

@Service
public class StripeService {
    @Value("${stripe.api-key}")
    private String stripeApiKey;
    
    private final ReservationService reservationService;
    
    public StripeService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }    
    
    // セッションを作成し、Stripeに必要な情報を返す
    public String createStripeSession(String houseName, ReservationRegisterForm reservationRegisterForm, HttpServletRequest httpServletRequest) {
        Stripe.apiKey = stripeApiKey;
        String requestUrl = new String(httpServletRequest.getRequestURL());
        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()   
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(houseName)
                                        .build())
                                .setUnitAmount((long)reservationRegisterForm.getAmount())
                                .setCurrency("jpy")                                
                                .build())
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved")
                .setCancelUrl(requestUrl.replace("/reservations/confirm", ""))
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("houseId", reservationRegisterForm.getHouseId().toString())
                        .putMetadata("userId", reservationRegisterForm.getUserId().toString())
                        .putMetadata("checkinDate", reservationRegisterForm.getCheckinDate())
                        .putMetadata("checkoutDate", reservationRegisterForm.getCheckoutDate())
                        .putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString())
                        .putMetadata("amount", reservationRegisterForm.getAmount().toString())
                        .build())
                .build();
        try {
            Session session = Session.create(params);
            return session.getId();
        } catch (StripeException e) {
            e.printStackTrace();
            return "";
        }
    } 
    
//     セッションから予約情報を取得し、ReservationServiceクラスを介してデータベースに登録する
//    public void processSessionCompleted(Event event) {
//        Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
//        optionalStripeObject.ifPresent(stripeObject -> {
//            Session session = (Session)stripeObject;
//            SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("payment_intent").build();
//
//            try {
//                session = Session.retrieve(session.getId(), params, null);
//                Map<String, String> paymentIntentObject = session.getPaymentIntentObject().getMetadata();
//                reservationService.create(paymentIntentObject);
//            } catch (StripeException e) {
//                e.printStackTrace();
//            }
//        
//        
//        System.out.println("=== Stripe Webhook Received ===");
//        System.out.println("Session ID: " + session.getId());
//        System.out.println("PaymentIntent: " + session.getPaymentIntent());
//
//        Map<String, String> metadata = session.getPaymentIntentObject().getMetadata();
//        System.out.println("Metadata: " + metadata);
//        });
//    }    
    
//    optionalStripeObject が empty になる	
//    Stripeの checkout.session.completed イベントに含まれる data.object が、SpringのStripeライブラリでは Session に自動的にデシリアライズできなかった。
//    
//    自動デシリアライズ失敗
//    特に 新しいAPIバージョン（2025-04-30.basil） では、形式が変わっていて getObject() が empty を返すことがある。
//    
//    エラーが null や empty なのに理由がわかりにくい
//    optionalStripeObject.isEmpty() しか表示していないため、なぜデシリアライズできないかが見えない。
//    
    

    
    
//    event.toJson() で イベント全体のJSON文字列を取得。
//    JsonNode を使って手動で "data.object.id" から sessionId を取り出している。
//    Session.retrieve(...) で再取得してから metadata を取り出している。
    public void processSessionCompleted(Event event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(event.toJson());
            String sessionId = rootNode.get("data").get("object").get("id").asText();
            System.out.println("[DEBUG] Session ID = " + sessionId);

            SessionRetrieveParams params = SessionRetrieveParams.builder()
                .addExpand("payment_intent")
                .build();

            Session session = Session.retrieve(sessionId, params, null);
            Map<String, String> metadata = session.getPaymentIntentObject().getMetadata();
            System.out.println("[DEBUG] Metadata = " + metadata);

            reservationService.create(metadata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
