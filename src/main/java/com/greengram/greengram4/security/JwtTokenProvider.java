package com.greengram.greengram4.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greengram.greengram4.common.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component//빈등록
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {//토큰을 만드는 작업

    private final ObjectMapper om;
    private final AppProperties appProperties;
    //private Key key; 버전 업으로 사용하지 않음
    private SecretKeySpec secretKeySpec;

    /*public JwtTokenProvider(@Value("${springboot.jwt.secret}")String secret
                ,@Value("${springboot.jwt.header-scheme-name") String headerSchemeName
                ,@Value("${springboot.jwt.token-type}")String tokenType){
        //Value{}에 받은 것을 secret에 넣어주고 생성자를 이용해
        //멤버필드에 넣은 후 돌려주면 찍히게 된다.
        this.secret = secret;
        this.headerSchemeName = headerSchemeName;
        this.tokenType = tokenType;
        log.info("secret : {}", secret); 원래는 파라미터를 받아 값을 하나씩 넣어주었는데 이너클래스에서 Setter를 통해 값을 넣고
        그 값을 가진 클래스 appProperties를 빈등록하여 사용한다?
    }*/
    @PostConstruct//사용방법이 제일 편함 이 애노테이션을 사용하면 해당 메소드를 호출하지 않아도 스프링이 켜질 때 딱 한 번 호출이 당한다
    //해당 메소드가 꼭 한 번 실행이 되었으면 할 때 사용함
    //조건은 빈 등록이 된 후 di 받을 게 있다면 받은 후(@autowired) (지금은 생성자를 통해 받고 있다)에 최종적으로 호출이 되게 한다
    //스프링에 의해 객체화가 된 (빈등록) 것들만 가능
    public void init(){//키 만들어 주는 작업
        log.info("secret : {}", appProperties.getJwt().getSecret());
        /*byte[] keyBytes = Decoders.BASE64.decode(appProperties.getJwt().getSecret());
        log.info("keyBytes : {}", keyBytes); 버전 업으로 사용하지 않음*/
        this.secretKeySpec = new SecretKeySpec(appProperties.getJwt().getSecret().getBytes()
                , SignatureAlgorithm.HS256.getJcaName());
    }
    public String generateAccessToken(MyPrincipal principal){

        return generateToken(principal, appProperties.getJwt().getAccessTokenExpiry());
    }
    public String generateRefreshToken(MyPrincipal principal) {
        return generateToken(principal, appProperties.getJwt().getRefreshTokenExpiry());
    }
    private String generateToken(MyPrincipal principal, long tokenValidMs){//로그인 하는 부분에서 사용

        return Jwts.builder()
                //.issuedAt(now)
                .claims(createClaims(principal))//토큰
                .issuedAt(new Date(System.currentTimeMillis()))//발행시간
                .expiration(new Date(System.currentTimeMillis() + tokenValidMs))//만료시간
                .signWith(secretKeySpec)//암호화
                .compact();
    }

    private Claims createClaims(MyPrincipal principal){//항목이 늘어날수록 add가 계속 추가되어야함>> 유연하게 만들고자 principal 통째로 넣기 ( 문자열로 바꿔준다 )
        try {
            String json = om.writeValueAsString(principal);//json문자열로 바꿔주기

            return Jwts.claims()
                    .add("user", json)
                    .build();
        }catch (Exception e){
            return null;
        }
    }

    public String resolveToken(HttpServletRequest req) {// 요청 시 모든 정보가 담겨져 있다
        String auth = req.getHeader(appProperties.getJwt().getHeaderSchemeName());
        //헤더에 담겨 있는 것들 중에 해당 키값으로 되어있는 값을 불러오는 작업
        if (auth == null) {//가져온 값이 없던 것
            return null;
        }//Bearer
        if (auth.startsWith(appProperties.getJwt().getTokenType())) {//가져온 값이 있던 것
            //startWith : Bearer로 시작하는지 판별한다

            return auth.substring(appProperties.getJwt().getTokenType().length()).trim();
            //substring은 문자열을 잘라주는 메소드 ()파라미터는 6을 리턴하고 있으니 Bearer (공백까지) 제거가 되고 남은 부분을 리턴해주는 것
            //trim은 문자열 앞 뒤의 공백을 제거해준다.
        }
        return null;
    }

    public boolean isValidateToken(String token){//로그인 시 토큰의 만료시간이 지났는지 체크
       try{
           //만료기간이 현재시간을 안 지났다면, 전이라면 false
           //만료기간이 현재시간을 지났다면, 후이라면 true
           return !getAllClaims(token).getExpiration().before(new Date());
           //!를 써서 꼬아주는 이유는 미국인 기준이래,,몰루?
       }catch (Exception e){
           return false;
       }
    }

/*    private Claims getAllClaims(String token){복호화
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                //.parseClaimsJwt(token)
                .parseClaimsJws(token)
                .getBody();
    }
    버전업으로 아래와 같이 변경*/

    public Claims getAllClaims(String token){//복호화
        return Jwts
                .parser()
                .verifyWith(secretKeySpec)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public Authentication getAuthentication(String token){
        UserDetails userDetails = getUserDetailsFromToken(token);
        return userDetails == null? null : new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        //로그인 전에 한 번 만들어지고 로그인 성공 후에도 만들어진다 Authentication에 담으려고 하는 작업?
        //로그인 전에는 아이디, 비번
    }

    public UserDetails getUserDetailsFromToken(String token){

        try {
            Claims claims = getAllClaims(token);
            String json = (String)claims.get("user");
            MyPrincipal myPrincipal = om.readValue(json, MyPrincipal.class);
            return MyUserDetails.builder()
                    .myPrincipal(myPrincipal)
                    .build();
        }catch (Exception e){
            return null;
        }
    }
}
