package com.specialdemy.config;

import com.specialdemy.model.AdminUser;
import com.specialdemy.repo.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class AdminBootstrapRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminBootstrapProperties adminBootstrapProperties;

    public AdminBootstrapRunner(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder,
            AdminBootstrapProperties adminBootstrapProperties) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminBootstrapProperties = adminBootstrapProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminUserRepository.count() > 0) {
            return;
        }
        AdminUser admin = new AdminUser();
        admin.setUsername(adminBootstrapProperties.getUsername());
        admin.setPasswordHash(passwordEncoder.encode(adminBootstrapProperties.getInitialPassword()));
        adminUserRepository.save(admin);
        log.warn(
                "최초 관리자 계정이 생성되었습니다. 사용자명: {} — 비밀번호는 설정값 또는 환경 변수로 확인하세요. 운영 환경에서는 반드시 변경하세요.",
                adminBootstrapProperties.getUsername());
    }
}
