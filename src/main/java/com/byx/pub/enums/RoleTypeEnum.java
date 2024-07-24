package com.byx.pub.enums;


import lombok.Getter;

/**
 * 1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人
 * @Author Jump
 * @Date 2023/7/14 22:03
 */
@Getter
public enum RoleTypeEnum {
    FRONT_CUSTOM("消费者","0"),
    BYX_MANEGE("平台管理","1"),
    BYX_OPERATE("平台运营","2"),
    SJ_MANEGE("商家管理员","3"),
    SJ_MENTOR("商家导师","4"),
    SJ_TEACHER("商家老师","5"),
    SJ_ASSISTANT("商家推荐官","6"),
    SJ_OPERATE("商家助教","7"),
    SJ_PARTNER("合伙人","8");
    ;

    private  String name;
    private  String value;

    RoleTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
