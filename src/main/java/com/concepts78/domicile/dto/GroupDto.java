package com.concepts78.domicile.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GroupDto {

    private UUID uuid;
    private Integer zigbeeId;
    private Date dateCreated;
    private Date dateModified;
    private String friendlyName;
    private List<GroupMemberDto> members;
    private List<GroupSceneDto> scenes;
    private boolean active;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getZigbeeId() {
        return zigbeeId;
    }

    public void setZigbeeId(Integer zigbeeId) {
        this.zigbeeId = zigbeeId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public List<GroupMemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberDto> members) {
        this.members = members;
    }

    public List<GroupSceneDto> getScenes() {
        return scenes;
    }

    public void setScenes(List<GroupSceneDto> scenes) {
        this.scenes = scenes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
