package org.product.resources;

import lombok.extern.slf4j.Slf4j;
import org.product.dto.ProductDTO;
import org.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listActiveProducts() {
        List<ProductDTO> activeProducts = productService.getActiveProducts();
        return ResponseEntity.ok(activeProducts);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<Product>> searchProducts(
//            @RequestParam(name = "productName", required = false) String productName,
//            @RequestParam(name = "minPrice", required = false) Double minPrice,
//            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
//            @RequestParam(name = "minPostedDate", required = false) LocalDateTime minPostedDate,
//            @RequestParam(name = "maxPostedDate", required = false) LocalDateTime maxPostedDate
//    ) {
//        List<Product> searchResult = productService.searchProducts(
//                productName, minPrice, maxPrice, minPostedDate, maxPostedDate
//        );
//        return ResponseEntity.ok(searchResult);
//    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId, @RequestBody ProductDTO productDTO
    ) {
        ProductDTO product = productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/approval-queue")
    public ResponseEntity<List<ProductDTO>> getProductsInApprovalQueue() {
        List<ProductDTO> productsInQueue = productService.getProductsInApprovalQueue();
        return ResponseEntity.ok(productsInQueue);
    }

    @PutMapping("/approval-queue/{approvalId}/approve")
    public ResponseEntity<Void> approveProduct(@PathVariable Long approvalId) {
        productService.approveProduct(approvalId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/approval-queue/{approvalId}/reject")
    public ResponseEntity<Void> rejectProduct(@PathVariable Long approvalId) {
        productService.rejectProduct(approvalId);
        return ResponseEntity.noContent().build();
    }
}
