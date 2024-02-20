package com.greengram.greengram4.feed;

import com.greengram.greengram4.entity.FeedFavEntity;
import com.greengram.greengram4.entity.FeedFavIds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedFavRepository extends JpaRepository<FeedFavEntity, FeedFavIds> {

}
