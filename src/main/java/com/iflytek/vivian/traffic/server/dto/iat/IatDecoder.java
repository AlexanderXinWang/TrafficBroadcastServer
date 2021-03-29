package com.iflytek.vivian.traffic.server.dto.iat;

/**
 * @ClassName IatDecoder
 * @Description TODO
 * @Author xinwang41
 * @Date 2021/3/29 11:38
 **/
public class IatDecoder {
    private IatText[] texts;
    private int defc = 10;
    public IatDecoder() {
        this.texts = new IatText[this.defc];
    }
    public synchronized void decode(IatText text) {
        if (text.sn >= this.defc) {
            this.resize();
        }
        if ("rpl".equals(text.pgs)) {
            for (int i = text.rg[0]; i <= text.rg[1]; i++) {
                this.texts[i].deleted = true;
            }
        }
        this.texts[text.sn] = text;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IatText t : this.texts) {
            if (t != null && !t.deleted) {
                sb.append(t.text);
            }
        }
        return sb.toString();
    }
    public void resize() {
        int oc = this.defc;
        this.defc <<= 1;
        IatText[] old = this.texts;
        this.texts = new IatText[this.defc];
        for (int i = 0; i < oc; i++) {
            this.texts[i] = old[i];
        }
    }
    public void discard(){
        for(int i=0;i<this.texts.length;i++){
            this.texts[i]= null;
        }
    }
}

