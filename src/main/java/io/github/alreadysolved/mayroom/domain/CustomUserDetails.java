package io.github.alreadysolved.mayroom.domain;

import io.github.alreadysolved.mayroom.domain.user.Role;
import io.github.alreadysolved.mayroom.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, OidcUser {
    //
    // UserDetails implement해야 하는 이유는?

    private final User user;
    private Map<String, Object> attributes;

    public String getRole() {
        return user.getRole().name();
    }

    public Long getId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    /** OAuth2User 인터페이스 메서드 구현 **/
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        // 이메일, pk 등 ?
        return user.getEmail();
    }

    /** OAuth2User + OidcUser + UserDetails 인터페이스 메서드 구현 **/
    // User의 Role을 가져와서 Collection<GrantedAuthority>에 넣어 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return authorities;
    }

    /** UserDetails 인터페이스 메서드 구현 **/

    @Override
    public @Nullable String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /** OidcUser 인터페이스 메서드 구현 **/

    @Override
    public Map<String, Object> getClaims() {
        return Map.of();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
