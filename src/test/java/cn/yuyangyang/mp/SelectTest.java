package cn.yuyangyang.mp;

import cn.yuyangyang.mp.dao.UserMapper;
import cn.yuyangyang.mp.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class SelectTest {

    @Autowired
    private UserMapper userMapper;

    // 普通查询

    @Test
    public void selectById(){
        User user = userMapper.selectById(1094590409767661570L);
        System.out.println(user);
    }

    @Test
    public void selectByIds(){
        List<Long> ids = Arrays.asList(1094590409767661570L, 1094592041087729666L);

        List<User> userList = userMapper.selectBatchIds(ids);
        userList.forEach(System.out::println);
    }

    /**
     * map.put("name", "郭德纲");
     * map.put("age", 20);
     * where name="郭德纲" and age=20
     */
    @Test
    public void selectByMap(){
        Map<String, Object> column = new HashMap<>();
        // 这里是表中字段名不是实体类的属性名
        column.put("manager_id", "1088248166370832385");
        column.put("age", 32);
        List<User> userList = userMapper.selectByMap(column);
        userList.forEach(System.out::println);

    }


    // 使用条件构造器查询  传递参数是 Wrapper 的就是使用条件构造器的查询


    @Test
    public void selectByWrapper(){

//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();  等同于下面的
        QueryWrapper<User> query = Wrappers.query();
        /**
         * 名字包含 马
         * 年龄小于 40
         * lt：小于
         * gt：大于
         */
        query.like("name", "马").lt("age", 40);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapper2(){

//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();  等同于下面的
        QueryWrapper<User> query = Wrappers.query();
        /**
         * 名字包含 马
         * 年龄大于30 小于35
         * 邮箱不为空
         */
        query.like("name", "马").between("age", 30, 35).isNotNull("email");
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }


    @Test
    public void selectByWrapper3(){

        QueryWrapper<User> query = new QueryWrapper<>();
        /**
         * 姓 马 或 年龄大于等于32
         * 年龄降序排列，年龄相同id升序排列
         * like 马%  and age >= 32 orderby age desc, id asc
         */
        query.likeRight("name", "马").or()
                .ge("age", 32).orderByDesc("age").orderByAsc("id");
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapper4(){

        QueryWrapper<User> query = Wrappers.query();
        /**
         * 创建日期在 2020-07-30
         * 直属上级姓王
         * date_format(create_time, '%Y-%m-%d') and manager_id in (select id from user where name like '王%')
         */
        /**
         * apply参考文档
         * https://mybatis.plus/guide/wrapper.html#apply
         */
       query.apply("date_format(create_time, '%Y-%m-%d')={0}", "2020-07-30")
       //query.apply("date_format(create_time, '%Y-%m-%d')='2020-07-30'") // 和上面效果相同，这个有sql注入风险 如： apply("date_format(create_time, '%Y-%m-%d')='2020-07-30' or true or true")
               .inSql("manager_id", "select id from user where name like '王%'");
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }


    @Test
    public void selectByWrapper5(){

        QueryWrapper<User> query = new QueryWrapper<>();
        /**
         * 姓马 并且 （年龄小于35或邮箱不是空）
         */
        query.likeRight("name", "马")
                .and(wq -> wq.lt("age", 35).or().isNotNull("email"));
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrappe6(){

        QueryWrapper<User> query = new QueryWrapper<>();
        /**
         * 姓马 或者（年龄小于40 大于 20 并 manager_id不是空）
         */
        query.likeRight("name", "马")
                .or(wq -> wq.lt("age", 40).gt("age", 20)
                        .isNotNull("manager_id"));
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }


    @Test
    public void selectByWrappe7(){

        QueryWrapper<User> query = new QueryWrapper<>();
        /**
         * （年龄小于40或者manager_id不为空）并且姓刘
         */
        query.nested(wq -> wq.lt("age", 40).or().isNotNull("manager_id"))
                .likeRight("name","刘%");
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }


    @Test
    public void selectByWrappe8(){

        QueryWrapper<User> query = new QueryWrapper<>();
        /**
         * 年龄：31， 33， 34， 35
         */
        query.in("age", Arrays.asList(31, 33, 34, 35));
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }


    @Test
    public void selectByWrappe9(){

        QueryWrapper<User> query = new QueryWrapper<>();
        /**
         * 返回符合条件的一条语句
         * limit 1
         */
        query.in("age", Arrays.asList(31, 33, 34, 35)).last("limit 1"); // 有sql注入风险
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }



    // select 不列出所有字段

    @Test
    public void selectByWrapperPlus(){

//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();  等同于下面的
        QueryWrapper<User> query = Wrappers.query();
        /**
         * 名字包含 马
         * 年龄小于 40
         * lt：小于
         * gt：大于
         */
        // 只显示 id和name
        query.select("id", "name").like("name", "马").lt("age", 40);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }
    @Test
    public void selectByWrapperPlus2(){

//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();  等同于下面的
        QueryWrapper<User> query = Wrappers.query();
        /**
         * 名字包含 马
         * 年龄小于 40
         * lt：小于
         * gt：大于
         */
        // 不显示 create_time 和 manager_id
        query.select(User.class, info -> !info.getColumn().equals("create_time") &&
                !info.getColumn().equals("manager_id")).like("name", "马").lt("age", 40);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }


    @Test
    public void conditionTest(){
        String name = "王%";
        String email = "";
        condition(name, email);
    }

    // 条件查询 如果有的字段是空就不查了
    private void condition(String name, String email){

        QueryWrapper<User> query = Wrappers.query();
        // 如果email是空 条件就不拼接了
        query.like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(email), "email", email);
        List<User> users = userMapper.selectList(query);
        users.forEach(System.out::println);
    }



}
