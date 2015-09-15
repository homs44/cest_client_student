package kr.devy.cest.db;

/**
 * Created by pc on 2015-08-03.
 */
public class AttendanceResult {
    String attendance_time;
    String uuid;
    int result;
    int delay;

    public String getAttendance_time() {
        return attendance_time;
    }

    public void setAttendance_time(String attendance_time) {
        this.attendance_time = attendance_time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "AttendanceResult{" +
                "attendance_time='" + attendance_time + '\'' +
                ", uuid='" + uuid + '\'' +
                ", result=" + result +
                ", delay=" + delay +
                '}';
    }
}
