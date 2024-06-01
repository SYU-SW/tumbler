package com.two.tumbler.controller;

import com.two.tumbler.model.User;
import com.two.tumbler.service.JwtService;
import com.two.tumbler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;

@Controller
@RequestMapping("/auth")
@SessionAttributes({"role", "phoneNumber", "address", "oauth2User", "provider", "email", "name", "profileImage"})
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/chooseRole")
    public String chooseRolePage() {
        return "chooseRole";
    }

    @PostMapping("/chooseRole")
    public String chooseRole(@RequestParam("role") String role, Model model) {
        model.addAttribute("role", role);
        return "redirect:/auth/chooseSocialLogin";
    }

    @GetMapping("/chooseSocialLogin")
    public String chooseSocialLoginPage() {
        return "chooseSocialLogin";
    }

    @PostMapping("/chooseSocialLogin")
    public String chooseSocialLogin(@RequestParam("provider") String provider, Model model) {
        model.addAttribute("provider", provider);
        if ("google".equals(provider)) {
            return "redirect:/oauth2/authorization/google";
        } else if ("naver".equals(provider)) {
            return "redirect:/oauth2/authorization/naver";
        } else {
            return "redirect:/auth/chooseSocialLogin";
        }
    }

    @GetMapping("/oauth2/redirect")
    public String handleOAuth2Redirect(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken token) {
            OAuth2User oAuth2User = token.getPrincipal();
            String provider = token.getAuthorizedClientRegistrationId();
            String email = null;
            String name = null;
            String profileImage = null;

            if ("google".equals(provider)) {
                email = oAuth2User.getAttribute("email");
                name = oAuth2User.getAttribute("name");
                profileImage = oAuth2User.getAttribute("picture");
            } else if ("naver".equals(provider)) {
                Map<String, Object> response = oAuth2User.getAttribute("response");
                if (response != null) {
                    email = (String) response.get("email");
                    name = (String) response.get("name");
                    profileImage = (String) response.get("profile_image");
                }
            }

            User existingUser = userService.findByEmail(email);
            if (existingUser != null) {
                String jwtToken = jwtService.generateToken(existingUser.getEmail());

                if ("ADMIN".equalsIgnoreCase(existingUser.getRole())) {
                    return "redirect:/admin/home?token=" + jwtToken;
                } else {
                    return "redirect:/user/home?token=" + jwtToken;
                }
            }

            model.addAttribute("oauth2User", oAuth2User);
            model.addAttribute("provider", provider);
            model.addAttribute("email", email);
            model.addAttribute("name", name);
            model.addAttribute("profileImage", profileImage);
        } else {
            return "redirect:/auth/chooseRole";
        }
        return "redirect:/auth/additionalInfo";
    }

    @GetMapping("/additionalInfo")
    public String additionalInfoPage(@SessionAttribute("oauth2User") OAuth2User oAuth2User,
                                     @SessionAttribute("email") String email,
                                     @SessionAttribute("name") String name,
                                     @SessionAttribute("profileImage") String profileImage,
                                     Model model) {
        if (oAuth2User == null) {
            return "redirect:/auth/chooseRole";
        }
        model.addAttribute("oauth2User", oAuth2User);
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("profileImage", profileImage);
        return "additionalInfo";
    }

    @PostMapping("/additionalInfo")
    public String additionalInfo(@RequestParam("phoneNumber") String phoneNumber, Model model) {
        model.addAttribute("phoneNumber", phoneNumber);
        return "redirect:/auth/additionalInfoAddress";
    }

    @GetMapping("/additionalInfoAddress")
    public String additionalInfoAddressPage(@SessionAttribute("oauth2User") OAuth2User oAuth2User,
                                            @SessionAttribute("phoneNumber") String phoneNumber,
                                            @SessionAttribute("email") String email,
                                            @SessionAttribute("name") String name,
                                            @SessionAttribute("profileImage") String profileImage,
                                            Model model) {
        if (oAuth2User == null || phoneNumber == null || email == null || name == null || profileImage == null) {
            return "redirect:/auth/chooseRole";
        }
        model.addAttribute("oauth2User", oAuth2User);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("profileImage", profileImage);
        return "additionalInfoAddress";
    }

    @PostMapping("/additionalInfoAddress")
    public String additionalInfoAddress(@RequestParam("zipcode") String zipcode, @RequestParam("addr") String addr, @RequestParam("addrDetail") String addrDetail,
                                        @SessionAttribute("role") String role,
                                        @SessionAttribute("phoneNumber") String phoneNumber,
                                        @SessionAttribute("oauth2User") OAuth2User oAuth2User,
                                        @SessionAttribute("provider") String provider,
                                        @SessionAttribute("email") String email,
                                        @SessionAttribute("name") String name,
                                        @SessionAttribute("profileImage") String profileImage,
                                        SessionStatus sessionStatus) {

        if (role == null || phoneNumber == null || oAuth2User == null || provider == null || email == null || name == null || profileImage == null) {
            return "redirect:/auth/chooseRole";
        }

        User user = new User();
        user.setRole(role);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setName(name);
        user.setProfileImage(profileImage);
        user.setProvider(provider);

        User.Address address = new User.Address();
        address.setZipcode(zipcode);
        address.setAddr(addr);
        address.setAddrDetail(addrDetail);
        user.setAddress(address);

        User savedUser;
        try {
            savedUser = userService.signup(user);
        } catch (Exception e) {
            return "redirect:/auth/chooseRole";
        }

        sessionStatus.setComplete();

        String jwtToken = jwtService.generateToken(email);

        if ("ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/admin/home?token=" + jwtToken;
        } else {
            return "redirect:/user/home?token=" + jwtToken;
        }
    }
}