package com.gary.fraudflagger.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String performedBy; //username of the manager/admin who acted
    private String action; //"APPROVED" or "DISMISSED"
    private Long flagId; //Which FraudFlag was acted on, stored as long so logs can surivive even if flag gets deleted
    private LocalDateTime timestamp;

    private AuditLog(){}

    public AuditLog(String performedBy, String action, Long flagId) {
        this.performedBy = performedBy;
        this.action = action;
        this.flagId = flagId;
        this.timestamp = LocalDateTime.now();
    }

    //Getters
    public Long getId() { return id; }
    public String getPerformedBy() { return performedBy; }
    public String getAction() { return action; }
    public Long getFlagId() { return flagId; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
