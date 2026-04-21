package com.collectivity.entity;

import java.util.List;

public class CollectivityEntity {

    private String id;
    private String location;
    private boolean federationApproval;
    private List<String> memberIds;
    private String presidentId;
    private String vicePresidentId;
    private String treasurerId;
    private String secretaryId;

    public CollectivityEntity() {}

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
