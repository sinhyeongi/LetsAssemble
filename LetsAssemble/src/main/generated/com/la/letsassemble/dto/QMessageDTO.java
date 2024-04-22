package com.la.letsassemble.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.la.letsassemble.dto.QMessageDTO is a Querydsl Projection type for MessageDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMessageDTO extends ConstructorExpression<MessageDTO> {

    private static final long serialVersionUID = 906469343L;

    public QMessageDTO(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> writer, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> date, com.querydsl.core.types.Expression<Long> partyId) {
        super(MessageDTO.class, new Class<?>[]{long.class, String.class, String.class, String.class, long.class}, id, writer, content, date, partyId);
    }

}

