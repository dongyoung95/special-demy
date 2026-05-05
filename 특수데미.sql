-- =============================================================================
-- 특수데미(special-demy) 운영 DB 스키마
-- =============================================================================
-- 대상: MySQL 8.0+ / MariaDB 10.5+ (InnoDB, utf8mb4)
-- 용도: 문의(inquiries)·관리자(admin_users) 영구 저장. 로컬 개발은 H2 파일 DB를
--       쓸 수 있으나, 운영·스테이징에서는 본 스크립트로 테이블을 준비한다.
--
-- 멱등성:
--   - CREATE TABLE IF NOT EXISTS 만 사용한다. 동일 스크립트를 여러 번 실행해도
--     기존 테이블이 있으면 CREATE 구문은 건너뛴다(데이터는 삭제하지 않는다).
--   - 인덱스·유니크는 CREATE TABLE 정의에 포함한다(별도 CREATE INDEX 없음).
--
-- 실행 예:
--   mysql -h HOST -u USER -p DATABASE < 특수데미.sql
--   (또는 클라이언트에서 대상 DATABASE 선택 후 전체 실행)
--
-- 애플리케이션 매핑:
--   Spring Data JPA 엔티티 Inquiry, AdminUser 와 컬럼명(snake_case) 일치.
--   Enum 문자열: preferred_contact = PHONE|EMAIL|KAKAO
--                 status           = NEW|READ|REPLIED|ON_HOLD
-- =============================================================================

SET NAMES utf8mb4;

-- (선택) DB 자체를 만들 권한이 있을 때만 주석 해제
-- CREATE DATABASE IF NOT EXISTS specialdemy
--   DEFAULT CHARACTER SET utf8mb4
--   DEFAULT COLLATE utf8mb4_unicode_ci;
-- USE specialdemy;

-- ---------------------------------------------------------------------------
-- 관리자 계정 (BCrypt 해시 저장, 평문 비밀번호 금지)
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin_users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(120) NOT NULL,
    password_hash VARCHAR(200) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_admin_users_username (username)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='관리자 로그인 계정';

-- ---------------------------------------------------------------------------
-- 사이트 문의
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS inquiries (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(30) DEFAULT NULL,
    email VARCHAR(200) DEFAULT NULL,
    preferred_contact VARCHAR(20) NOT NULL COMMENT 'PHONE, EMAIL, KAKAO',
    program VARCHAR(120) NOT NULL,
    preparation_status LONGTEXT NOT NULL,
    message LONGTEXT NOT NULL,
    status VARCHAR(20) NOT NULL COMMENT 'NEW, READ, REPLIED, ON_HOLD',
    admin_memo LONGTEXT DEFAULT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY idx_inquiries_created_at (created_at),
    KEY idx_inquiries_status (status),
    KEY idx_inquiries_status_created (status, created_at)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='사이트 문의 접수';

-- =============================================================================
-- 끝. (스키마 변경 시 버전 주석을 남기고, Flyway/Liquibase 도입 시 이 파일과 동기화)
-- =============================================================================
