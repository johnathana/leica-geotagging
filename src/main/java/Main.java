import leica.geotag.GeoTaggingClient;
import leica.geotag.entry.GpsLogEntry;
import leica.geotag.entry.LogEntry;
import leica.geotag.entry.VoidLogEntry;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        long start = dateTimeInMillis(LocalDateTime.of(2015, Month.NOVEMBER, 14, 13, 24, 0));
        long stop = dateTimeInMillis(LocalDateTime.of(2015, Month.NOVEMBER, 14, 16, 39, 0));

        List<LogEntry> gpsLogEntries = new ArrayList<LogEntry>() {{
            add(new VoidLogEntry(start));
            for (long timestamp = start; timestamp <= stop; timestamp += 5000) {
                add(new GpsLogEntry(timestamp, 38.0, 24.0, 0));
            }
            add(new VoidLogEntry(stop));
        }};

        GeoTaggingClient geoTagClient = new GeoTaggingClient("192.168.54.1");
        geoTagClient.sendGpsEntries(gpsLogEntries);
    }

    private static long dateTimeInMillis(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.of("UTC");
        return localDateTime.atZone(zone).toInstant().toEpochMilli();
    }
}
