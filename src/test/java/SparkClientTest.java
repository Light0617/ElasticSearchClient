import com.sun.rowset.internal.Row;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.junit.Test;

import org.apache.spark;
import org.apache.spark.sql.Dataset;

public class SparkClientTest {
    @Test
    public void test() {
        String topic = "yelpBusiness";
        SparkConf conf = new SparkConf().setAppName("Data Transformation")
                .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
                .setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);

        SQLContext sqlContext = new org.apache.spark.sql.SQLContext(sc);

        Dataset<Row> df = sqlContext
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
                .option("subscribePattern", "topic.*")
                .load();
        df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

    }
}