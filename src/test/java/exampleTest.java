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

public class exampleTest {
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
        IndexResponse response = client.prepareIndex("people", "Doe")
                .setSource(builderObject).get();

        String id = response.getId();
        String index = response.getIndex();
        String type = response.getType();
        long version = response.getVersion();

        assertEquals(Result.CREATED, response.getResult());
        assertEquals(0, version);
        assertEquals("people", index);
        assertEquals("Doe", type);
    }

    @After
    public void clearDown() {
        client.close();
    }

    private XContentBuilder getExampleObject() throws IOException {
        XContentBuilder builderObject = XContentFactory.jsonBuilder()
                .startObject()
                .field("fullName", "Test")
                .field("dateOfBirth", new Date())
                .field("age", "10")
                .endObject();
        return builderObject;
    }
}
