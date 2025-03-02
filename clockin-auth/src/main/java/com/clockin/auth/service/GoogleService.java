package com.clockin.auth.service;

import com.clockin.auth.config.GoogleOAuth2Config;
import com.clockin.common.exception.BusinessException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Google服務類
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final GoogleOAuth2Config googleConfig;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final NetHttpTransport transport = new NetHttpTransport();

    /**
     * 獲取Google登入URL
     *
     * @return Google登入URL
     */
    public String getAuthorizationUrl() {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                transport, JSON_FACTORY, googleConfig.getClientId(), googleConfig.getClientSecret(),
                Collections.singleton("email")
        ).build();

        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        url.setRedirectUri(googleConfig.getRedirectUri());
        url.setScopes(Arrays.asList("email", "profile"));
        return url.build();
    }

    /**
     * 通過授權碼獲取令牌
     *
     * @param code 授權碼
     * @return 令牌
     */
    public GoogleTokenResponse exchangeCodeForToken(String code) {
        try {
            return new GoogleAuthorizationCodeTokenRequest(
                    transport, JSON_FACTORY, googleConfig.getClientId(), googleConfig.getClientSecret(),
                    code, googleConfig.getRedirectUri()
            ).execute();
        } catch (IOException e) {
            log.error("獲取Google令牌失敗", e);
            throw new BusinessException("Google登入失敗，請稍後再試");
        }
    }

    /**
     * 驗證ID令牌並獲取用戶信息
     *
     * @param idTokenString ID令牌
     * @return 用戶信息
     */
    public GoogleIdToken.Payload verifyIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, JSON_FACTORY)
                    .setAudience(Collections.singletonList(googleConfig.getClientId()))
                    .build();
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new BusinessException("Google令牌驗證失敗");
            }
            return idToken.getPayload();
        } catch (GeneralSecurityException | IOException e) {
            log.error("驗證Google令牌失敗", e);
            throw new BusinessException("Google登入失敗，請稍後再試");
        }
    }
}
