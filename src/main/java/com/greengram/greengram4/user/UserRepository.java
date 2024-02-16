package com.greengram.greengram4.user;

import com.greengram.greengram4.common.ProviderTypeEnum;
import com.greengram.greengram4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {//<해당 테이블, 그 테이블의 pk 타입>
    Optional<UserEntity> findByProviderTypeAndUid(ProviderTypeEnum providerType, String uid);
    //By 다음에 오는 이름이 where절에 오는 컬럼명 그래서 각 컬럼에 이 값이 들어가게 된다
    //select문 find all(리스트) by(where절)
}
