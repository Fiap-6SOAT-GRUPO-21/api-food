package br.com.api_food.useCases.order;

import br.com.api_food.domain.entity.order.OrderDomain;
import br.com.api_food.domain.entity.order.enums.StatusOrder;
import br.com.api_food.domain.persistence.order.OrderPersistence;
import br.com.api_food.domain.useCases.order.FindOrderById;
import br.com.api_food.domain.useCases.order.UpdateOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOrderStatusImpl implements UpdateOrderStatus {

    private final OrderPersistence orderPersistence;
    private final FindOrderById findOrderById;

    @Override
    public void execute(UUID id, StatusOrder status) {
        OrderDomain orderDomain = findOrderById.execute(id);
        orderDomain.setStatus(status);

        orderPersistence.save(orderDomain);
    }
}
