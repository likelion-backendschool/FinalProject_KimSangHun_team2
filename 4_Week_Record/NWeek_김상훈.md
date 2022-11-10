### 4주차과제정리

### RestApi 구현을 위한 HTTP 정리

- 서로다른 웹서비스가 통신을 하기위해서는 **정해진 형식**이 필요함
- 우리는 주로  서버와 클라이언트 사이의 통신 방법인 **HTTP**를 방식 을 사용함
- 스프링부트 또한  HTTP 통신에 맞게 데이터를 송수신해야함→쉽고 빠르게 규격에 맞춘 HTTP 응답을 보내기 위해 Response Entity를 사용하게 된다.
- ResponseEntity를 잘사용하기위해선 먼저 HTTP 요청,응답에대해서 상세하게 알아야함
    - HTTP요청
        - HTTP요청은 크게 StartLine,Headers,Body 로 나뉨
        - StartLine은 method,URL,version으로 이루어짐
        - Headers는 요청에 대한 접속 운영체제,브라우저,인증정보와 같은 부가정보가 담김
        - Body는 요청에 관련된 json,html 내용 포함
    - HTTP응답
        - HTTP응답도 크게 StartLine,Headers,Body 로 나뉨
        - StartLine은 **요청에대한 처리상태코드를 나타냄**
        - Headers는 요청에 대한 접속 운영체제,브라우저,인증정보와 같은 부가정보가 담김
        - Body는 요청에 관련된 json,html 내용 포함
