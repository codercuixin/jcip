package net.jcip.examples.chapter10;

import java.util.Objects;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/4 14:19
 */
public class HeatInfoItem {
    /**
     * 热度值
     */
    private double heat;

    /**
     * 按照优先级排序，目前只有公告需要这个。
     */
    private double priority;
    /**
     * 资讯id
     */
    private String infoCode;
    /**
     * 是否已经删除
     */
    private boolean deleted;



    /**
     * 新复制一个新的对象，防止对资讯Item进行修改的时候
     *
     * @return 新的对象
     */
    public HeatInfoItem copy() {
        HeatInfoItem infoItem = new HeatInfoItem();
        infoItem.setInfoCode(this.infoCode);
        infoItem.setDeleted(this.deleted);
        infoItem.setHeat(this.heat);
        infoItem.setPriority(this.priority);
        return infoItem;
    }

    @Override
    public String toString() {
        return "HeatInfoItem{" +
                "heat=" + heat +
                ", priority=" + priority +
                ", infoCode='" + infoCode + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeatInfoItem that = (HeatInfoItem) o;
        return Objects.equals(infoCode, that.infoCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(infoCode);
    }


    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode;
    }



    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getHeat() {
        return heat;
    }

    public void setHeat(double heat) {
        this.heat = heat;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }
}
