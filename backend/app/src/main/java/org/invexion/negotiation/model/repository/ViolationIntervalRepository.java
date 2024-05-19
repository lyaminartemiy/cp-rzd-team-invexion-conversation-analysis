package org.invexion.negotiation.model.repository;

import org.invexion.negotiation.model.NegotiationAnalysis;
import org.invexion.negotiation.model.ViolationInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViolationIntervalRepository extends JpaRepository<ViolationInterval, Long> {
  @Query("select v from ViolationInterval v where v.negotiationAnalysis = ?1")
  List<ViolationInterval> findByNegotiationAnalysis(NegotiationAnalysis negotiationAnalysis);

  @Query("select v from ViolationInterval v join NegotiationAnalysis na on v.negotiationAnalysis.id=na.id where na.id=?1")
  List<ViolationInterval> findAllByAnalysisId(Long analysisId);
}