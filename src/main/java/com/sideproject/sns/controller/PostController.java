package com.sideproject.sns.controller;


import com.sideproject.sns.controller.request.PostCreateRequestDto;
import com.sideproject.sns.controller.request.PostModifyRequestDto;
import com.sideproject.sns.controller.response.PostAllResponseDto;
import com.sideproject.sns.controller.response.PostModifyResponseDto;
import com.sideproject.sns.controller.response.PostMyFeedResponseDto;
import com.sideproject.sns.controller.response.Response;
import com.sideproject.sns.model.Post;
import com.sideproject.sns.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> createPost(@RequestBody PostCreateRequestDto requestDto, Authentication authentication) {
        postService.createPost(requestDto.getTitle(), requestDto.getContent(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostModifyResponseDto> modifyPost(
            @RequestBody PostModifyRequestDto requestDto,
            @PathVariable Long postId,
            Authentication authentication
    ) {
        Post post = postService.modifyPost(requestDto.getTitle(), requestDto.getContent(), authentication.getName(), postId);
        return Response.success(PostModifyResponseDto.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
        postService.deletePost(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostMyFeedResponseDto>> listPost(Pageable pageable) {
        Page<Post> posts = postService.listPost(pageable);
        return Response.success(posts.map(PostMyFeedResponseDto::fromPost));
    }

    @GetMapping("/my-feed")
    public Response<Page<PostAllResponseDto>> listMyFeed(Pageable pageable, Authentication authentication) {
        Page<Post> posts = postService.listMyFeed(pageable, authentication.getName());
        return Response.success(posts.map(PostAllResponseDto::fromPost));
    }

    @PostMapping("{postId}/likes")
    public Response<Void> likePost(@PathVariable Long postId, Authentication authentication) {
        postService.likePost(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("{postId}/likes")
    public Response<Long> countLikeAtPost(@PathVariable Long postId) {
        return Response.success(postService.countLikeAtPost(postId));
    }
}
