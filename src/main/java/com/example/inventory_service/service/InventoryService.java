package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;

    @Transactional
    public Map<String, InventoryResponse> checkAndReduceStock(
            String skuCode,
            Integer requestedQty) {

        Inventory inventory = repository.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("SKU not found: " + skuCode));

        boolean inStock = inventory.getQuantity() >= requestedQty;

        InventoryResponse response = InventoryResponse.builder()
                .availableQuantity(inventory.getQuantity())
                .requestedQuantity(requestedQty)
                .inStock(inStock)
                .build();

        if (!inStock) {
            Map<String, InventoryResponse> result = new HashMap<>();
            result.put(skuCode, response);
            return result;
        }

        inventory.setQuantity(inventory.getQuantity() - requestedQty);
        repository.save(inventory);

        Map<String, InventoryResponse> result = new HashMap<>();
        result.put(skuCode, response);

        return result;
    }
}
