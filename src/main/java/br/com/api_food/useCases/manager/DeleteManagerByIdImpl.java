package br.com.api_food.useCases.manager;

import br.com.api_food.domain.useCases.manager.DeleteManagerById;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteManagerByIdImpl implements DeleteManagerById {
    @Override
    public void execute(UUID id) {

    }
}
