package com.example.authserver.security.entity;

public enum Authority {
    ROLE_ADMIN               /* 관리자 */
    , ROLE_USER              /* 기본권한 */

    , ROLE_VIEW              /* 인력조회 */
    , ROLE_MANAGE            /* 인력관리 */

    , ROLE_DOWNLOAD          /* 다운로드권한 */
    , ROLE_APPROVAL          /* 승인권한 */
    , ROLE_ALL               /* 전체권한 */
}