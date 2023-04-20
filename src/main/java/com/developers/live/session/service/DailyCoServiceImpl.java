package com.developers.live.session.service;

import com.developers.live.exception.DailyCoException;
import com.developers.live.session.dto.dailyco.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@AllArgsConstructor
public class DailyCoServiceImpl implements DailyCoService{

    @Value("${com.developers.daily.secret}")
    private String apiKey;

    @Override
    public String create(Long nbf, Long exp) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String createRoomUrl = "https://api.daily.co/v1/rooms";

            Map<String, Long> properties = new HashMap<>();
            properties.put("nbf", nbf);
            properties.put("exp", exp);
            Map<String, Object> body = new HashMap<>();
            body.put("properties", properties);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer "+apiKey);
            HttpEntity<Map> entity = new HttpEntity<>(body, headers);

            ResponseEntity<DailyCoAnswer> responseEntity = restTemplate.postForEntity(createRoomUrl, entity, DailyCoAnswer.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("방 생성 성공! " + responseEntity.getBody());
                return responseEntity.getBody().getUrl();
            } else {
                log.error("방 생성 실패: " + responseEntity.getBody());
                throw new DailyCoException("방 생성이 실패하였습니다! " + responseEntity, HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (Exception e) {
            log.error("방 생성 오류! ", e);
            throw new DailyCoException("dailyco에서 방 생성 오류가 발생하였습니다" + e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public DailyCoResponse delete(String roomName) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String deleteRoomUrl = "https://api.daily.co/v1/rooms/" + roomName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer 17885ccd0a16f1c5e4d642075773a775fc45b46b020cfac4023c3fb88f7aba01");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                restTemplate.delete(deleteRoomUrl, entity);
                log.info("방 삭제 성공! " + roomName);
                DailyCoResponse response = DailyCoResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("방이 성공적으로 삭제되었습니다!")
                        .data(null)
                        .build();

                return response;
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    log.error("방 삭제 실패: 방을 찾을 수 없습니다. room-name-with-typo: " + roomName);
                    throw new DailyCoException("방 삭제 실패: 방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value());
                } else {
                    log.error("방 삭제 실패: " + e);
                    throw new DailyCoException("방 삭제 실패: 알 수 없는 오류가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
            }
        }
        catch (Exception e) {
                log.error("방 삭제 오류: ", e);
                throw new DailyCoException("dailyco에서 방 삭제 오류가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }
