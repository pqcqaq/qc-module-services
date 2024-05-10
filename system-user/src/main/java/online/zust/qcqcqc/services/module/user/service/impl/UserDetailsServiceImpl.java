package online.zust.qcqcqc.services.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import online.zust.qcqcqc.services.module.user.entity.User;
import online.zust.qcqcqc.services.module.user.entity.UserLogin;
import online.zust.qcqcqc.services.module.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author qcqcqc
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).last("limit 1"));
        //异常
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名未发现");
        }
        //数据封装为UserDetails返回
        return new UserLogin(user);
    }
}
