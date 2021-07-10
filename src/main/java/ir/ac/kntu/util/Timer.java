package ir.ac.kntu.util;

public class Timer {
    private int second;
    private int minute;
    private int hour;

    public Timer(int second, int minute, int hour) {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
    }

    public void next() {
        second++;
        if (second >= 60) {
            second = 0;
            minute++;
        }
        if (minute >= 60) {
            minute = 0;
            hour++;
        }
    }

    public int getValue() {
        return second + (minute * 60) + (hour * 3600);
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
