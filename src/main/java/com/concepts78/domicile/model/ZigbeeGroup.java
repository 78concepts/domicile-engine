package com.concepts78.domicile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ZigbeeGroup {

    private Integer id;

    @JsonProperty("friendly_name")
    private String friendlyName;

    private List<ZigbeeGroupMember> members;
    private List<ZigbeeGroupScene> scenes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public List<ZigbeeGroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<ZigbeeGroupMember> members) {
        this.members = members;
    }

    public List<ZigbeeGroupScene> getScenes() {
        return scenes;
    }

    public void setScenes(List<ZigbeeGroupScene> scenes) {
        this.scenes = scenes;
    }
}
