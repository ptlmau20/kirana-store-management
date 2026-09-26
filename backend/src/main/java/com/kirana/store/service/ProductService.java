package com.kirana.store.service;

import com.kirana.store.dto.ProductDto;
import com.kirana.store.entity.Category;
import com.kirana.store.entity.Product;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.CategoryRepository;
import com.kirana.store.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDto.Response> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return productRepository.findAll().stream()
                    .filter(Product::getActive)
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
        return productRepository.searchProducts(query.trim()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductDto.Response> getProductsPaged(String query, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        String searchQuery = (query != null) ? query.trim() : "";
        return productRepository.searchProductsPaged(searchQuery, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ProductDto.Response getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDto.Response> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto.Response createProduct(ProductDto.Request request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName().trim())
                .hsnCode(request.getHsnCode() != null ? request.getHsnCode().trim() : null)
                .category(category)
                .costPrice(request.getCostPrice())
                .sellingPrice(request.getSellingPrice())
                .mrp(request.getMrp())
                .gstRate(request.getGstRate())
                .unit(request.getUnit().trim())
                .stockQuantity(request.getStockQuantity())
                .minStockAlert(request.getMinStockAlert())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Transactional
    public ProductDto.Response updateProduct(Long id, ProductDto.Request request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName().trim());
        product.setHsnCode(request.getHsnCode() != null ? request.getHsnCode().trim() : null);
        product.setCategory(category);
        product.setCostPrice(request.getCostPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setMrp(request.getMrp());
        product.setGstRate(request.getGstRate());
        product.setUnit(request.getUnit().trim());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinStockAlert(request.getMinStockAlert());
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDto.CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> ProductDto.CategoryDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .description(c.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto.CategoryDto createCategory(ProductDto.CategoryDto dto) {
        if (categoryRepository.existsByName(dto.getName().trim())) {
            throw new BadRequestException("Category '" + dto.getName() + "' already exists");
        }

        Category category = Category.builder()
                .name(dto.getName().trim())
                .description(dto.getDescription())
                .build();

        Category saved = categoryRepository.save(category);
        return ProductDto.CategoryDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .build();
    }

    public ProductDto.Response mapToResponse(Product product) {
        return ProductDto.Response.builder()
                .id(product.getId())
                .name(product.getName())
                .hsnCode(product.getHsnCode())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .costPrice(product.getCostPrice())
                .sellingPrice(product.getSellingPrice())
                .mrp(product.getMrp())
                .gstRate(product.getGstRate())
                .unit(product.getUnit())
                .stockQuantity(product.getStockQuantity())
                .minStockAlert(product.getMinStockAlert())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
