package online.zust.qcqcqc.services.module.user.service;

import jakarta.servlet.http.HttpServletRequest;
import online.zust.qcqcqc.services.entity.dto.PageResult;
import online.zust.qcqcqc.services.module.user.entity.User;
import online.zust.qcqcqc.services.module.user.entity.dto.ChangePasswordDto;
import online.zust.qcqcqc.services.module.user.entity.dto.LoginParam;
import online.zust.qcqcqc.services.module.user.entity.dto.RegisterParam;
import online.zust.qcqcqc.services.module.user.entity.dto.UserPublish;
import online.zust.qcqcqc.services.module.user.entity.vo.UserVo;
import online.zust.qcqcqc.utils.IServiceEnhance;

/**
 * @author qcqcqc
 * Date: 2024/5/10
 * Time: 下午11:49
 */
public interface UserService extends IServiceEnhance<User> {

    boolean register(RegisterParam registerParam);

    UserVo login(LoginParam loginParam);

    Boolean logout(HttpServletRequest httpServletRequest);

    UserVo refreshToken(HttpServletRequest httpServletRequest);

    PageResult<UserVo> getUserList(Integer page, Integer size, String role, String fuzzyQuery);

    String addUser(UserPublish userPublish);

    String alterUser(UserPublish userPublish);

    String changePassword(ChangePasswordDto userPublish);

    String deleteUser(String id);

    String openApplication();

    String closeApplication();
}
