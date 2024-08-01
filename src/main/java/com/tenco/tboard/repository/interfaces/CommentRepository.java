package com.tenco.tboard.repository.interfaces;

import java.util.List;

import com.tenco.tboard.model.Comment;

public interface CommentRepository {
	void addComment(Comment comment);

	void updateComment(Comment comment);

	Comment getCommentById(int id);
	
	List<Comment> getCommentByBoardId(int BoardId, int limit, int offset);
	
	void deleteComment(int id);
}
