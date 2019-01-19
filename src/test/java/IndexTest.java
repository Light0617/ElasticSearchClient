import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class IndexTest extends TestBase {
    TransportClient client;
    @Before
    public void setUp() throws Exception {
        client = new PreBuiltTransportClient(
                Settings
                        .builder().put("client.transport.sniff", false)
                        .put("cluster.name","docker-cluster").build())
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    @Test
    public void simpleTest() throws IOException {

        XContentBuilder builderObject = getExampleObject();
        IndexResponse response = client.prepareIndex("member", "Doe")
                .setSource(builderObject).get();

        String id = response.getId();
        String index = response.getIndex();
        String type = response.getType();
        long version = response.getVersion();

        assertEquals(Result.CREATED, response.getResult());
        assertEquals(1, version);
        assertEquals("member", index);
        assertEquals("Doe", type);
    }

    @After
    public void clearDown() {
        client.close();
    }

}
