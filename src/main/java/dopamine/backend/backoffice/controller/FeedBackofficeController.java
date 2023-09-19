package dopamine.backend.backoffice.controller;

import dopamine.backend.domain.feed.entity.Feed;
import dopamine.backend.domain.feed.repository.FeedRepository;
import dopamine.backend.domain.level.entity.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Controller
@EnableWebMvc
@RequestMapping("/backoffice/feed")
@RequiredArgsConstructor
public class FeedBackofficeController {
    private final FeedRepository feedRepository;
    @GetMapping
    public String feed(Model model) {
        List<Feed> feeds = feedRepository.findAll();
        model.addAttribute("feeds", feeds);
        return "feed/feed";
    }
}
