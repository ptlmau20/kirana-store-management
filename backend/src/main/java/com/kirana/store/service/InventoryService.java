package com.kirana.store.service;

import com.kirana.store.dto.ProductDto;
import com.kirana.store.entity.Product;
import com.kirana.store.entity.StockMovement;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.exception.InsufficientStockException;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.ProductRepository;
import com.kirana.store.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public InventoryService(ProductRepository productRepository, StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional(readOnly = true)
    public List<StockMovement> getRecentStockMovements() {
        return stockMovementRepository.findTop100ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<StockMovement> getStockMovementsByProduct(Long productId) {
        return stockMovementRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Transactional
    public StockMovement recordMovement(Product product,
                                         StockMovement.MovementType type,
                                         int quantityChange,
                                         String referenceId,
                                         String notes,
                                         String performerName) {

        int previousStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        int newStock = previousStock + quantityChange;

        if (newStock < 0) {
            throw new InsufficientStockException("Insufficient stock for product '" + product.getName() + "'. Requested change: " + quantityChange + ", Current stock: " + previousStock);
        }

        product.setStockQuantity(newStock);
        productRepository.save(product);

        StockMovement movement = StockMovement.builder()
                .product(product)
                .movementType(type)
                .quantityChange(quantityChange)
                .previousStock(previousStock)
                .newStock(newStock)
                .referenceId(referenceId)
                .notes(notes)
                .createdByName(performerName)
                .build();

        return stockMovementRepository.save(movement);
    }

    @Transactional
    public StockMovement adjustStock(ProductDto.StockAdjustmentRequest request, String performerName) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        if (request.getQuantityChange() == 0) {
            throw new BadRequestException("Stock adjustment quantity change cannot be zero");
        }

        return recordMovement(
                product,
                StockMovement.MovementType.ADJUSTMENT,
                request.getQuantityChange(),
                "ADJ-" + System.currentTimeMillis(),
                request.getNotes(),
                performerName
        );
    }
}
