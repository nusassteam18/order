package com.ss.order.service;

import com.ss.order.constant.Constants;
import com.ss.order.helperDTO.OrderItemDTO;
import com.ss.order.helperDTO.ReceiptDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSend;

    public EmailService(JavaMailSender mailSend) {
        this.mailSend = mailSend;
    }

    public void sendOrderReceipt(ReceiptDTO receiptDTO, int emailPurpose) throws MessagingException {

        MimeMessage message = mailSend.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(receiptDTO.getCustomerTruncatedDTO().getEmail());

        String subject;
        String header;
        if (emailPurpose == Constants.ORDER_CONFIRMED) {
            subject = "Order Confirmed";
            header = "Order Confirmed";
        } else {
            subject = "Order " + receiptDTO.getStatus();
            header = "Order " + receiptDTO.getStatus();
        }

        helper.setSubject(subject);

        StringBuilder orderItemText = new StringBuilder();
        for (OrderItemDTO orderItemDTO : receiptDTO.getOrderItemDTOs()) {
            orderItemText
                    .append("<p>")
                    .append(orderItemDTO.getQuantity())
                    .append("x&nbsp;&nbsp;&nbsp;&nbsp;")
                    .append(orderItemDTO.getDish())
                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#36;")
                    .append(orderItemDTO.getSubtotal())
                    .append("</p>");
        }

        String body =   "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +
                        "  body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                        "  .header { color: #4CAF50; text-align: center; }" +
                        "  .content { background: #f9f9f9; padding: 20px; border-radius: 5px; }" +
                        "  .button { display: inline-block; padding: 10px 20px; background: #4CAF50; " +
                        "           color: white; text-decoration: none; border-radius: 4px; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='header'><h1>" + header + "</h1></div>" +
                        "<div class='content'>" +
                        "<p><b>" + receiptDTO.getRestaurantName() + "</b></p>" +
                        "<p>Your Order ID is <b>" + receiptDTO.getOrderId() + "</b>.</p>" +
                        "<p>Status: <b>" + receiptDTO.getStatus() + "</b></p>" +
                        "<p><b><u>Order Items:</u></b></p>" +
                        orderItemText +
                        "<p>Total: &#36;" + receiptDTO.getTotal() + "</p>" +
                        "<br/><p>Updated at: " + receiptDTO.getUpdatedAt() + "</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

        helper.setText(body, true);

        mailSend.send(message);
    }
}