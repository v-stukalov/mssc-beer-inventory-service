package guru.springframework.beer.inventory.service.services;

import guru.springframework.beer.inventory.service.config.JmsConfig;
import guru.springframework.beer.inventory.service.domain.BeerInventory;
import guru.springframework.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.springframework.common.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewInventoryListener {
    private final BeerInventoryRepository beerInventoryRepository;

    @Transactional
    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent newInventoryEvent) {
        log.debug("got inventory: " + newInventoryEvent.toString());

        beerInventoryRepository.save(BeerInventory.builder()
                .beerId(newInventoryEvent.getBeerDto().getId())
                .upc(newInventoryEvent.getBeerDto().getUpc())
                .quantityOnHand(newInventoryEvent.getBeerDto().getQuantityOnHand())
                .build());
    }
}
