package com.bowltop.account;

import java.util.List;

import javax.validation.Valid;

import com.bowltop.domain.Account;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    // private final AuthenticationManager authenticationManager;

    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    private void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("볼탑, 회원 가입 인증");
        mailMessage
                .setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email="
                        + newAccount.getEmail());
        javaMailSender.send(mailMessage);
    }

    public void login(Account account) {
        // AuthenticationManager에서 사용하는 생성자를 가져와서 사용하는것임
        // 이렇게하는 이유는 패스워드 접근을 인코딩된 패스워드에만 접근할 수 있기때문에
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                account.getNickname(), 
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
        
        // 정석적인 방법의 코드- AuthenticationToken => Form인증시 사용됨
        // UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        //         username, password);
        // Authentication authentication = authenticationManager.authenticate(token);
        // SecurityContext context = SecurityContextHolder.getContext();
        // context.setAuthentication(authentication);
    }

}
