package com.example.project.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "audit_log", uniqueConstraints = @UniqueConstraint(columnNames = "log_id"))
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "action_timestamp")
    private Date actionTimestamp;

    @Column(name = "new_values")
    private String newValues;

    @Column(name = "log_id", unique = true)
    private Long logId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "action")
    private String action;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id", insertable = false, updatable = false)
    private com.example.project.entity.LibraryEvent libraryEvent;

    public AuditLog() {}

    public AuditLog(String tableName, Long recordId, String actionType, Date actionTimestamp, String newValues, Long logId, Long eventId, String action, String message) {
        this.tableName = tableName;
        this.recordId = recordId;
        this.actionType = actionType;
        this.actionTimestamp = actionTimestamp;
        this.newValues = newValues;
        this.logId = logId;
        this.eventId = eventId;
        this.action = action;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getActionTimestamp() {
        return actionTimestamp;
    }

    public void setActionTimestamp(Date actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }

    public String getNewValues() {
        return newValues;
    }

    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public com.example.project.entity.LibraryEvent getLibraryEvent() {
        return libraryEvent;
    }

    public void setLibraryEvent(com.example.project.entity.LibraryEvent libraryEvent) {
        this.libraryEvent = libraryEvent;
    }
}