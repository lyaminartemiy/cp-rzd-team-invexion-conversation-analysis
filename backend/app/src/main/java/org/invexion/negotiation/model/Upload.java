package org.invexion.negotiation.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Table(name = "uploads")
@Data
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parentFileName;

    @Enumerated(EnumType.STRING)
    private UploadType uploadType;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime uploadDateTime;

    private Boolean isAnalyzed;
}
