package com.la.letsassemble.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageRead is a Querydsl query type for MessageRead
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessageRead extends EntityPathBase<MessageRead> {

    private static final long serialVersionUID = -2082014038L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageRead messageRead = new QMessageRead("messageRead");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMessage message;

    public final QUsers users;

    public QMessageRead(String variable) {
        this(MessageRead.class, forVariable(variable), INITS);
    }

    public QMessageRead(Path<? extends MessageRead> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessageRead(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessageRead(PathMetadata metadata, PathInits inits) {
        this(MessageRead.class, metadata, inits);
    }

    public QMessageRead(Class<? extends MessageRead> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.message = inits.isInitialized("message") ? new QMessage(forProperty("message"), inits.get("message")) : null;
        this.users = inits.isInitialized("users") ? new QUsers(forProperty("users")) : null;
    }

}

