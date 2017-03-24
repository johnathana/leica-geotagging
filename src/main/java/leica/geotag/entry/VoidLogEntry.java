package leica.geotag.entry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Void log entry.
 */
public class VoidLogEntry implements LogEntry {

    private long timestamp;

    public VoidLogEntry() {
        this.timestamp = System.currentTimeMillis();
    }

    public VoidLogEntry(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[16];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt((int)((timestamp - TIMESTAMP_MAGIC) / 1000L));
        buf.putInt(Integer.MAX_VALUE);
        buf.putInt(Integer.MAX_VALUE);
        buf.putShort((short) 32767);
        buf.put((byte) 86);
        buf.put((byte) 0);
        return bytes;
    }

    @Override
    public String toString() {
        return "VoidLogEntry{" +
                "timestamp=" + timestamp +
                '}';
    }
}
