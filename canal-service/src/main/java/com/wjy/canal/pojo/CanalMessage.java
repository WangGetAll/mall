package com.wjy.canal.pojo;


import lombok.Data;

import java.util.List;



// {"data":[{"id":"4","passwd":"$2a$10$vENCHKoGTwFDTczjyfV2keZRB5tqhunxuHvEsbVdzhCyC0QTgs0DW","user_name":"hebwiwangxz","user_role":"USER_ROLE","user_email":null,"user_idcard":null,"user_phone":"13518638013","user_province":null,"vip_epoch":"0","vip_buy_date":null,"vip_end_date":null,"vip_status":"0","user_real_name":null}],"database":"oauth2","es":1695095151000,"id":1,"isDdl":false,"mysqlType":{"id":"int(11)","passwd":"varchar(265)","user_name":"varchar(256)","user_role":"varchar(255)","user_email":"varchar(255)","user_idcard":"varchar(255)","user_phone":"varchar(15)","user_province":"varchar(255)","vip_epoch":"int(11)","vip_buy_date":"datetime","vip_end_date":"datetime","vip_status":"int(4)","user_real_name":"varchar(255)"},"old":[{"user_phone":"13518638011"}],"pkNames":["id"],"sql":"","sqlType":{"id":4,"passwd":12,"user_name":12,"user_role":12,"user_email":12,"user_idcard":12,"user_phone":12,"user_province":12,"vip_epoch":4,"vip_buy_date":93,"vip_end_date":93,"vip_status":4,"user_real_name":12},"table":"user","ts":1695104014078,"type":"UPDATE"}
@Data
public class CanalMessage<T> {
    private List<User> data;
    private String database;
    private String table;
    private String type;
    private String sql;
    private List<T> old;

    private Long es;
    private Integer id;
    private Boolean isDdl;
    private List<String> pkNames;
    private Long ts;

}
