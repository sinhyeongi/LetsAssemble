package com.la.letsassemble.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBuy_Option is a Querydsl query type for Buy_Option
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBuy_Option extends EntityPathBase<Buy_Option> {

    private static final long serialVersionUID = 150840417L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBuy_Option buy_Option = new QBuy_Option("buy_Option");

    public final StringPath buy_day = createString("buy_day");

    public final StringPath even_day = createString("even_day");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath impUid = createString("impUid");

    public final BooleanPath isOnline = createBoolean("isOnline");

    public final StringPath name = createString("name");

    public final QParty party;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QUsers user;

    public QBuy_Option(String variable) {
        this(Buy_Option.class, forVariable(variable), INITS);
    }

    public QBuy_Option(Path<? extends Buy_Option> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBuy_Option(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBuy_Option(PathMetadata metadata, PathInits inits) {
        this(Buy_Option.class, metadata, inits);
    }

    public QBuy_Option(Class<? extends Buy_Option> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.party = inits.isInitialized("party") ? new QParty(forProperty("party"), inits.get("party")) : null;
        this.user = inits.isInitialized("user") ? new QUsers(forProperty("user")) : null;
    }

}

