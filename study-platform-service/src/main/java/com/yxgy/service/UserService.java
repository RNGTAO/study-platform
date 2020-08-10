package com.yxgy.service;

import com.yxgy.mapper.UsersMapper;
import com.yxgy.pojo.Users;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserService {

    @Autowired
    private UsersMapper userMapper;

    @Autowired
    private Sid sid;

    //查询用户是否存在
    @Transactional(propagation = Propagation.SUPPORTS)  //如果外部有事务就加入，没有就无事务
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);

        Users u = userMapper.selectOne(user);
        return u == null ? false : true;
    }

    //保存用户
    @Transactional(propagation = Propagation.REQUIRED)  //如果外部方法没有开启事务，那么就自己开事务，如果外部有事务，那么两个是一个事务，任何一个回滚全部回滚
    public void saveUser(Users user) {
        String userId = sid.nextShort();
        user.setId(userId);
        userMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String username, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        return userMapper.selectOneByExample(example);
    }

    //更新用户信息
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserInfo(Users user) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", user.getId());
        userMapper.updateByExampleSelective(user, userExample); //selective的方式只会修改user对象中有值的字段
    }

    //查询用户信息
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserInfo(String userId) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", userId);
        Users user = userMapper.selectOneByExample(userExample);
        return user;
    }
}
