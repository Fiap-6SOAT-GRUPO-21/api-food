package br.com.api_food.domain.entity.manager;

import br.com.api_food.domain.entity.DomainEntity;
import br.com.api_food.domain.entity.store.StoreDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDomain extends DomainEntity {

    private String name;
    private String email;
    private String cpf;
    private UUID idStore;
    private StoreDomain store;
}