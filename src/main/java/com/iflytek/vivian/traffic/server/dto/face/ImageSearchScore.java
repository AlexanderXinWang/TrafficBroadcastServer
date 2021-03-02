package com.iflytek.vivian.traffic.server.dto.face;

import lombok.Data;

/**
 * @ClassName ImageSearchScore
 * @Author xinwang41
 * @Date 2021/3/2 9:35
 **/

@Data
public class ImageSearchScore {
    private String imageId;
    private double score;
    private String props;
}
