package com.wjy.canal.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  User {

    private Integer id;

    private String password;

    private String userName;

    private String userEmail;

    private String userIDCard;

    private String userRole;

    private String userPhone;

    private String userProvince;

    private Integer vipStatus;

    private Integer vipEpoch;

    private Date vipBuyDate;

    private Date vipEndDate;
}