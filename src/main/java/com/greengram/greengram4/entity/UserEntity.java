package com.greengram.greengram4.entity;

import com.greengram.greengram4.common.ProviderTypeEnum;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.security.core.parameters.P;

@Entity//무조건 pk를 적용해주어야 빨간줄이 안 뜬다
@Table(name = "t_user")
public class UserEntity extends BaseEntity{
    @Id//멤버필드에 pk를 주는 것
    @Column(columnDefinition = "BIGINT UNSIGNED")//부호도 넣을 수 있음 오라클은 자동으로 넣기 가능?
    @GeneratedValue(strategy = GenerationType.IDENTITY)//멤버필드에 autoincrement 주는 것
    private Long iuser;

    @Column(length = 10, name = "provider_type", nullable = false)
    //columnDefinition : 해당 컬럼에 코멘트를 남기는 것, name : 실제 db에 있는 이름과 다를 때, nullable = false : not null
    private ProviderTypeEnum providerType;

    @Column(unique = true, length = 100, nullable = false)
    private String uid;

    @Column(length = 100, nullable = false)
    private String upw;

    @Column(length = 20, nullable = false)
    private String nm;

    @Column(length = 2100, nullable = true)
    private String pic;

    @Column(length = 2100, name = "firebase_token")
    private ProviderTypeEnum firebaseToken;
}
