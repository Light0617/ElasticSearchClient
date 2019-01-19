import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.Date;

public class TestBase {
    protected XContentBuilder getExampleObject() throws IOException {
        XContentBuilder builderObject = XContentFactory.jsonBuilder()
                .startObject()
                .field("fullName", "Test")
                .field("dateOfBirth", new Date())
                .field("age", "10")
                .endObject();
        return builderObject;
    }

    protected XContentBuilder getExampleObject(String... fields) throws IOException {
        XContentBuilder builderObject = XContentFactory.jsonBuilder()
                .startObject()
                .field("fullName", fields[0])
                .field("dateOfBirth", new Date())
                .field("age", fields[1])
                .endObject();
        return builderObject;
    }

}
