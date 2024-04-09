package com.la.letsassemble.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPartyInfo is a Querydsl query type for PartyInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPartyInfo extends EntityPathBase<PartyInfo> {

    private static final long serialVersionUID = -612150687L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPartyInfo partyInfo = new QPartyInfo("partyInfo");

    public final StringPath aplicant_day = createString("aplicant_day");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isBlack = createBoolean("isBlack");

    public final QParty party_id;

    public final StringPath state = createString("state");

    public final QUsers user;

    public QPartyInfo(String variable) {
        this(PartyInfo.class, forVariable(variable), INITS);
    }

    public QPartyInfo(Path<? extends PartyInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPartyInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPartyInfo(PathMetadata metadata, PathInits inits) {
        this(PartyInfo.class, metadata, inits);
    }

    public QPartyInfo(Class<? extends PartyInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.party_id = inits.isInitialized("party_id") ? new QParty(forProperty("party_id"), inits.get("party_id")) : null;
        this.user = inits.isInitialized("user") ? new QUsers(forProperty("user")) : null;
    }

}

