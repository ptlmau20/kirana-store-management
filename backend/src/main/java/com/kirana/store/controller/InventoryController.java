package com.kirana.store.controller;

import com.kirana.store.dto.ProductDto;
import com.kirana.store.entity.StockMovement;
import com.kirana.store.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/movements")
    public ResponseEntity<List<StockMovement>> getRecentStockMovements() {
        return ResponseEntity.ok(inventoryService.getRecentStockMovements());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovement>> getStockMovementsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getStockMovementsByProduct(productId));
    }

    @PostMapping("/adjust")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockMovement> adjustStock(
            @Valid @RequestBody ProductDto.StockAdjustmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        StockMovement movement = inventoryService.adjustStock(request, userDetails.getUsername());
        return ResponseEntity.ok(movement);
    }
}
