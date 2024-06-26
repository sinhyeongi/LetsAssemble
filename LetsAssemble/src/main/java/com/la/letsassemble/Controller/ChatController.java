package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Security_Custom.PricipalDetails;
import com.la.letsassemble.Service.*;
import com.la.letsassemble.dto.MessageDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final RedisMessageSubscriber redisMessageSubscriber;
    private final MessageService service;
    private final MessageReadService messageReadService;
    private final PartyInfoService partyInfoService;


    @GetMapping("/{partyId}")
    public String ChatForm(Model model,@AuthenticationPrincipal PricipalDetails pricipalDetails, @PathVariable Long partyId){
        Users u = pricipalDetails.getUser();
        Optional<PartyInfo> info = partyInfoService.findByPartIdAndUser(partyId,u.getEmail());
        if(info.isEmpty()){
            return "redirect:/";
        }
        List<MessageDTO> messages = service.findByPartyIdLimit30(partyId);
        model.addAttribute("note",info.get().getParty().getNotification());
        model.addAttribute("messages",messages);
        model.addAttribute("partyId",partyId);
        model.addAttribute("email",u.getEmail());
        return "chat/Chat";
    }
    @MessageMapping("/send/{partyId}")
    public void sendMessage(@Payload String content, @DestinationVariable Long partyId, @AuthenticationPrincipal Authentication auth){
        if (auth.getPrincipal() instanceof PricipalDetails) {
            Users u = ((PricipalDetails) auth.getPrincipal()).getUser();
            String channel = "/topic/party-" + partyId;
            MessageDTO dto = service.SendChatBefore(partyId, content, u);
            if (dto == null) {
                return;
            }
            redisMessageSubscriber.sendMessageToRedis(channel,dto);
        }
    }

    @DeleteMapping("/read/{messageId}")
    public @ResponseBody String ReadMessage(@PathVariable Long messageId,@AuthenticationPrincipal PricipalDetails pricipalDetails){
        return messageReadService.DeleteMessageByUsers(messageId,pricipalDetails.getUser()) == 1 ? "ok" : "fail";
    }
}
