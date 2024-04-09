package com.la.letsassemble;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class ChatController {

    @GetMapping("/chat")
    public String chatGET(){

        log.info("@ChatController , chat GET()");

        return "chat";
    }
}
