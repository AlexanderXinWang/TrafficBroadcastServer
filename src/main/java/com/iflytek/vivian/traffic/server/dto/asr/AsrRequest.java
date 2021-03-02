package com.iflytek.vivian.traffic.server.dto.asr;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AsrRequest
 * @Author xinwang41
 * @Date 2021/3/2 9:29
 **/

@Data
public class AsrRequest {
    /**
     * 业务编号
     */
    private String id;

    private List<AsrAudio> audios;

    /**
     * 个性化声学资源Id（为0，使用默认声学资源
     */

    private int resId;

    /**
     * 解码方式,RecPass1 = 0, 二遍解码 RecPass2 = 1, Lattice CN Lattice = 2
     */
    private int type;

    /**
     * 扩展属性
     */
    private String tags;

}
