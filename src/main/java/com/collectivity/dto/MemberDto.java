package com.collectivity.dto;

import java.util.List;

public class MemberDto extends MemberInformationDto {

    private String id;
    private List<MemberDto> referees;

    public MemberDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public List<MemberDto> getReferees() { return referees; }
    public void setReferees(List<MemberDto> referees) { this.referees = referees; }
}
