package com.collectivity.entity;

import jakarta.persistence.*; 
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "member")
public class MemberEntity {

    @Id
    private String id;
    
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;
    private String profession;
    private int phoneNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberOccupation occupation;

    private String collectivityIdentifier;

    @ElementCollection 
    private List<String> refereeIds;

    
    private LocalDate joinDate;

    public MemberEntity() {}


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public int getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public MemberOccupation getOccupation() { return occupation; }
    public void setOccupation(MemberOccupation occupation) { this.occupation = occupation; }
    public String getCollectivityIdentifier() { return collectivityIdentifier; }
    public void setCollectivityIdentifier(String collectivityIdentifier) { this.collectivityIdentifier = collectivityIdentifier; }
    public List<String> getRefereeIds() { return refereeIds; }
    public void setRefereeIds(List<String> refereeIds) { this.refereeIds = refereeIds; }
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
}
