package com.realtimeapp.blogapp.service.impl;

import com.realtimeapp.blogapp.entity.Comment;
import com.realtimeapp.blogapp.entity.Post;
import com.realtimeapp.blogapp.exception.BlogAPIException;
import com.realtimeapp.blogapp.exception.ResourceNotFoundException;
import com.realtimeapp.blogapp.payload.CommentDto;
import com.realtimeapp.blogapp.repository.CommentRepository;
import com.realtimeapp.blogapp.repository.PostRepository;
import com.realtimeapp.blogapp.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        //retrieve post from postid
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        //set comment to the post
        comment.setPost(post);
        //save comment
        Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);

    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByPostId(long postId, long commentId) {
        //retrieve post
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
        //retrieve comment by post Id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment not found");
        }

        return mapToDto(comment);
    }


    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}
