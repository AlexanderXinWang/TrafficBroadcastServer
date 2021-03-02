package com.iflytek.vivian.traffic.server.dto.face;

import lombok.Data;

/**
 * @ClassName FdbImage
 * @Author xinwang41
 * @Date 2021/3/2 9:33
 **/

@Data
public class FdbImage {
    String name;
    String imageId;
    double qualityScore;
    String feature;
}
