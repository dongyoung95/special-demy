package com.specialdemy.security;

import com.specialdemy.model.AdminUser;
import com.specialdemy.repo.AdminUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserDetailsService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다."));
        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPasswordHash())
                .roles("ADMIN")
                .build();
    }
}
