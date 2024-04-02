package com.la.letsassemble.Controller;

import com.la.letsassemble.Entity.EmailCertificationRequest;
import com.la.letsassemble.Service.UsersService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api/v1/mails")
@RequiredArgsConstructor
public class EmailController {
    private final UsersService usersService;
//    private final MailSendService mailSendService;
//    private final MailVerifyService mailVerifyService;
//
//    @PostMapping("/send-certification")
//    public ResponseEntity<ApiResponse<EmailCertificationResponse>> sendCertificationNumber(@Validated @RequestBody EmailCertificationRequest request)
//            throws MessagingException, NoSuchAlgorithmException {
//        mailSendService.sendEmailForCertification(request.getEmail());
//        return ResponseEntity.ok(ApiResponse.success());
//    }
//
//    @GetMapping("/verify")
//    public ResponseEntity<ApiResponse<Void>> verifyCertificationNumber(
//            @RequestParam(name = "email") String email,
//            @RequestParam(name = "certificationNumber") String certificationNumber
//    ) {
//        mailVerifyService.verifyEmail(email, certificationNumber);
//        return ResponseEntity.ok(ApiResponse.success());
//    }
}
