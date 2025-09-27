package com.lntravel.comment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论回复实体类
 */
@Data
@TableName("comment_reply")
public class CommentReply {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long commentId;
    
    private Long userId;
    
    private String content;
    
    private Long parentId;
    
    private Integer likeCount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
