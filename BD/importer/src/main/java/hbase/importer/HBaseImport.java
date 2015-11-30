package hbase.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Hello world!
 *
 * scan 'bigdata', { COLUMNS => 'plz:city', LIMIT => 10, FILTER =>
 * "ValueFilter( =, 'binaryprefix:HAMBURG' )" }
 *
 * 
 */
public class HBaseImport {
	public static void main(String[] args) {
		Configuration config = HBaseConfiguration.create();
		// config.setInt("timeout", 3);
		config.set("hbase.master", "localhost:16000");
		config.set("hbase.zookeeper.quorum", "localhost");
		config.set("hbase.zookeeper.property.clientPort", "2222");
		System.out.println("aa");
		try (Connection connection = ConnectionFactory.createConnection(config); Admin admin = connection.getAdmin()) {
			System.out.println("bbb");
			for (TableName item : admin.listTableNames()) {
				System.out.println(item.getNameAsString());
			}
			HTableDescriptor tableD = new HTableDescriptor(TableName.valueOf("bigdata"));

			tableD.addFamily(new HColumnDescriptor("plz"));

			if (!admin.tableExists(tableD.getTableName())) {
				System.out.print("Creating table. ");
				admin.createTable(tableD);
				System.out.println(" Done.");
			}

			Table table = connection.getTable(TableName.valueOf("bigdata"));

			JSONParser parser = new JSONParser();
			BufferedReader b = new BufferedReader(new FileReader("src/data/plz.data"));
			long lineCounter = 0;
			String line = b.readLine();
			do {
				try {
					JSONObject o = (JSONObject) parser.parse(line);
					PLZStructure s = new PLZStructure();
					s.setId((String) o.get("_id"));
					s.setCity((String) o.get("city"));
					s.setLoc((JSONArray) o.get("loc"));
					s.setPop(((Long) o.get("pop")).toString());
					s.setState((String) o.get("state"));

					Put p = new Put(Bytes.toBytes(s.getId()));
					p.addColumn(Bytes.toBytes("plz"), Bytes.toBytes("city"), Bytes.toBytes(s.getCity()));
					p.addColumn(Bytes.toBytes("plz"), Bytes.toBytes("loc"), Bytes.toBytes(s.getLoc().toJSONString()));
					p.addColumn(Bytes.toBytes("plz"), Bytes.toBytes("pop"), Bytes.toBytes(s.getPop()));
					p.addColumn(Bytes.toBytes("plz"), Bytes.toBytes("state"), Bytes.toBytes(s.getState()));
					table.put(p);

					if (++lineCounter % 50 == 0)
						System.out.println(lineCounter + " Zeilen hinzugefuegt. ");
				} catch (Exception e) {
					System.out.println("Zeile die einen Fehler geschmissen hat: " + line);
					e.printStackTrace();
				}
				line = b.readLine();
			} while (line != null);

			b.close();
			System.out.println("Der Import ist fertig");

			table.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
