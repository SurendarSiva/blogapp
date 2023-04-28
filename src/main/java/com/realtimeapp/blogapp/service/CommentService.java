package com.realtimeapp.blogapp.service;

import com.realtimeapp.blogapp.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getAllCommentsByPostId(long postId);

    CommentDto getCommentByPostId(long postId, long CommentId);
}
