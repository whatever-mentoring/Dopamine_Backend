package dopamine.backend.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@EnableWebMvc
@RequestMapping("/backoffice")
public class BackofficeController {

    @GetMapping
    public String home(){
        return "home/home";
    }

    @GetMapping("/level")
    public String level(){
        return "level";
    }

    @GetMapping("/member")
    public String member(){
        return "member";
    }

    @GetMapping("/feed")
    public String feed(){
        return "feed";
    }

    @GetMapping("/challenge")
    public String challenge(){
        return "challenge";
    }

}
