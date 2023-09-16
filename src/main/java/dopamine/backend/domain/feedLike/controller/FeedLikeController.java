package dopamine.backend.domain.feedLike.controller;

import dopamine.backend.domain.feedLike.response.FeedLikeResponseDTO;
import dopamine.backend.domain.feedLike.service.FeedLikeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Api(tags = "인증글 좋아요 API")
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    /**
     * 피드 좋아요
     * @param feedId
     * @param memberId
     */
    @PostMapping("/feeds/{feedId}/likes")
    public Integer feedLike(@PathVariable Long feedId, @RequestParam Long memberId){
        return feedLikeService.feedLike(feedId, memberId);
    }

    /**
     * 피드 좋아요 취소
     * @param feedId
     * @param memberId
     */
    @DeleteMapping("/feeds/{feedId}/likes")
    public Integer feedLikeCancel(@PathVariable Long feedId, @RequestParam Long memberId){
        return feedLikeService.feedLikeCancel(feedId, memberId);
    }

    /**
     * 피드 좋아요 정보
     * @param feedId
     */
    @GetMapping("/feeds/{feedId}/likes")
    public List<FeedLikeResponseDTO> getFeedLikes(@PathVariable Long feedId){
        return feedLikeService.getFeedLikes(feedId);
    }
}
