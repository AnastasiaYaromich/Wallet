package com.yaromich.wallet.domain.dto;


public class RoleDto {
    private Long id;
    private String name;

    public RoleDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoleDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
