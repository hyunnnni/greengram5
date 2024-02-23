package com.greengram.greengram4.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDmMsgEntity is a Querydsl query type for DmMsgEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDmMsgEntity extends EntityPathBase<DmMsgEntity> {

    private static final long serialVersionUID = -929615133L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDmMsgEntity dmMsgEntity = new QDmMsgEntity("dmMsgEntity");

    public final QCreatedAtEntity _super = new QCreatedAtEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDmMsgIds dmMsgIds;

    public final QDmEntity idm;

    public final QUserEntity iuser;

    public final StringPath msg = createString("msg");

    public QDmMsgEntity(String variable) {
        this(DmMsgEntity.class, forVariable(variable), INITS);
    }

    public QDmMsgEntity(Path<? extends DmMsgEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDmMsgEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDmMsgEntity(PathMetadata metadata, PathInits inits) {
        this(DmMsgEntity.class, metadata, inits);
    }

    public QDmMsgEntity(Class<? extends DmMsgEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dmMsgIds = inits.isInitialized("dmMsgIds") ? new QDmMsgIds(forProperty("dmMsgIds")) : null;
        this.idm = inits.isInitialized("idm") ? new QDmEntity(forProperty("idm")) : null;
        this.iuser = inits.isInitialized("iuser") ? new QUserEntity(forProperty("iuser")) : null;
    }

}

