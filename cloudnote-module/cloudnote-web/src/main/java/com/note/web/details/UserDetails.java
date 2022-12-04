package com.note.web.details;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.stream.Stream;

/**
 * Security用户描述信息
 *
 * @date 2022/11/29 23:20
 **/
public class UserDetails extends User {
    private final Long userId;

    public UserDetails(
           final long userId,
           final String username,
           final String password,
           final boolean status,
           final List<String> roles,
           final List<String> permissions)
    {
        super(
                username,
                password,
                status,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList(
                        Stream
                                .concat(roles.stream().map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r), permissions.stream())
                                .toArray(String[]::new)
                )
        );
                this.userId = userId;
    }

    public Long getUserId(){return this.userId;}
}
