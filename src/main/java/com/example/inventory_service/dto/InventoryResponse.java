package com.example.inventory_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryResponse {

    private Integer availableQuantity;
    private Integer requestedQuantity;
    private Boolean inStock;
}
