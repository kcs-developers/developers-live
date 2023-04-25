package com.developers.live.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JwtUtil {
    @Value("${com.developers.member.secret}")
    private String key;
    public String generateToken(Map<String, Object> valueMap, int minutes) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        log.info(headers);

        // Payload Part Setting
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);
        log.info(payloads);

        // 유효 시간(테스트 시에는 짧게 설정)
        int time = (1) * minutes; //테스트는 분 단위로 나중에 60*24 (일)단위변경
        // 이후 10분 단위로 조정
//        int time = (10) * minutes; //테스트는 분단위로 나중에 60*24 (일)단위변경

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();
    }

    public Map<String, Object> validateToken(String token) throws JwtException {
        Map<String, Object> claim = null;
        claim = Jwts.parser()
                .setSigningKey(key.getBytes()) // Set Key
                .parseClaimsJws(token) // 파싱 및 검증, 실패시 에러
                .getBody();
        return claim;
    }
}
