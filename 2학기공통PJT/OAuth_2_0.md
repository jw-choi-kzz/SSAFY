# [Spring Boot] OAuth2 소셜 로그인 가이드 (구글, 페이스북, 네이버, 카카오)
Spring Security 및 OAuth2를 사용하여 REST API 기반의 소셜 로그인 기능 구현.  
## Spring Security
Spring 기반 어플리케이션 권한과 인증, 인가 등 보안 담당 하위 프레임워크.  
인증, 권한을 Filter 흐름에 따라 처리하게 구현, 이미 대부분의 보안적인 로직 포함.  
## OAuth2.0 
Open Authentication. 사용자 인증, 리소스에 대한 권한 획득(인가).  
### 구성요소
Resource Owner: 사용자 / Client: 리소스 서버 제공 자원 사용하는 외부 플랫폼 / Authorization Server: 외부 플랫폼이 리소스 서버의 사용자 자원 사용 인증 서버 / Resource Server: 사용자의 자원 제공 서버  
*외부 플랫폼을 통해 인증 서버에 인증 요청, 외부 플랫폼은 정보를 사용할 수 있는 권한얻음*  
### 인증 종류
**Authorization Code Grant**: 권한 코드 승인 방식    
서버 사이드에서 인증 처리할 때 이용. Authorization Code를 이용해 Access Token을 요청. Access Token이 바로 클라이언트로 전달되지 않아서 보안에 좋음.    
**Implicit Grant**: 암시적 승인 방식  
브라우저, 앱 같은 서버와의 연동 없는 어플리케이션에서 사용.   
권한 코드 없이 바로 토큰 발급하여 사용하는 방식 => Read Only 서비스에서만 사용 권장   
**Password Credentials Grant**: 비밀번호 자격 증명 방식  
Client에 Service Provider의 아이디, 비밀번호 저장해두고 사용. Client, Service Provider의 관계가 아주 긴밀할 때 사용.  
**Client Credentials Grant**: 클라이언트 자격 증명 방식  
Client와 Resource Owner가 같을 때 사용 => 추가적인 인증 필요하지 않고 Authorization Server로부터 바로 토큰 받을 수 있음.  
  
**Spring OAuth2 Client**: 인증 절차들을 간단히 사용하도록 설정  
### 필요 프레임워크, 라이브러리
#### Spring Security
#### OAuth2 Client
#### Json Web Token (JWT)
  
1. 소셜 로그인 요청  
2. 백엔드로 GET “/oauth2/authorization/{provider-id}?redirect_uri=http://localhost:3000/oauth/redirect”으로 OAuth 인가 요청  
3. Provider 별로 Authorization Code 인증을 할 수 있도록 리다이렉트 (Redirect: GET “https://oauth.provider.com/oauth2.0/authorize?…”)  
4. 리다이렉트 화면에서 provider 서비스에 로그인  
5. 로그인이 완료된 후, Athorization server로부터 백엔드로 Authorization 코드 응답  
6. 백엔드에서 인가 코드를 이용하여 Authorization Server에 엑세스 토큰 요청  
7. 엑세스 토큰 획득  
8. 엑세스 토큰을 이용하여 Resource Server에 유저 데이터 요청  
9. 획득한 유저 데이터를 DB에 저장 후, JWT 엑세스 토큰과 리프레시 토큰을 생성  
10. 리프레시 토큰은 수정 불가능한 쿠키에 저장하고, 엑세스 토큰은 프로트엔드 리다이렉트 URI 에 쿼리스트링에 토큰을 담아 리다이렉트 (Redirect: GET http://localhost:3000/oauth/redirect?token={jwt-token})  
11. 프론트엔드에서 토큰을 저장 후, API 요청 시 헤더에 Authroization: Bearer {token}을 추가하여 요청  
12. 백엔드에서는 토큰을 확인하여 권한 확인  
13. 토큰이 만료된 경우, 쿠키에 저장된 리프레시 토큰을 이용하여 엑세스 토큰과 리프레시 토큰을 재발급  
https://github.com/sushistack/oauth-login-be  
  
### build.gradle 의존성
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-log4j2'
    implementation 'io.jsonwebtoken:jjwt-api:0.11.2'
    implementation 'jakarta.xml.bind:jakarta.xml.bind-api:2.3.2'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.2'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.2'
    compileOnly 'org.projectlombok:lombok'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    runtimeOnly 'mysql:mysql-connector-java'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}

//... 생략 ...
```  
  
### application.yml
```yaml
spring:
  profiles.active: local
  # 데이터 소스 설정
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/oauth_login_tutorial?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true
    username: root
    password: root
    hikari:
      pool-name: jpa-hikari-pool
      maximum-pool-size: 5
      jdbc-url: ${spring.datasource.url}
      username: ${spring.datasource.username}
      password: ${spring.datasource.password}
      driver-class-name: ${spring.datasource.driver-class-name}
      data-source-properties:
        rewriteBatchedStatements: true
  # JPA 설정
  jpa:
    generate-ddl: true
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        hbm2ddl.import_files_sql_extractor: org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor
        current_session_context_class: org.springframework.orm.hibernate5.SpringSessionContext
        default_batch_fetch_size: ${chunkSize:100}
        jdbc.batch_size: 20
        order_inserts: true
        order_updates: true
        format_sql: true
  # Security OAuth
  security:
    oauth2.client:
      registration:
        google:
          clientId: '{구글 client-id}'
          clientSecret: '{구글 client-secret}'
          scope:
            - email
            - profile
        facebook:
          clientId: '{페이스북 client-id}'
          clientSecret: '{페이스북 client-secret}'
          scope:
            - email
            - public_profile
        naver:
          clientId: '{네이버 client-id}'
          clientSecret: '{네이버 client-secret}'
          clientAuthenticationMethod: post
          authorizationGrantType: authorization_code
          redirectUri: "{baseUrl}/{action}/oauth2/code/{registrationId}"
          scope:
            - nickname
            - email
            - profile_image
          clientName: Naver
        kakao:
          clientId: '{카카오 client-id}'
          clientSecret: '{카카오 client-secret}'
          clientAuthenticationMethod: post
          authorizationGrantType: authorization_code
          redirectUri: "{baseUrl}/{action}/oauth2/code/{registrationId}"
          scope:
            - profile_nickname
            - profile_image
            - account_email
          clientName: Kakao
      # Provider 설정
      provider:
        naver:
          authorizationUri: https://nid.naver.com/oauth2.0/authorize
          tokenUri: https://nid.naver.com/oauth2.0/token
          userInfoUri: https://openapi.naver.com/v1/nid/me
          userNameAttribute: response
        kakao:
          authorizationUri: https://kauth.kakao.com/oauth/authorize
          tokenUri: https://kauth.kakao.com/oauth/token
          userInfoUri: https://kapi.kakao.com/v2/user/me
          userNameAttribute: id

# cors 설정
cors:
  allowed-origins: 'http://localhost:3000'
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: '*'
  max-age: 3600

# jwt secret key 설정
jwt.secret: '8sknjlO3NPTBqo319DHLNqsQAfRJEdKsETOds'

# 토큰 관련 secret Key 및 RedirectUri 설정
app:
  auth:
    tokenSecret: 926D96C90030DD58429D2751AC1BDBBC
    tokenExpiry: 1800000
    refreshTokenExpiry: 604800000
  oauth2:
    authorizedRedirectUris:
      - http://localhost:3000/oauth/redirect
```
  
### AuthToken
```java
@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expiry);
    }

    AuthToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }

    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    private String createAuthToken(String id, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }
}

