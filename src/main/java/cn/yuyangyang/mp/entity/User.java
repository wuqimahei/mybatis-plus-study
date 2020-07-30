package cn.yuyangyang.mp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
// 使用这个注解指定对应的数据库的表 防止类名和表名对应不上
//@TableName("mp_user")
public class User {

    /*
    @TableId // 使用这个注解表名这个字段对应的就是主键 如实体类是 userId 数据库是 user_id，可以使用本注解
    private Long id;
    @TableField("name") // 实体类字段与数据库字段不一样使用本注解
    private String realName;
    private Integer age;
    private String email;
    private Long managerId;
    private LocalDateTime createTime;
    */
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Long managerId;
    private LocalDateTime createTime;

    // 排除非表字段的三种方式
    /**
     * 1. 实体类字段添加 transient 关键字  好像就不能序列化了
     * 2. 实体类字段添加 static 关键字，静态的好像lombok不能生成get set方法 手动生成一下吧
     * 这样的话就是全类唯一一份，直接使用类名.字段直接赋值了  也不好
     * 3. 字段上添加 @TableField(exist = false) 说明这个字段数据库不存在
     */
    @TableField(exist = false)
    private static String remark;

}
