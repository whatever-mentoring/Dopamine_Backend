package dopamine.backend.domain.feed.controller;

import dopamine.backend.domain.feed.request.FeedEditDTO;
import dopamine.backend.domain.feed.request.FeedRequestDTO;
import dopamine.backend.domain.feed.response.FeedResponseDTO;
import dopamine.backend.domain.feed.response.FeedYearResponseDto;
import dopamine.backend.domain.feed.service.FeedService;
import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.global.exception.BusinessLogicException;
import dopamine.backend.global.exception.ExceptionCode;
import dopamine.backend.global.jwt.service.JwtService;
import dopamine.backend.global.s3.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "인증글 API")
public class FeedController {

    private final FeedService feedService;
    private final ImageService imageService;
    private final JwtService jwtService;

    /**
     * 피드 조회
     *
     * @param feedId
     */
    @GetMapping("/feeds/{feedId}")
    public FeedResponseDTO getFeed(@PathVariable Long feedId, @RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        return feedService.getFeed(member, feedId);
    }

    /**
     * 피드 리스트 조회 - 최신 순
     *
     * @param page
     * @return
     */
    @GetMapping("/feeds/order-by-date")
    public List<FeedResponseDTO> getFeedsByDate(@RequestParam Integer page, @RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        return feedService.feedListOrderByDate(member, page);
    }

    /**
     * 피드 리스트 조회 - 좋아요 순 (한달 내)
     *
     * @param page
     * @return
     */
    @GetMapping("/feeds/order-by-likecount")
    public List<FeedResponseDTO> getFeedsByLikeCount(@RequestParam Integer page, @RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        return feedService.feedListOrderByLikeCount(member, page);
    }

    /**
     * 피드 리스트 조회 - 챌린지 기준 좋아요 순 (한달 내)
     * 메인 페이지 노출 용
     *
     * @return
     */
    @GetMapping("/feeds/main-page/{challengeId}")
    public List<FeedResponseDTO> getFeedsByChallenge(@PathVariable Long challengeId, @RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        return feedService.feedListByChallengeOrderByDate(member, challengeId);
    }

    /**
     * 피드 리스트 조회 - 사용자 기준 최신 순
     *
     * @param page
     * @return
     */
    @GetMapping("/feeds/by-member")
    public List<FeedResponseDTO> getFeedsByMember(@RequestParam Integer page, @RequestHeader("Authorization") String accessToken) {
        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        return feedService.feedListByMember(member, page);
    }

    /**
     * Month를 기준으로 회원 정보 조회
     *
     * @param accessToken
     * @param month      => 2023-01
     * @return
     */
    @GetMapping("/feeds/month/by-member")
    public List<FeedResponseDTO> getFeedsByMemberAndMonth(@RequestHeader("Authorization") String accessToken,
                                                           @RequestParam(value = "month", required = false) String month) {
        if(StringUtils.isEmpty(month)) {
            throw new BusinessLogicException(ExceptionCode.MISSING_MONTH_REQUEST_PARAM);
        }
        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        return feedService.feedListByMemberAndMonth(member, month);
    }

    /**
     * Year을 기준으로 인증글이 몇 개 있는지 조회
     *
     * @param accessToken
     * @param years      => 2021, 2022, 2023, ...
     * @return
     */
    @GetMapping("/feeds/year/by-member")
    public List<FeedYearResponseDto> getFeedsByMemberAndYear(@RequestHeader("Authorization") String accessToken,
                                                             @RequestParam(value = "years", required = false) List<String> years) {
        if(StringUtils.isEmpty(years)) {
            throw new BusinessLogicException(ExceptionCode.MISSING_YEAR_REQUEST_PARAM);
        }
        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        return feedService.feedListByMemberAndYear(member, years);
    }

    /**
     * 피드 생성
     *
     * @param feedRequestDTO
     */
    @ApiOperation(value = "인증글 작성", notes = "요청값으로 form 데이터에 \n 1. Key:request, Value:문서 하단 Models-FeedRequestDTO를 json \n 2. Key:images, Value:image 여러 개를 한번에")
    @PostMapping(value = "/feeds", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void postFeed(@RequestPart(value = "request") FeedRequestDTO feedRequestDTO,
                         @RequestPart(value = "images") List<MultipartFile> files,
                         @RequestHeader("Authorization") String accessToken) {

        int index = 0;
        for (MultipartFile file : files) {
            if (file == null) continue;

            if (index > 3) break;

            if (index == 0)
                feedRequestDTO.setImage1Url(imageService.updateImage(file, "challenge", "image1"));

            else if (index == 1)
                feedRequestDTO.setImage2Url(imageService.updateImage(file, "challenge", "image2"));

            else if (index == 2)
                feedRequestDTO.setImage3Url(imageService.updateImage(file, "challenge", "image3"));

            index++;
        }
        Member member = jwtService.getMemberFromAccessToken(accessToken); // member 찾기
        feedService.postFeed(member, feedRequestDTO);
    }

    /**
     * 피드 수정
     *
     * @param feedId
     * @param feedEditDTO
     */
    @ApiOperation(value = "인증글 수정", notes = "요청값으로 url에 feedId \n form 데이터에 \n 1. Key:request, Value:문서 하단 Models-FeedEditDTO를 json \n 2. Key:images Value:image 여러 개를 한번에")
    @PutMapping(value = "/feeds/{feedId}", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void editFeed(@PathVariable Long feedId,
                         @RequestPart(value = "request") FeedEditDTO feedEditDTO,
                         @RequestPart(value = "images") List<MultipartFile> files) {
        int index = 0;

        for (MultipartFile file : files) {
            if (file == null) continue;

            if (index > 3) break;

            if (index == 0)
                feedEditDTO.setImage1Url(imageService.updateImage(file, "challenge", "image1"));

            else if (index == 1)
                feedEditDTO.setImage2Url(imageService.updateImage(file, "challenge", "image2"));

            else if (index == 2)
                feedEditDTO.setImage3Url(imageService.updateImage(file, "challenge", "image3"));

            index++;
        }

        feedService.editFeed(feedId, feedEditDTO);
    }

    /**
     * 피드 기준 미달 여부 수정
     *
     * @param feedId
     * @param value
     */
    @PatchMapping("/feeds/{feedId}/fulfill")
    public void patchFeedFulfill(@PathVariable Long feedId, @RequestParam Boolean value) {
        feedService.patchFeedFulfill(feedId, value);
    }

    /**
     * 피드 삭제
     *
     * @param feedId
     */
    @DeleteMapping("/feeds/{feedId}")
    public void deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
    }

    /**
     * 피드 삭제 (DB에서도)
     *
     * @param feedId
     */
    @DeleteMapping("/feeds/{feedId}/hard")
    public void deleteFeedHard(@PathVariable Long feedId) {
        feedService.deleteFeedHard(feedId);
    }
}