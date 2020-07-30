package cn.yuyangyang.mp;

import cn.yuyangyang.mp.dao.UserMapper;
import cn.yuyangyang.mp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class MybatisPlusStudyApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void mpSelectTest(){

        List<User> users = userMapper.selectList(null);

        users.forEach(System.out::println);
    }


    @Test
    public void insertTest(){
        User user = new User();
        user.setName("郭德纲");
        user.setAge(32);
        user.setManagerId(1088248166370832385L);
        user.setEmail("gdg@baomidou.com");
        user.setCreateTime(LocalDateTime.now());


//        User.setRemark("这是一个备注~");
        int result = userMapper.insert(user);
        System.out.println(String.format("影响条数：%s", result));
    }

}
