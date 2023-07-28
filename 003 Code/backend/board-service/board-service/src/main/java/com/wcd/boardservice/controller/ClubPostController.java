package com.wcd.boardservice.controller;

import com.wcd.boardservice.dto.post.ResponsePostListDto;
import com.wcd.boardservice.dto.post.RequestPostDto;
import com.wcd.boardservice.dto.post.ResponsePostDto;
import com.wcd.boardservice.service.PostService;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/clubs/{club-id}")
public class ClubPostController {
    Environment env;
    PostService postService;

    public ClubPostController(Environment env, PostService postService) {
        this.env = env;
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<ResponsePostListDto>> getAllCLubPosts(@PathVariable("club-id") Long clubId,
                                                                     Pageable pageable) {
        Page<ResponsePostListDto> responsePostListDtos = postService.getClubAllPost(clubId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(responsePostListDtos);
    }

    @PostMapping("/posts")
    public ResponseEntity<ResponsePostDto> createNewPost(@PathVariable("club-id") Long clubId,
                                                         @RequestHeader("user-id") Long writerId,
                                                         @RequestBody RequestPostDto requestPostDto) {
        ResponsePostDto responsePostDto = postService.createPost(clubId, writerId, requestPostDto);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }

    @GetMapping("/posts/{post-id}")
    public ResponseEntity<ResponsePostDto> getPost(@PathVariable("club-id") Long clubId,
                                                   @PathVariable("post-id") Long postId) {
        ResponsePostDto responsePostDto = postService.getPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }

    @PatchMapping("/posts/{post-id}")
    public ResponseEntity<ResponsePostDto> updatePost(@PathVariable("club-id") Long clubId,
                                                      @PathVariable("post-id") Long postId,
                                                      @RequestHeader("user-id") Long writerId,
                                                      @RequestBody RequestPostDto requestPostDto) {
        ResponsePostDto responsePostDto = postService.updatePost(postId, writerId, requestPostDto);
        return ResponseEntity.status(HttpStatus.OK).body(responsePostDto);
    }

    @DeleteMapping("/posts/{post-id}")
    public HttpStatus deletePost(@PathVariable("club-id") Long clubId,
                                 @PathVariable("post-id") Long postId,
                                 @RequestHeader("user-id") Long writerId) {
        postService.deletePost(writerId, postId);
        return HttpStatus.OK;
    }
}
