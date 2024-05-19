package org.invexion.negotiation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "violation_intervals")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ViolationInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "negotiation_analysis_id")
    @JsonIgnore
    private NegotiationAnalysis negotiationAnalysis;

    private String violatedText;

    private Long beginIndex;

    private Long endIndex;

    private String violatedRegulation;
}
