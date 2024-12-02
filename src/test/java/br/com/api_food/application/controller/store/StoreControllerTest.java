package br.com.api_food.application.controller.store;

import br.com.api_food.application.dtos.store.StoreDTO;
import br.com.api_food.application.dtos.store.StoreInputDTO;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.useCases.store.CreateNewStore;
import br.com.api_food.domain.useCases.store.DeleteStoreById;
import br.com.api_food.domain.useCases.store.FindAllStores;
import br.com.api_food.domain.useCases.store.FindStoreById;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

    @InjectMocks
    private StoreController storeController;

    @Mock
    private CreateNewStore createNewStore;

    @Mock
    private FindAllStores findAllStores;

    @Mock
    private FindStoreById findStoreById;

    @Mock
    private DeleteStoreById deleteStoreById;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void save_ShouldReturnCreatedStore() {
        StoreInputDTO inputDTO = new StoreInputDTO();
        StoreDomain domain = new StoreDomain();
        StoreDomain savedDomain = new StoreDomain();
        StoreDTO expectedDTO = new StoreDTO();

        when(modelMapper.map(inputDTO, StoreDomain.class)).thenReturn(domain);
        when(createNewStore.execute(domain)).thenReturn(savedDomain);
        when(modelMapper.map(savedDomain, StoreDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<StoreDTO> response = storeController.save(inputDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        verify(modelMapper).map(inputDTO, StoreDomain.class);
        verify(createNewStore).execute(domain);
        verify(modelMapper).map(savedDomain, StoreDTO.class);
    }

    @Test
    void getById_ShouldReturnStore() {
        UUID id = UUID.randomUUID();
        StoreDomain domain = new StoreDomain();
        StoreDTO expectedDTO = new StoreDTO();

        when(findStoreById.execute(id)).thenReturn(domain);
        when(modelMapper.map(domain, StoreDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<StoreDTO> response = storeController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        verify(findStoreById).execute(id);
        verify(modelMapper).map(domain, StoreDTO.class);
    }

    @Test
    void getAll_ShouldReturnStores() {
        List<StoreDomain> domains = List.of(new StoreDomain(), new StoreDomain());
        List<StoreDTO> expectedDTOs = List.of(new StoreDTO(), new StoreDTO());

        when(findAllStores.execute()).thenReturn(domains);
        when(modelMapper.map(domains.get(0), StoreDTO.class)).thenReturn(expectedDTOs.get(0));
        when(modelMapper.map(domains.get(1), StoreDTO.class)).thenReturn(expectedDTOs.get(1));

        ResponseEntity<List<StoreDTO>> response = storeController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTOs, response.getBody());

        verify(findAllStores).execute();
        verify(modelMapper, Mockito.times(2)).map(Mockito.any(StoreDomain.class), Mockito.eq(StoreDTO.class));
    }

    @Test
    void getAll_ShouldReturnNoContentWhenEmpty() {
        when(findAllStores.execute()).thenReturn(Collections.emptyList());

        ResponseEntity<List<StoreDTO>> response = storeController.getAll();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(findAllStores).execute();
    }

    @Test
    void delete_ShouldReturnNoContent() {
        UUID id = UUID.randomUUID();

        Mockito.doNothing().when(deleteStoreById).execute(id);

        ResponseEntity<Void> response = storeController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(deleteStoreById).execute(id);
    }
}