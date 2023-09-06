package dopamine.backend.jwt.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dopamine.backend.jwt.dto.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class OauthService {

    public String getKakaoAccessToken(String code) {

        String accessToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로 설정
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요처에 필요로 요구하는 파라미터를 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(conn.getOutputStream())));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=3b154b60421d117460fd5e1f5333e00a");
            sb.append("&redirect_uri=http://127.0.0.1:8080/api/oauth/kakao");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            String result = getRequestResult(conn);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = new Gson().fromJson(result, JsonElement.class);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            log.info("access_token : " + accessToken);

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public KakaoUserInfo getKakaoUserInfo(String token) {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo();

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            String result = getRequestResult(conn);

            //Gson 라이브러리로 JSON파싱

            JsonElement element = new Gson().fromJson(result, JsonElement.class);

            JsonElement kakaoAccount = element.getAsJsonObject().get("kakao_account");
            JsonElement profile = kakaoAccount.getAsJsonObject().get("profile");

            //dto에 저장하기
            kakaoUserInfo.setId(element.getAsJsonObject().get("id").getAsLong());
//            kakaoUserInfo.setNickname(profile.getAsJsonObject().get("nickname").getAsString());
            kakaoUserInfo.setProfileImgUrl(profile.getAsJsonObject().get("profile_image_url").getAsString());
//            kakaoUserInfo.setThumnailImgUrl(profile.getAsJsonObject().get("thumbnail_image_url").getAsString());
            kakaoUserInfo.setHasBirthDay(kakaoAccount.getAsJsonObject().get("has_birthday").getAsBoolean());
            kakaoUserInfo.setHasGender(kakaoAccount.getAsJsonObject().get("has_gender").getAsBoolean());

            if (!kakaoUserInfo.isHasBirthDay()) {
                kakaoUserInfo.setBirthday(kakaoAccount.getAsJsonObject().get("birthday").getAsString());
            }

            if (!kakaoUserInfo.isHasGender()) {
                kakaoUserInfo.setGender(kakaoAccount.getAsJsonObject().get("gender").getAsString());
            }

            log.info(kakaoUserInfo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kakaoUserInfo;
    }

    private String getRequestResult(HttpURLConnection conn){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
