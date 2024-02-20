package com.greengram.greengram4.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
@Data
@Embeddable
@EqualsAndHashCode
public class FeedFavIds implements Serializable {

    private Long ifeed;
    private Long iuser;
}
