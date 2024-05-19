package org.invexion.negotiation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@Table(name = "negotiation_analysis")
@AllArgsConstructor
public class NegotiationAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "negotiation_id")
    private Negotiation negotiation;

    @Column(columnDefinition="TEXT")
    private String negotiationText;

    private Boolean isViolated;

    @OneToMany(mappedBy = "negotiationAnalysis")
    private List<ViolationInterval> violationIntervals;
}
