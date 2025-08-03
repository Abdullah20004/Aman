package com.example.fintech_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "users", schema = "gle_prod_config")  // Explicit schema and table name
@Data
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "payment_network_code")
    private String paymentNetworkCode;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "login_id_1")
    private String loginId1;

    @Column(name = "login_id_2")
    private String loginId2;

    @Column(name = "password_1")
    private String password1;

    @Column(name = "password_2")
    private String password2;

    @Column(name = "user_category_code")
    private String userCategoryCode;

    @Column(name = "user_segment_code")
    private String userSegmentCode;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "external_id")
    private String externalId;

    private String msisdn;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "outlet_name")
    private String outletName;

    @Column(name = "outlet_type_code")
    private String outletTypeCode;

    @Column(name = "location_code")
    private String locationCode;

    @Column(name = "access_profile_code")
    private String accessProfileCode;

    @Column(name = "language_code")
    private String languageCode;

    private String status;

    @Column(name = "previous_status")
    private String previousStatus;

    @Column(name = "last_login_on")
    private LocalDate lastLoginOn;

    @Column(name = "invalid_password_count")
    private Double invalidPasswordCount;

    @Column(name = "reset_password_1")
    private String resetPassword1;

    @Column(name = "reset_password_2")
    private String resetPassword2;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_on")
    private LocalDate modifiedOn;

    @Column(name = "modified_by")
    private String modifiedBy;

    private String area;

    @Column(name = "terminal_type")
    private String terminalType;

    @Column(name = "data_sim_1")
    private String dataSim1;

    @Column(name = "data_sim_2")
    private String dataSim2;

    @Column(name = "gender_code")
    private String genderCode;

    @Column(name = "birth_date")
    private String birthDate;

    private String job;

    private String nickname;

    @Column(name = "business_field_type_code")
    private String businessFieldTypeCode;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_license_number")
    private String businessLicenseNumber;

    @Column(name = "tax_number")
    private String taxNumber;

    @Column(name = "landline_number")
    private String landlineNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "business_address")
    private String businessAddress;

    private String landmark;

    @Column(name = "registeration_documents_id")
    private String registrationDocumentsId;

    @Column(name = "terminal_serial")
    private String terminalSerial;

    private String comment;

    @Column(name = "commercial_register")
    private String commercialRegister;

    private String msisdn2;

    @Column(name = "route_code")
    private String routeCode;

    @Column(name = "temporary_active_flag")
    private String temporaryActiveFlag;

    @Column(name = "competitor_flag")
    private String competitorFlag;

    @Column(name = "last_modification_comment")
    private String lastModificationComment;

    @Column(name = "street_type_code")
    private String streetTypeCode;

    private String longitude;

    private String latitude;

    @Column(name = "extra_info_1")
    private String extraInfo1;

    @Column(name = "extra_info_2")
    private String extraInfo2;

    @Column(name = "extra_info_3")
    private String extraInfo3;

    @Column(name = "associated_hosted_stock_code")
    private String associatedHostedStockCode;
}