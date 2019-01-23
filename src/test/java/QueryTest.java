import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class QueryTest extends TestBase {
    TransportClient client;
    @Before
    public void setUp() throws Exception {
        client = new PreBuiltTransportClient(
                Settings
                        .builder().put("client.transport.sniff", false)
                        .put("cluster.name","docker-cluster").build())
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        setupNaiveData();
    }

    @Test
    public void queryAllTest() {
        QueryBuilder q =  QueryBuilders.matchAllQuery();
        SearchResponse searchResponse = client.prepareSearch()
                .setQuery(q).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit);
        }
    }

    @Test
    public void queryBoolTest() {
        String expected = "35";
        QueryBuilder q =  QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("age", expected));
        SearchResponse searchResponse = client.prepareSearch()
                .setQuery(q).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit);
        }
    }


    @After
    public void clearUp() {
        client.close();
    }

    private void setupNaiveData() throws Exception {
        XContentBuilder builderObject1 = getExampleObject("Mark", "15");
        XContentBuilder builderObject2 = getExampleObject("Mark", "35");

        client.prepareIndex("member", "Doe")
                .setSource(builderObject1).get();
        client.prepareIndex("member", "Doe")
                .setSource(builderObject2).get();
    }
}
