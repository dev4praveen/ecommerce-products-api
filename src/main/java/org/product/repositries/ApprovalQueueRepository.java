package org.product.repositries;

import org.product.entities.ApprovalQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue,Long> {

}
