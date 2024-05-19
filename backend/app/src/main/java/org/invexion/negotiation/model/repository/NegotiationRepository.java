package org.invexion.negotiation.model.repository;

import org.invexion.negotiation.model.Negotiation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
    @Query("select count(n) from Negotiation n join NegotiationAnalysis na on n.id = na.id where na.isViolated = true")
    long countViolated();

    @Query("select count(n) from Negotiation n join NegotiationAnalysis na on n.id = na.id where na.isViolated = false")
    long countNonViolated();

    @Query("select n from Negotiation n where n.fileStorageId=?1")
    Negotiation findByS3UUID(String s3UUID);
}