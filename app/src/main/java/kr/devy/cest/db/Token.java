package kr.devy.cest.db;

/**
 * Created by pc on 2015-08-01.
 */
public class Token {
    int id;
    String uuid;
    int c_class_id;
    String attendacne_time;
    String valid_time;
    String receive_time;
    int done;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getC_class_id() {
        return c_class_id;
    }

    public void setC_class_id(int c_class_id) {
        this.c_class_id = c_class_id;
    }

    public String getAttendacne_time() {
        return attendacne_time;
    }

    public void setAttendacne_time(String attendacne_time) {
        this.attendacne_time = attendacne_time;
    }

    public String getValid_time() {
        return valid_time;
    }

    public void setValid_time(String valid_time) {
        this.valid_time = valid_time;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", c_class_id=" + c_class_id +
                ", attendacne_time='" + attendacne_time + '\'' +
                ", valid_time='" + valid_time + '\'' +
                ", receive_time='" + receive_time + '\'' +
                ", done=" + done +
                ", name='" + name + '\'' +
                '}';
    }
}
