package com.example.atapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Worker implements Parcelable {

    public static final String TABLE_NAME = "workers";
    public static final String SQLITE_ID = "id";
    public static final String WORKER_ID = "workerId";
    public static final String NAME = "name";
    public static final String ENTER_EXIT = "enterExit";
    public static final String TIME = "time";

    private int sqliteId;
    private String name;
    private int workerId;
    private String enterExit;
    private String time;

    public Worker() { }

    public int getSqliteId() {
        return sqliteId;
    }

    public void setSqliteId(int sqliteId) {
        this.sqliteId = sqliteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getEnterExit() {
        return enterExit;
    }

    public void setEnterExit(String enterExit) {
        this.enterExit = enterExit;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "sqliteId=" + sqliteId +
                ", name='" + name + '\'' +
                ", workerId=" + workerId +
                ", enterExit='" + enterExit + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sqliteId);
        dest.writeString(name);
        dest.writeInt(workerId);
    }

    public static final Creator<Worker> CREATOR = new Creator<Worker>() {
        public Worker createFromParcel(Parcel pc) {
            return new Worker(pc);
        }
        public Worker[] newArray(int size) {
            return new Worker[size];
        }
    };

    private Worker(Parcel pc){
        sqliteId = pc.readInt();
        name = pc.readString();
        workerId = pc.readInt();
    }
}