    package com.example.fintech_api.model;

    import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Column;
    import jakarta.persistence.Table;
    import lombok.Data;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "status_codes", schema = "gle_prod_config")  // Explicit schema and table name
    @Data
    public class StatusCode {
        @Id
        @Column(name = "status_code_id")
        private String statusCodeId;

        @Column(name = "status_code")
        private String statusCode;

        private String description;

        @Column(name = "status_message")
        private String statusMessage;

        @Column(name = "status_type")
        private String statusType;

        private String module;

        @Column(name = "created_on")
        private LocalDateTime createdOn;

        @Column(name = "created_by")
        private String createdBy;

        @Column(name = "tmp_msg")
        private String tmpMsg;

        @Column(name = "ar_description")
        private String arDescription;

        @Column(name = "ar_status_message")
        private String arStatusMessage;

        private String hide;

        @Column(name = "exposed_status_code")
        private String exposedStatusCode;
    }