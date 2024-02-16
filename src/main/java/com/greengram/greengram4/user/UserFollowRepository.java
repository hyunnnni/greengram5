package com.greengram.greengram4.user;

import com.greengram.greengram4.entity.UserFollowEntity;
import com.greengram.greengram4.entity.UserFollowIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollowEntity, UserFollowIds> {
}
