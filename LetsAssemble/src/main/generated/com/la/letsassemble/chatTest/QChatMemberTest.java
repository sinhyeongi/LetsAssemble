package com.la.letsassemble.chatTest;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatMemberTest is a Querydsl query type for ChatMemberTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatMemberTest extends EntityPathBase<ChatMemberTest> {

    private static final long serialVersionUID = -78894768L;

    public static final QChatMemberTest chatMemberTest = new QChatMemberTest("chatMemberTest");

    public final NumberPath<Long> chatId = createNumber("chatId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> joinDate = createDate("joinDate", java.time.LocalDate.class);

    public final NumberPath<Long> messageCount = createNumber("messageCount", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> userid = createNumber("userid", Long.class);

    public QChatMemberTest(String variable) {
        super(ChatMemberTest.class, forVariable(variable));
    }

    public QChatMemberTest(Path<? extends ChatMemberTest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatMemberTest(PathMetadata metadata) {
        super(ChatMemberTest.class, metadata);
    }

}

