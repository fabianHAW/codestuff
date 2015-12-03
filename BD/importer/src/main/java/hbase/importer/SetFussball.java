package hbase.importer;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Hello world!
 *
 * scan 'bigdata', { COLUMNS => 'plz:city', LIMIT => 10, FILTER => "ValueFilter( =, 'binaryprefix:HAMBURG' )" }
 *
 *  scan 'bigdata', {COLUMNS => ['Fussball:fussball']}
 */
public class SetFussball {
	public static void main(String[] args) {
		Configuration config = HBaseConfiguration.create();
		// config.setInt("timeout", 3);
		config.set("hbase.master", "localhost:16000");
		config.set("hbase.zookeeper.quorum", "localhost");
		config.set("hbase.zookeeper.property.clientPort", "2222");
		System.out.println("aa");
		try (Connection connection = ConnectionFactory.createConnection(config); Admin admin = connection.getAdmin()) {
			
			Table table = connection.getTable(TableName.valueOf("bigdata"));
			
			Scan scan = new Scan();
			scan.addColumn(Bytes.toBytes("plz"), Bytes.toBytes("city"));
			
			scan.setFilter(new ValueFilter(CompareOp.EQUAL, new RegexStringComparator("(Hamburg)|(Bremen)",Pattern.CASE_INSENSITIVE)));
			ResultScanner scanner = table.getScanner(scan);
			for (Result result = scanner.next(); (result != null); result = scanner
					.next()) {
				// if you want to get the entire row
			
				String key = Bytes.toString(result.getRow());
				System.out.println("plz:" + key);

				Put p = new Put(Bytes.toBytes(key));
				p.addColumn(Bytes.toBytes("Fussball"), Bytes.toBytes("fussball"), Bytes.toBytes("ja"));
			
				table.put(p);
			
			}
			
			table.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
