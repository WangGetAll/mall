package com.wjy.oauth2.pojo;

import lombok.Data;


@Data
public class User {
    private Integer id;

    private String userName;

    private  String passwd;

    private String userRole;
}
