package com.demo.webtuyendung.payload;

public class CommentRequestDto {
    private Integer candidateIdSendComment;
    private String content;

    public Integer getCandidateIdSendComment() {
        return candidateIdSendComment;
    }

    public void setCandidateIdSendComment(Integer candidateIdSendComment) {
        this.candidateIdSendComment = candidateIdSendComment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
