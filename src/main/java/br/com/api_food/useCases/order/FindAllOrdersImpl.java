package br.com.api_food.useCases.order;

import br.com.api_food.domain.entity.order.OrderDomain;
import br.com.api_food.domain.persistence.order.OrderPersistence;
import br.com.api_food.domain.useCases.order.FindAllOrders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllOrdersImpl implements FindAllOrders {
    
    private final OrderPersistence orderPersistence;
    @Override
    public List<OrderDomain> execute() {
        return orderPersistence.findAll();
    }
}
