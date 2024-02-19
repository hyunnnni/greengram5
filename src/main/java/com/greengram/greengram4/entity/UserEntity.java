package com.greengram.greengram4.entity;

import com.greengram.greengram4.common.ProviderTypeEnum;
import com.greengram.greengram4.common.RoleEunm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.parameters.P;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity//무조건 pk를 적용해주어야 빨간줄이 안 뜬다
@Table(name = "t_user",uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"provider_type", "uid"}
        )
})//복합 유니크를 줄 때 방법
//그냥 유니크는 같은 내용이 한 컬럼에 2개이상 들어가지 않는다
//복합 유니크는 두 가지 컬럼을 합쳤을 때 같은 내용만 아니면 된다 복합키와 같다
//복합키는 방식이 두가지 객체지향방식, rdb방식 쿼리를 적을 시 길이와 성능이 다르다는 차이점이 있다.
//@Embeddable 객제지향방식
public class UserEntity extends BaseEntity{
    @Id//멤버필드에 pk를 주는 것
    @Column(columnDefinition = "BIGINT UNSIGNED")//부호도 넣을 수 있음 오라클은 자동으로 넣기 가능?
    @GeneratedValue(strategy = GenerationType.IDENTITY)//멤버필드에 autoincrement 주는 것
    private Long iuser;

    @Column(length = 10, name = "provider_type", nullable = false)
    //columnDefinition : 해당 컬럼에 코멘트를 남기는 것, name : 실제 db에 있는 이름과 다를 때, nullable = false : not null
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'LOCAL'")//컬럼에 디폴트값 주기
    private ProviderTypeEnum providerType;

    @Column(length = 100, nullable = false)
    private String uid;

    @Column(length = 100, nullable = false)
    private String upw;

    @Column(length = 20, nullable = false)
    private String nm;

    @Column(length = 2100, nullable = true)
    private String pic;

    @Column(length = 2100, name = "firebase_token")
    private String firebaseToken;

    @Column
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'USER'")
    private RoleEunm role;
}
