package leica.geotag.entry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * GPS log entry.
 */
public class GpsLogEntry implements LogEntry {

    private long timestamp;

    private double latitude;

    private double longitude;

    private double altitude;


    public GpsLogEntry(double latitude, double longitude, double altitude) {
        this.timestamp = System.currentTimeMillis();
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public GpsLogEntry(long timestamp, double latitude, double longitude, double altitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public GpsLogEntry(byte[] payload) {
        ByteBuffer buf = ByteBuffer.wrap(payload);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        timestamp = buf.getInt(TIME_INDEX) * 1000L + TIMESTAMP_MAGIC;
        latitude = buf.getInt(LAT_INDEX) / 1.0E7D;
        longitude = buf.getInt(LON_INDEX) / 1.0E7D;
        altitude = buf.getShort(ALT_INDEX) / 1.0D;
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[16];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt((int)((timestamp - TIMESTAMP_MAGIC) / 1000L));
        buf.putInt((int) (latitude * 1.0E7D));
        buf.putInt((int) (longitude * 1.0E7D));
        buf.putShort((short) (altitude * 1.0D));
        buf.put((byte) 65);
        buf.put((byte) 0);
        return bytes;
    }

    @Override
    public String toString() {
        return "GpsLogEntry{" +
                "timestamp=" + timestamp +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                '}';
    }
}
