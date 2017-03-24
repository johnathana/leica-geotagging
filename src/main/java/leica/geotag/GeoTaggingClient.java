package leica.geotag;

import leica.geotag.entry.LogEntry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Geo tagging client.
 */
public class GeoTaggingClient {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssZ");

    private String cameraURL;


    public GeoTaggingClient(String cameraIP) {
        this.cameraURL = String.format("http://%s", cameraIP);
    }

    public void sendGpsEntries(List<LogEntry> gpsLogEntries) {
        try {
            byte[] payload = getPayload(gpsLogEntries);
            getRequest("/cam.cgi?mode=notify&type=clock&value=" + formatter.format(new Date()));
            getRequest(String.format("/cam.cgi?mode=startsenddata&type=geodata&value=%d&value2=1", gpsLogEntries.size()));
            postGeoData(payload);
            getRequest("/cam.cgi?mode=camctrl&type=add_location_data&value=start");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] getPayload(List<LogEntry> gpsLogEntries) throws IOException, InterruptedException {
        int size = gpsLogEntries.size();

        byte[] header = new byte[8];
        header[0] = 1;
        header[1] = 0;
        header[2] = 0;
        header[3] = 0;
        header[4] = (byte) size;
        header[5] = (byte) (size >> 8);
        header[6] = (byte) (size >> 16);
        header[7] = (byte) (size >> 24);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(header);

        for (LogEntry entry : gpsLogEntries) {
            out.write(entry.getBytes());
        }
        return out.toByteArray();
    }

    private void postGeoData(byte[] payload) throws IOException {
        HttpClient httpClient = getHttpClient();

        HttpPost httpPost = new HttpPost(cameraURL + "/cam.cgi?mode=senddata&value2=1");
        FormBodyPart bodyPart = FormBodyPartBuilder.create()
                .setName("filename")
                .setBody(new ByteArrayBody(payload, ContentType.APPLICATION_OCTET_STREAM, "bindata.bin"))
                .addField("Content-Length", String.valueOf(payload.length))
                .build();

        HttpEntity multipart = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.STRICT)
                .addPart(bodyPart)
                .build();

        httpPost.setEntity(multipart);
        httpClient.execute(httpPost);
    }

    private HttpClient getHttpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);
        params.setParameter("http.useragent", "Apache-HttpClient");

        //enableProxy(httpClient);
        return httpClient;
    }

    private void getRequest(final String endpoint) throws Exception {
        HttpClient httpClient = getHttpClient();
        HttpGet getRequest = new HttpGet();
        getRequest.setURI(new URI(cameraURL + endpoint));
        httpClient.execute(getRequest);
    }

    private void enableProxy(DefaultHttpClient httpclient) {
        HttpHost proxy = new HttpHost("127.0.0.1", 8080);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }
}
