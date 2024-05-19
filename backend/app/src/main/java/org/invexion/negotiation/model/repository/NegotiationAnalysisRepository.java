package org.invexion.negotiation.model.repository;

import org.invexion.negotiation.model.Negotiation;
import org.invexion.negotiation.model.NegotiationAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NegotiationAnalysisRepository extends JpaRepository<NegotiationAnalysis, Long> {

    @Query("select na from NegotiationAnalysis na where na.negotiation in (?1)")
    List<NegotiationAnalysis> findAllByNegotiations(List<Negotiation> negotiations);

    @Query("select n from NegotiationAnalysis n where n.negotiation = ?1")
    NegotiationAnalysis findByNegotiation(Negotiation negotiation);
}