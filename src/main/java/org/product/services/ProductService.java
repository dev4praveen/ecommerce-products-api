package org.product.services;

import org.product.dto.ProductDTO;
import org.product.entities.ApprovalQueue;
import org.product.entities.Product;
import org.product.repositries.ApprovalQueueRepository;
import org.product.repositries.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ApprovalQueueRepository approvalQueueRepository;

    public List<ProductDTO> getActiveProducts() {
        List<Product> products =  productRepository.findByStatusTrueOrderByPostedDateDesc();
        List<ProductDTO> productDTOS = products.stream().map(this::convertToDto).collect(Collectors.toList());
        return productDTOS;
    }

    public List<ProductDTO> searchProducts(Specification<Product> spec) {
        List<Product> entityList = productRepository.findAll(spec);
        List<ProductDTO> outPutDTO =entityList.stream().map(x ->convertToDto(x)).collect(Collectors.toList());
        return outPutDTO;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO, null);
        if (product.getPrice() > 10000) {
            product.setStatus(false);
        } else if (product.getPrice() > 5000) {
            product.setStatus(false);
        } else {
            product.setStatus(true);
        }
        product = productRepository.save(product);
        return convertToDto(product);
    }

    public ProductDTO updateProduct( Long productId, ProductDTO productDTO) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));
        Product updatedProduct = convertToEntity(productDTO, existingProduct);
        if (updatedProduct.getPrice() > existingProduct.getPrice() * 1.5) {
            updatedProduct.setStatus(false); // Push to approval queue
        } else {
            updatedProduct.setStatus(true);
        }
        updatedProduct.setId(productId);
        Product updatedClient = productRepository.save(updatedProduct);
        return convertToDto(updatedClient);
    }

    public void deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        existingProduct.setStatus(false);
        productRepository.save(existingProduct);
    }

    public List<ProductDTO> getProductsInApprovalQueue() {
        List<Product> products = approvalQueueRepository.findAll().stream().map(ApprovalQueue::getProduct).collect(Collectors.toList());
        List<ProductDTO> productDTOS = products.stream().map(this::convertToDto).collect(Collectors.toList());
        return productDTOS;
    }

    public void approveProduct(Long approvalId) {
        ApprovalQueue approval = approvalQueueRepository.findById(approvalId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found"));
        Product product = approval.getProduct();
        product.setStatus(true);
        productRepository.save(product);
        approvalQueueRepository.delete(approval);
    }

    public void rejectProduct(Long approvalId) {
        ApprovalQueue approval = approvalQueueRepository.findById(approvalId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found"));
        approvalQueueRepository.delete(approval);
    }

    private ProductDTO convertToDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setStatus(product.isStatus());
        productDTO.setPrice(product.getPrice());
        productDTO.setPostedDate(product.getPostedDate());
        return productDTO;
    }

    private Product convertToEntity(ProductDTO productDTO, Product product) {

        if(product == null){
            product = new Product();
        }
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setStatus(productDTO.isStatus());
        product.setPrice(productDTO.getPrice());
        product.setPostedDate(productDTO.getPostedDate());
        return product;
    }

}