package dopamine.backend.backoffice.controller;

import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.feed.repository.FeedRepository;
import dopamine.backend.domain.feed.service.FeedService;
import dopamine.backend.domain.level.entity.Level;
import dopamine.backend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Controller
@EnableWebMvc
@RequestMapping("/backoffice/feed")
@RequiredArgsConstructor
public class FeedBackofficeController {
    private final FeedRepository feedRepository;
    private final FeedService feedService;
    private final MemberService memberService;

    @GetMapping
    public String feed(Model model) {
        List<Feed> feeds = feedRepository.findAll();
        model.addAttribute("feeds", feeds);
        return "feed/feed";
    }

    @GetMapping("/{feedId}/delete")
    public String feedDelete(@PathVariable("feedId") Long feedId) {
        Feed feed = feedService.verifiedFeed(feedId);
        feedService.deleteFeedHard(feedId);
        if (feed.getFulfillYn()) {
            memberService.minusMemberExp(feed.getMember(), feed.getChallenge().getChallengeLevel().getExp());
        }
        return "redirect:/backoffice/feed";
    }

    @GetMapping("/{feedId}/fullfill")
    public String feedFullfill(@PathVariable("feedId") Long feedId) {
        Feed feed = feedService.verifiedFeed(feedId);

        if (feedService.verifiedFeed(feedId).getFulfillYn()) {
            feedService.patchFeedFulfill(feedId, false);
            memberService.minusMemberExp(feed.getMember(), feed.getChallenge().getChallengeLevel().getExp());
        } else {
            feedService.patchFeedFulfill(feedId, true);
            memberService.plusMemberExp(feed.getMember(), feed.getChallenge().getChallengeLevel().getExp());
        }
        return "redirect:/backoffice/feed";
    }

}