```
### AuthTokenProvider
```java
@Slf4j
public class AuthTokenProvider {

    private final Key key;
    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(id, expiry, key);
    }

    public AuthToken createAuthToken(String id, String role, Date expiry) {
        return new AuthToken(id, role, expiry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) {

        if(authToken.validate()) {

            Claims claims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            log.debug("claims subject := [{}]", claims.getSubject());
            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            throw new TokenValidFailedException();
        }
    }
}
```
### JwtConfig
```java
@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public AuthTokenProvider jwtProvider() {
        return new AuthTokenProvider(secret);
    }
}
```
### TokenAuthenticationFilter
```java
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        String tokenStr = HeaderUtil.getAccessToken(request);
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);

        if (token.validate()) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
```
### TokenValidFailedException
```java
public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException() {
        super("Failed to generate Token.");
    }

    private TokenValidFailedException(String message) {
        super(message);
    }
}
```
### TokenAccessDeniedHandler
```java
@Component
@RequiredArgsConstructor
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    }
}
```
### ProviderType
```java
@Getter
public enum ProviderType {
    GOOGLE,
    FACEBOOK,
    NAVER,
    KAKAO,
    LOCAL;
}
```
### RoleType
```java
@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    GUEST("GUEST", "게스트 권한");

    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}
```


https://deeplify.dev/back-end/spring/oauth2-social-login  
  
Velog의 "[Spring Boot] 카카오 OAuth2.0 구현하기"에서는 카카오 로그인 API를 사용하여 인가 코드를 받고, 이를 사용하여 Access Token을 요청하는 방법을 설명합니다​​.  
https://velog.io/@leesomyoung/Spring-Boot-%EC%B9%B4%EC%B9%B4%EC%98%A4-OAuth2.0-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0  
  
Naver 블로그의 "[Spring] 소셜 로그인 구현하기 (feat. 카카오 로그인)"에서는 카카오 서비스를 이용하여 소셜 로그인 기능을 구현하는 방법을 설명하며, 특히 사용자 정보를 조회하는 과정을 다룹니다​​   
https://m.blog.naver.com/adamdoha/222674641842  
  
Iseunghan의 Tistory 블로그에서는 Spring Boot와 OAuth2-client를 이용한 카카오 로그인 구현 방법을 제공합니다. 여기서는 OAuth 서비스 별로 다른 프로필 정보를 어떻게 처리하는지 설명합니다​​.  
https://iseunghan.tistory.com/300  
  
Velog의 "[Spring] 카카오 로그인 API(oAuth 2.0)-(1)"에서는 카카오 로그인 API를 사용하여 인가 코드를 받고, Access Token을 요청하는 방법을 자세히 설명합니다​​.  
https://velog.io/@shwncho/Spring-%EC%B9%B4%EC%B9%B4%EC%98%A4-%EB%A1%9C%EA%B7%B8%EC%9D%B8-APIoAuth-2.0  
  
Velog의 "[Spring Boot] OAuth 2.0 (카카오 로그인)"에서는 카카오 로그인을 위한 Access Token의 요청 및 사용자 정보 조회 방법을 다룹니다​​.  
https://velog.io/@723poil/Spring-Boot-OAuth-2.0-kakao  
  
Bcp0109의 Tistory 블로그에서는 "Spring Boot 에서 Kakao, Naver 로그인하기 1편 (OAuth 2.0) - 앱 등록"과 "2편 - 코드 구현"을 통해 OAuth 2.0을 이용한 소셜 로그인 기능을 구현하는 전체 과정을 단계별로 안내합니다​​​​.  
https://bcp0109.tistory.com/379  
https://bcp0109.tistory.com/380  
  
Spring.io의 "Getting Started | Spring Boot and OAuth2"에서는 Spring Boot와 OAuth2를 사용하여 소셜 로그인을 구현하는 기본적인 방법을 설명합니다​​.  
https://spring.io/guides/tutorials/spring-boot-oauth2/   

