package com.deliveryservice.domain;

import com.commonlibs.api.kafka.DeliveryAssignedEvent;
import com.commonlibs.api.kafka.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryProcessor {

    private final DeliveryJpaRepository deliveryJpaRepository;
    private final KafkaTemplate<Long, DeliveryAssignedEvent> kafkaTemplate;

    @Value("${delivery-assigned-topic}")
    private String deliveryAssignedTopic;

    public void processOrderPaid(final OrderPaidEvent event) {
        Long orderId = event.orderId();
        var found = deliveryJpaRepository.findByOrderId(orderId);
        if (found.isPresent()) {
            log.info("Found order delivery was already assigned: delivery={}", found.get());
            return;
        }
        DeliveryEntity assignDelivery = assignDelivery(orderId);
        sendDeliveryAssignedEvent(assignDelivery);
    }

    private void sendDeliveryAssignedEvent(final DeliveryEntity assignDelivery) {
        kafkaTemplate.send(
                deliveryAssignedTopic,
                assignDelivery.getOrderId(),
                DeliveryAssignedEvent.builder()
                        .orderId(assignDelivery.getOrderId())
                        .courierName(assignDelivery.getCourierName())
                        .etaMinutes(assignDelivery.getEtaMinutes()).build()
        ).thenAccept(result -> {
            log.info("Delivery assigned event sent: deliveryId:{}", assignDelivery.getId());
        });
    }

    private DeliveryEntity assignDelivery(final Long orderId) {
        DeliveryEntity deliveryEntity = new DeliveryEntity();

        deliveryEntity.setOrderId(orderId);
        deliveryEntity.setCourierName("courier-" + ThreadLocalRandom.current().nextInt(100));
        deliveryEntity.setEtaMinutes(ThreadLocalRandom.current().nextInt(10, 45));

        log.info("Saved order delivery was assigned: delicery={}", deliveryEntity);
        return deliveryJpaRepository.save(deliveryEntity);
    }
}
