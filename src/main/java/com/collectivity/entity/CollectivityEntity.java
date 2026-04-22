package com.collectivity.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "collectivity")
public class CollectivityEntity {

    @Id
    private String id;
    
    private String location;
    private boolean federationApproval;

    @ElementCollection
    @CollectionTable(name = "collectivity_members", joinColumns = @JoinColumn(name = "collectivity_id"))
    @Column(name = "member_id")
    private List<String> memberIds;

    // Structure (Stored as IDs to keep it simple for your L2 level)
    private String presidentId;
    private String vicePresidentId;
    private String treasurerId;
    private String secretaryId;

    public CollectivityEntity() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public boolean isFederationApproval() { return federationApproval; }
    public void setFederationApproval(boolean federationApproval) { this.federationApproval = federationApproval; }
    public List<String> getMemberIds() { return memberIds; }
    public void setMemberIds(List<String> memberIds) { this.memberIds = memberIds; }
    public String getPresidentId() { return presidentId; }
    public void setPresidentId(String presidentId) { this.presidentId = presidentId; }
    public String getVicePresidentId() { return vicePresidentId; }
    public void setVicePresidentId(String vicePresidentId) { this.vicePresidentId = vicePresidentId; }
    public String getTreasurerId() { return treasurerId; }
    public void setTreasurerId(String treasurerId) { this.treasurerId = treasurerId; }
    public String getSecretaryId() { return secretaryId; }
    public void setSecretaryId(String secretaryId) { this.secretaryId = secretaryId; }
}
