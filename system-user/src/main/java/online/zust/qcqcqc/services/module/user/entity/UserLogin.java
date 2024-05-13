package online.zust.qcqcqc.services.module.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author qcqcqc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserLogin implements UserDetails {

    @Serial
    private static final long serialVersionUID = -6537063971623940187L;
    /**
     * 传入用户对象
     */
    private User user;

    /**
     * 判断权限信息
     *
     * @return 权限信息
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<String> roles = user.getRoles();
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (String role : roles) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return simpleGrantedAuthorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 判断是否未过期
     *
     * @return true
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 判断账户是否未锁定
     *
     * @return true
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 判断是否可以使用
     *
     * @return true
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
