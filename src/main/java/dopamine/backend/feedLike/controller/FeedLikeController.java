package dopamine.backend.feedLike.controller;

import dopamine.backend.feedLike.response.FeedLikeResponseDTO;
import dopamine.backend.feedLike.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    /**
     * 피드 좋아요
     * @param feedId
     * @param memberId
     */
    @PostMapping("/feeds/{feedId}/likes")
    public void feedLike(@PathVariable Long feedId, @RequestParam Long memberId){
        feedLikeService.feedLike(feedId, memberId);
    }

    /**
     * 피드 좋아요 취소
     * @param feedId
     * @param memberId
     */
    @DeleteMapping("/feeds/{feedId}/likes")
    public void feedLikeCancel(@PathVariable Long feedId, @RequestParam Long memberId){
        feedLikeService.feedLikeCancel(feedId, memberId);
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
