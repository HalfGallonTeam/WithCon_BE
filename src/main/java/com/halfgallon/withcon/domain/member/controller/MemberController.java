package com.halfgallon.withcon.domain.member.controller;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.member.dto.request.CurrentPasswordCheckRequest;
import com.halfgallon.withcon.domain.member.dto.request.UpdateMemberRequest;
import com.halfgallon.withcon.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/me")
  public ResponseEntity<?> findMyInformation(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(memberService.getMyInformation(userDetails.getId()));
  }

  @PatchMapping
  public ResponseEntity<Void> updateMember(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody UpdateMemberRequest updateMemberRequest) {
    memberService.updateMember(userDetails.getId(), updateMemberRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/current-password-check")
  public ResponseEntity<Void> currentPasswordCheck(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody CurrentPasswordCheckRequest request) {
    memberService.currentPasswordCheck(userDetails.getId(), request.password());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/profile-image")
  public ResponseEntity<?> uploadProfileImage(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestPart MultipartFile image) {
    memberService.uploadProfileImage(userDetails.getId(), image);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
    memberService.deleteMember(userDetails.getId());
    return ResponseEntity.ok().build();
  }
}
