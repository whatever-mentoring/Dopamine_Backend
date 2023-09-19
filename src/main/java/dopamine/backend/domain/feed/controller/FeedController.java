package dopamine.backend.domain.feed.controller;

import dopamine.backend.domain.feed.request.FeedEditDTO;
import dopamine.backend.domain.feed.request.FeedRequestDTO;
import dopamine.backend.domain.feed.response.FeedResponseDTO;
import dopamine.backend.domain.feed.service.FeedService;
import dopamine.backend.domain.member.entity.Member;
import dopamine.backend.global.exception.BusinessLogicException;
import dopamine.backend.global.exception.ExceptionCode;
import dopamine.backend.global.jwt.service.JwtService;
import dopamine.backend.global.s3.service.ImageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
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
    public FeedResponseDTO getFeed(@PathVariable Long feedId) {
        return feedService.getFeed(feedId);
    }

    /**
     * 피드 리스트 조회 - 최신 순
     *
     * @param page
     * @return
     */
    @GetMapping("/feeds/order-by-date")
    public List<FeedResponseDTO> getFeedsByDate(@RequestParam Integer page) {
        return feedService.feedListOrderByDate(page);
    }

    /**
     * 피드 리스트 조회 - 좋아요 순 (한달 내)
     *
     * @param page
     * @return
     */
    @GetMapping("/feeds/order-by-likecount")
    public List<FeedResponseDTO> getFeedsByLikeCount(@RequestParam Integer page) {
        return feedService.feedListOrderByLikeCount(page);
    }

    /**
     * 피드 리스트 조회 - 챌린지 기준 좋아요 순 (한달 내)
     * 메인 페이지 노출 용
     *
     * @return
     */
    @GetMapping("/feeds/main-page/{challengeId}")
    public List<FeedResponseDTO> getFeedsByChallenge(@PathVariable Long challengeId) {
        return feedService.feedListByChallengeOrderByDate(challengeId);
    }

    /**
     * 피드 리스트 조회 - 사용자 기준 최신 순
     *
     * @param memberId
     * @param page
     * @return
     */
    @GetMapping("/feeds/by-member/{memberId}")
    public List<FeedResponseDTO> getFeedsByMember(@PathVariable Long memberId, @RequestParam Integer page) {
        return feedService.feedListByMember(memberId, page);
    }

    /**
     * Month를 기준으로 회원 정보 조회
     *
     * @param accessToken
     * @param months      => 2023_1
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
     * 피드 생성
     *
     * @param feedRequestDTO
     */
    @PostMapping("/feeds")
    public void postFeed(@RequestPart(value = "request") FeedRequestDTO feedRequestDTO,
                         @RequestPart(value = "images") List<MultipartFile> files) {

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

        feedService.postFeed(feedRequestDTO);
    }

    /**
     * 피드 수정
     *
     * @param feedId
     * @param feedEditDTO
     */
    @PutMapping("/feeds/{feedId}")
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