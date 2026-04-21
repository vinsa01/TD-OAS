package com.collectivity.dto;

import java.util.List;

public class CreateCollectivityDto {

    private String location;
    private List<String> members;
    private boolean federationApproval;
    private CreateCollectivityStructureDto structure;

    public CreateCollectivityDto() {}

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }
    public boolean isFederationApproval() { return federationApproval; }
    public void setFederationApproval(boolean federationApproval) { this.federationApproval = federationApproval; }
    public CreateCollectivityStructureDto getStructure() { return structure; }
    public void setStructure(CreateCollectivityStructureDto structure) { this.structure = structure; }
}
