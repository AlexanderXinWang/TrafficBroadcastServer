package com.iflytek.vivian.traffic.server.dto.asr;

import lombok.Data;

/**
 * @ClassName AsrAudio
 * @Author xinwang41
 * @Date 2021/3/2 9:19
 * @Descripion：声音内容
 */

@Data
public class AsrAudio {
    /**
     * 语音资源ID
     */
    private String aid;
    /**
     * 语音链接地址, http, smb, local
     */
    private String uri;

    /**
     * 语音信道,如立体音为：2
     */
    private int chnl;

    /**
     * 说话人数量
     */
    private int spkn;

    /**
     * 语音的编码格式,目前支持的语音格式为：
     * Unknown(0),pcm(1);ALaw(6);G729(162);AMR_NB(177);
     * GSM_HR(178);GSMEFR(179);SILK(180)
     */
    private int encoding;

    /**
     * 采样精度,如 16bit
     */
    private int bits;

    /**
     * 采样频率,如8000K
     */
    private int rate;

    /**
     * 偏移量。如果是有效值，就做静音处理。应用场景： 过滤 DTMF按键片段
     */
    private int offset;
}
