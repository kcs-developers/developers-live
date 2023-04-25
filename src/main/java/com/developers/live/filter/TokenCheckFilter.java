package com.developers.live.filter;

import com.developers.live.exception.AccessTokenException;
import com.developers.live.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {
        log.info("request: {}", request.getRequestURI());
        String headerStr = request.getHeader("Authorization");

        // 아래 조건을 만족하지 않는다면 예외 발생
        if(headerStr == null || headerStr.length() < 8){
            log.info("[TokenCheckFilter] request header str is null or length < 8");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }
        // Bearer 생략, 실제 토큰 가져오기
        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);
        // 타입 검사하기
        if(tokenType.equalsIgnoreCase("Bearer") == false){
            log.info("[TokenCheckFilter] request token not start str Bearer");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }
        // 토큰의 유효성 검사하기
        /**
         * MalformedJwtException: JWT의 형식이 잘못되어 파싱할 수 없는 경우 발생함. 올바른 형식의 JWT가 아닌 일반 문자열을 파싱하려고 할 때 발생할 수 있음.
         * SignatureException: JWT의 서명 검증이 실패한 경우 발생함. JWT의 서명은 발행한 서버가 유효한 것으로 증명하기 위해 사용되며, 이 예외는 서명 검증에 실패한 경우 발생함.
         * ExpiredJwtException: JWT가 만료된 경우 발생함. JWT에는 발행 시간과 만료 시간이 포함되며, 이 예외는 JWT의 만료 시간이 현재 시간보다 이전인 경우 발생함.
         */
        try{
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        }catch(MalformedJwtException malformedJwtException){
            log.error("[TokenCheckFilter] Token Validation Fail - MalformedJwtException");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch(SignatureException signatureException){
            log.error("[TokenCheckFilter] Token Validation Fail - SignatureException");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch(ExpiredJwtException expiredJwtException){
            log.error("[TokenCheckFilter] Token Validation Fail - ExpiredJwtException");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException
    {
        // 클라이언트의 URI 확인하기
        String path = request.getRequestURI();
        log.info("[TokenCheckFilter] request URI: {}", path);
        log.info("[TokenCheckFilter] request Method: {}", request.getMethod());

        /**
         * 인증 처리가 필요없다면 TokenCheckFilter 로직을 수횅할 필요가 없음.
         * 사용자: 로그인, 회원가입
         * 문제풀이: 전체 문제 목록 조회
         * 멘토링: 전체 방 목록 조회
         */
        if (path.startsWith("/api/room")
            || path.startsWith("/api/room/next")
            || path.startsWith("/api/room/top")
            || path.startsWith("/api/room/{searchingword}")) {
            log.info("[TokenCheckFilter] Skip Token Check Filter, path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }
        // api로 시작하지 않는 요청이라면 다음 필터로 넘긴다.
        // doFilterInternal는 리턴 타입이 void이기 때문에 리턴을 안해도 될 것 같지만, 리턴을 반드시 해야한다.
        // return을 만날 때까지 무조건 수행된다.
        if (!path.startsWith("/api/")) {
            log.info("[TokenCheckFilter] Skip Token Check Filter");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("[TokenCheckFilter] Token Check Filter !");
        try {
            validateAccessToken(request);
            // 다음 필터에게 처리를 넘긴다.
            // AOP, Filter, Interceptor 등으로 넘길 수 있다.
            filterChain.doFilter(request, response);
        }catch(AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }
    }
}

