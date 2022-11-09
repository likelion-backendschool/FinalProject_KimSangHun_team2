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
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/34db0add-7af4-4717-bb30-66b6a9c15a82/Untitled.png)
        
    - **HttpEntity 구조**
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/999a9bc7-2424-49b7-a7c2-cc41585ef47b/Untitled.png)
        
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
                       Util.spring.httpHeadersOf("Authentication","JWT_Access_Token"));
            }
        }
        ```
