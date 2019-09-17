package com.bfwg.model;

import com.bfwg.dto.GroupTypeDto;

import javax.persistence.*;

@Entity
@Table(name = "group_type")
public class GroupType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    public GroupType(GroupTypeDto groupTypeDto) {
        this.id = groupTypeDto.getId();
        this.name = groupTypeDto.getName();
    }

    public GroupType() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