- 이러한 HTTP Response를 데이터만 넣어주면 자동으로 구성해주는것이 
@ResponseBody와 ResponseEntity 이다.
    - @ResponseBody는 HttpMessageConverter 를 이용해 객체를 HTTP 규격에 맞는 응답으로 직렬화를 해줌→하지만 StatucCode와 Header 의 변경이 복잡하고 어려움
    - ResponseEntity는 HttpEntity 클래스를 상속받은 클래스로 쉽게 직접적으로 StatusCode 와 Header 를 설정해줄수있다.
    - **ResponseEntity 구조**
        
        ![p1](https://user-images.githubusercontent.com/40134318/201179259-de2bc983-8434-4ebf-b0d7-69222e9cfc6f.png)

        
    - **HttpEntity 구조**
        
        ![p2](https://user-images.githubusercontent.com/40134318/201179280-3f0aa87d-08aa-47c0-97c2-2704e0ff8115.png)

        
    - 위에서 본것과 같이 Generic 에 넣은 데이터 형식은 body 의 타입으로 지정되어
    사용 방법은 다음과 같다
    - **new ResponseEntity<body 타입>(body, headers,StatusCode);**
    - **예시 코드**
        
        ```bash
        public ResponseEntity<MoveResponseDto> move(@PathVariable String name,
            @RequestBody MoveDto moveDto) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Game", "Chess");
            
            String command = makeMoveCmd(moveDto.getSource(), moveDto.getTarget());
            springChessService.move(name, command, new Commands(command));
            
            MoveResponseDto moveResponseDto = new MoveResponseDto(springChessService
                .continuedGameInfo(name), name);
        		//R
            return new ResponseEntity<MoveResponseDto>(moveResponseDto, headers, HttpStatus.valueOf(200)); // ResponseEntity를 활용한 응답 생성
        }
        ```
        

### 4_week_mission 구현 설명

- **JWT 구현 및 응답**
    - **build.gradle→JWT 라이브러리 추가**
        
        ```bash
        //JWT
        	implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
        	runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
        	runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'
        ```
        
    - **application.yml→SecretKey 정의**
        
        ```xml
        custom:
          jwt:
            secretKey: secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey
        ```
        
    - **JwtConfig.java**→**jwt 에서 사용될** **SecretKey 생성 해서 빈에 담기**
        
        ```java
        /*
        * Jwt 에 사용되는 시크릿키관련 설정
        * */
        @Configuration
        public class JwtConfig {
            @Value("${custom.jwt.secretKey}")
            private String secretKeyPlain;
        
            @Bean
            public SecretKey jwtSecretKey(){
                String keyBase64Encoded= Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
                return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
            }
        }
        ```
        
    - **Member.java→로그인 성공후 jwt 토큰생성 위해 사용되는  로그인 정보→map 으로 추출 메서드 생성**
        
        ```java
        ...
        
        //JWT 에 암호화되는 사용자 정보
            public Map<String,Object> getAccessTokenClaims(){
                return Util.mapOf(
                        "id",getId(),
                        "createDate",getCreateDate(),
                        "modifyDate",getUpdateDate(),
                        "username",getUsername(),
                        "email",getEmail()
                );
            }
        ```
        
    - **Util.java→ Appcofig 안에 등록된 objectMapper 를 이용해서 map↔Json 직열화,역직열화 메서드 생성**
        
        ```java
        ...
        
        private static ObjectMapper getObjectMapper() {
                return (ObjectMapper) AppConfig.getContext().getBean("objectMapper");
            }
        
            public static class json {
                /*
                * map->json
                * */
                public static Object toStr(Map<String, Object> map) {
                    try {
                        return getObjectMapper().writeValueAsString(map);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        
                /*
                * json->map
                * */
                public static Map<String, Object> toMap(String jsonStr) {
                    try {
                        return getObjectMapper().readValue(jsonStr, LinkedHashMap.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        ```
        
    - **JwtProvider.java→ 로그인 정보와 만료시간을 전달받아 위에서 정의한 함수들을 이용해서
     jwt token을 생성하는 메서드→상세하게는 map으로 받은 사용자정보를 secretkey 로 암호화해서 반환**
        
        ```java
        @Configuration
        @RequiredArgsConstructor
        public class JwtProvider {
            private final SecretKey jwtSecretKey;
        
            private SecretKey getSecretKey(){
                return jwtSecretKey;
            }
        
            /*
            *
            * jwt token 생성
            *
            * */
            public String generateAccessToken(Map<String,Object> claims,int seconds){
                long now=new Date().getTime();
                Date accessTokenExpiresIn=new Date(now+1000L*seconds);
                return Jwts.builder()
                        .claim("body", Util.json.toStr(claims))
                        .setExpiration(accessTokenExpiresIn)
                        .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                        .compact();
            }
        }
        ```
        
    - **MemberService.java**
        
        ```java
        public String genAccessToken(Member member) {
                return jwtProvider.generateAccessToken(member.getAccessTokenClaims(), 60 * 60 * 24 * 90);
            }
        ```
        
    
    - **ApiController.java**
        
        ```java
        ...
        
        String accessToken= memberService.genAccessToken(member);
                //로그인 성공시 헤더에 jwt 토큰 포함해서 반환
               return Util.spring.responseEntityOf(RsData.of(
                       "S-1",
                               "로그인 성공",
                               Util.mapOf("accessToken",accessToken)),
                       Util.spring.httpHeadersOf("Authentication",accessToken));
            }
        }
        ```
        
- **리액트요청안에 있는 JWT받아서 로그인한 회원 상세보기 구현**
    - **JwtProvider.java**→**JWT 읽어서 토큰 검즘,토큰 내용 가져오는 메서드 추가**
        
        ```java
        /*
            * 토큰 검증
           * */
            public boolean verify(String token) {
                try{
                    Jwts.parserBuilder()
                            .setSigningKey(getSecretKey())
                            .build()
                            .parseClaimsJws(token);
                }catch (Exception e){
                    return false;
                }
                return true;
            }
            /*
            * 토큰 내용 가져오기
            * */
            public Map<String, Object> getClaims(String token) {
                String body = Jwts.parserBuilder()
                        .setSigningKey(getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .get("body", String.class);
        
                return Util.json.toMap(body);
            }
        ```
        
    - **JwtAuthorizationFilter.class →JWT 의 claims 를 읽어 Principal 로 등록하는 filter 생성**
        
        ```java
        /*
        * 로그인후 요청을주면 요청헤더에 있는  JWT 토큰으로 부터
        * MemberContext 생성하는 filter
        * */
        @Slf4j
        @Component
        @RequiredArgsConstructor
        public class JwtAuthorizationFilter extends OncePerRequestFilter {
            private final JwtProvider jwtProvider;
            private final MemberService memberService;
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
               String bearerToken=request.getHeader("Authorization");
               if(bearerToken!=null){
                   String token=bearerToken.substring("Bearer ".length());
                    if(jwtProvider.verify(token)){
                        Map<String,Object> claims=jwtProvider.getClaims(token);
                        String username=(String)claims.get("username");
                        Member member=memberService.getMemberByUsername(username);
                        forceAuthentication(member);
                    }
               }
               filterChain.doFilter(request,response);
            }
        
            /*
            * memberContext 생성후 로그인 정보로 변경
            * */
            private void forceAuthentication(Member member) {
                MemberContext memberContext = new MemberContext(member);
        
                UsernamePasswordAuthenticationToken authentication =
                        UsernamePasswordAuthenticationToken.authenticated(
                                memberContext,//principal->객체
                                null,//crediencial->암호
                                member.getAuthorities()//authority->권한
                        );
        
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }
        }
        ```
        
    - **SecurityConfig.java →Controller 사용 전 필터 추가**
        
        ```java
        ...
        .addFilterBefore(
                               jwtAuthorizationFilter,
                               UsernamePasswordAuthenticationFilter.class
                        );
        ```
        
    
- **feat [#18](https://github.com/likelion-backendschool/FinalProject_KimSangHun_team2/issues/18): 내 도서 리스트 구현**
    - - `내 도서에 대한 Api 출력을위해 ApiDto 생성
    - dto 안에 static 메서드로 생성하고 mapping 하는 함수생성
    - RsData 의 data 출력시 클래스 명을 출력하고 싶으면 map 으로 만들어서 출력하기`
- **feat [#18](https://github.com/likelion-backendschool/FinalProject_KimSangHun_team2/issues/18): swagger 기능추가,리액트와 연결 설정 추가**
    - - `cors 설정을 추가해 타 도메인에서 api 호출가능
    - security 설정에도 cors 관련 설정 추가
    - Parameter annotation 으로 swagger 에 파라미터 숨기기`
- **feat [#18](https://github.com/likelion-backendschool/FinalProject_KimSangHun_team2/issues/18): 내 도서 상세보기에서 사용될 ProductPost 엔티티 생성**
    - - `상품 서비스안에 상품 등록시 글 연관관계 등록 기능 추가
    - 상품 등록시 글과의 연관관계를 등록하여 도서 상세보기시 상품의 글들을 볼수있다.
    - 상품글 엔티티,서비스 생성
    - ApiMyBookDetail 을 이용해 내책,상품,상품관련글을 전체적으로 출력`
