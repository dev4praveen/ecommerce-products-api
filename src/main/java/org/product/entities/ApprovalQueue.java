package org.product.entities;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ApprovalQueue {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @OneToOne
        private Product product;
        private LocalDateTime requestDate;
    }

