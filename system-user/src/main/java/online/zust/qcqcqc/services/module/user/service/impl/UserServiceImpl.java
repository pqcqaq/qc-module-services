package online.zust.qcqcqc.services.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import online.zust.qcqcqc.services.entity.dto.PageResult;
import online.zust.qcqcqc.services.exception.ServiceException;
import online.zust.qcqcqc.services.module.redis.service.RedisService;
import online.zust.qcqcqc.services.module.user.entity.User;
import online.zust.qcqcqc.services.module.user.entity.UserLogin;
import online.zust.qcqcqc.services.module.user.entity.dto.ChangePasswordDto;
import online.zust.qcqcqc.services.module.user.entity.dto.LoginParam;
import online.zust.qcqcqc.services.module.user.entity.dto.RegisterParam;
import online.zust.qcqcqc.services.module.user.entity.dto.UserPublish;
import online.zust.qcqcqc.services.module.user.entity.vo.UserVo;
import online.zust.qcqcqc.services.module.user.exception.ErrorLoginException;
import online.zust.qcqcqc.services.module.user.mapper.UserMapper;
import online.zust.qcqcqc.services.module.user.service.UserService;
import online.zust.qcqcqc.services.utils.JwtUtils;
import online.zust.qcqcqc.utils.EnhanceService;
import online.zust.qcqcqc.utils.threads.Tasks;
import online.zust.qcqcqc.utils.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户;(user)表服务实现类
 *
 * @author qcqcqc
 * @date : 2024-2-16
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends EnhanceService<UserMapper, User> implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    /**
     * token过期时间,单位：ms
     */
    @Value("${token.expiration}")
    private long expireTime;

    @Override
    public boolean register(RegisterParam registerParam) {
        User user = BeanConvertUtils.objectConvent(registerParam, User.class);
        ArrayList<String> roles = new ArrayList<>();
        roles.add("USER");
        user.setRoles(roles);
        user.setEnabled(true);
        checkUserName(user.getUsername());
        String password = user.getPassword();
        // 检查密码
        checkPassword(password);
        user.setPassword(passwordEncoder.encode(password));
        return this.save(user);
    }

    @Override
    public UserVo login(LoginParam loginParam) {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        // 验证用户身份
        //认证成功，生成token
        //获取用户信息（getPrincipal()）
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (authenticate == null) {
            throw new ErrorLoginException("登录失败");
        }
        UserLogin userLogin = (UserLogin) authenticate.getPrincipal();
        User user = userLogin.getUser();
        User deepSearch = this.getDeepSearch(user);
        // 生成token
        String jwt = jwtUtils.createJwt(userLogin);
        // 存入redis
        UserLogin userLogin1 = new UserLogin();
        userLogin1.setUser(deepSearch);
        redisService.set(jwt, userLogin1, expireTime);
        // 返回用户信息
        UserVo userVo = BeanConvertUtils.objectConvent(deepSearch, UserVo.class);
        userVo.setToken(jwt);
        return userVo;
    }

    @Override
    public Boolean logout(HttpServletRequest httpServletRequest) {
        // 从请求头中获取token
        String tokenRow = httpServletRequest.getHeader("Authorization");
        String token = tokenRow.replace("Bearer ", "");
        // 从redis中删除token
        return redisService.delete(token);
    }

    @Override
    public UserVo refreshToken(HttpServletRequest httpServletRequest) {
        // 从请求头中获取token
        String tokenRow = httpServletRequest.getHeader("Authorization");
        String token = tokenRow.replace("Bearer ", "");
        // 从redis中获取token
        UserLogin userLogin = redisService.get(token, UserLogin.class);
        if (userLogin == null) {
            throw new ServiceException("token已过期，请重新登录");
        }
        // 生成新的token
        String jwt = jwtUtils.createJwt(userLogin);
        // 存入redis
        redisService.set(jwt, userLogin, expireTime);
        // 删除旧的token
        redisService.delete(token);
        // 返回用户信息
        User user = userLogin.getUser();
        User deepSearch = this.getDeepSearch(user);
        UserVo userVo = BeanConvertUtils.objectConvent(deepSearch, UserVo.class);
        userVo.setToken(jwt);
        return userVo;
    }

    @Override
    public PageResult<UserVo> getUserList(Integer page, Integer size, String role, String fuzzyQuery) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        ArrayList<String> strings = new ArrayList<>();
        strings.add(role);
        wrapper.eq(StringUtils.isNotBlank(role), User::getRoles, strings.toString())
                .like(StringUtils.isNotBlank(fuzzyQuery), User::getUsername, fuzzyQuery);
        Page<User> userPage1 = this.pageByLambda(page, size, wrapper);
        List<User> records = userPage1.getRecords();
        List<UserVo> list = Tasks.startWithMultiThreadsSync(records, user -> {
            return BeanConvertUtils.objectConvent(user, UserVo.class);
        });
        return new PageResult<>(userPage1, list);
    }

    @Override
    public String addUser(UserPublish userPublish) {
        // 新增
        User user = BeanConvertUtils.objectConvent(userPublish, User.class);
        user.setId(null);
        checkUserName(user.getUsername());
        String password = user.getPassword();
        // 检查密码
        checkPassword(password);
        user.setPassword(passwordEncoder.encode(password));
        ArrayList<String> roles = new ArrayList<>();
        // 已经经过校验了，不会是非法输入
        roles.add(userPublish.getRole());
        user.setRoles(roles);
        this.save(user);
        return "新增用户成功";
    }

    @Override
    public String alterUser(UserPublish userPublish) {
        User user = BeanConvertUtils.objectConvent(userPublish, User.class);
        user.setPassword(null);
        checkUserNameExceptId(user);
        ArrayList<String> roles = new ArrayList<>();
        roles.add(userPublish.getRole());
        user.setRoles(roles);
        this.updateById(user);
        return "更新用户成功";
    }

    @Override
    public String changePassword(ChangePasswordDto userPublish) {
        String password = userPublish.getPassword();
        User user = new User();
        // 检查密码
        checkPassword(password);
        user.setId(Long.valueOf(userPublish.getId()));
        user.setPassword(passwordEncoder.encode(password));
        this.updateById(user);
        return "修改密码成功";
    }

    @Override
    public String disableUser(String id) {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.eq(User::getId, id);
        User user = new User();
        user.setEnabled(false);
        return this.update(user, userLambdaUpdateWrapper) ? "禁用用户成功" : "禁用用户失败";
    }

    @Override
    public String enableUser(String id) {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userLambdaUpdateWrapper.eq(User::getId, id);
        User user = new User();
        user.setEnabled(true);
        return this.update(user, userLambdaUpdateWrapper) ? "启用用户成功" : "启用用户失败";
    }

    private void checkUserNameExceptId(User user) {
        if (user == null || user.getId() == null) {
            throw new ServiceException("用户id不能为空");
        }
        User byId = this.getById(user.getId());
        if (byId == null) {
            throw new ServiceException("用户不存在");
        }
        String username = user.getUsername();
        if (username == null || username.trim().isEmpty()) {
            throw new ServiceException("用户名不能为空");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        userLambdaQueryWrapper.ne(User::getId, user.getId());
        User one = this.getOne(userLambdaQueryWrapper);
        if (one != null) {
            throw new ServiceException("用户名已存在");
        }
    }

    private void checkUserName(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ServiceException("用户名不能为空");
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        User one = this.getOne(userLambdaQueryWrapper);
        if (one != null) {
            throw new ServiceException("用户名已存在");
        }
    }

    @Override
    public String deleteUser(String id) {
        this.removeById(id);
        return "删除用户成功";
    }

    @Override
    public String openApplication() {
        return "打开应用，已记录日志";
    }

    @Override
    public String closeApplication() {
        return "关闭应用，已记录日志";
    }

    private void checkPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new ServiceException("密码不能为空");
        } else if (password.length() < 6) {
            throw new ServiceException("密码长度不能小于6");
        }
    }
}
