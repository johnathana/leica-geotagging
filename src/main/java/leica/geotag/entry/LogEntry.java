package leica.geotag.entry;

import java.util.Date;

/**
 * Log entry interface.
 */
public interface LogEntry {

    int TIME_INDEX = 0;
    int LAT_INDEX = 4;
    int LON_INDEX = 8;
    int ALT_INDEX = 12;

    long TIMESTAMP_MAGIC =
            Date.UTC(80, 1, 6, 0, 0, 0) -
            Date.UTC(70, 1, 1, 0, 0, 0);

    byte[] getBytes();
}
