package com.jainamjhaveri.rasppiclient.Utils;

public class DataPoint
{
    private float point;
    private long time;

    public DataPoint(float point, long time) {
        this.point = point;
        this.time = time;
    }

    public float getPoint() {
        return point;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return point + ", "+time;
    }
}
