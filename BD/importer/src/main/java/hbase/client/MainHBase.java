package hbase.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.file.tfile.TFile.Reader.Scanner;

import com.sun.org.apache.xml.internal.utils.StringComparable;
import com.sun.tools.internal.xjc.model.SymbolSpace;

public class MainHBase {

	// http://www.codeproject.com/Articles/1024851/Apache-HBase-Example-Using-Java

	/**
	 * Ermittelt die PLZ (oder mehrere) die zur eingegebene Stadt gehoeren.
	 * 
	 * @param city
	 * @return Liste von PLZ
	 */
	static List<String> getPlzByCity(String city) {
		// Connection connection = connect();
		// if(connection != null){

		// HTableDescriptor tableD = new
		// HTableDescriptor(TableName.valueOf("bigdata"));

		Table table;
		try (Connection connection = ConnectionFactory
				.createConnection(getConf());
				Admin admin = connection.getAdmin()) {
			System.out.println("Connection aufgebaut");
			table = connection.getTable(TableName.valueOf("bigdata"));

			System.out.println(table.getName());
			Scan scan = new Scan();
			scan.addColumn(Bytes.toBytes("plz"), Bytes.toBytes("city"));
			
			scan.setFilter(new ValueFilter(CompareOp.EQUAL, new BinaryComparator( Bytes.toBytes(city))));
			ResultScanner scanner = table.getScanner(scan);
			for (Result result = scanner.next(); (result != null); result = scanner
					.next()) {
				// if you want to get the entire row
				Get get = new Get(result.getRow());
				Result entireRow = table.get(get);			
				String key = Bytes.toString(result.getRow());
				System.out.println("plz:" + key);
			
			}
			
			table.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }

		return null;
	}

	/**
	 * Ermittelt die Stadt und den Staat die zur eingegebenen PLZ gehoeren.
	 * 
	 * @param plz
	 * @return Liste bestehend aus Stadt und Staat
	 */
	static String getCityAndStateByPlz(String plz) {
	
		try {
			HTable table = new HTable(getConf(), "bigdata");

			Get g = new Get(Bytes.toBytes(plz));
			Result result = table.get(g);
			byte[] city = result.getValue(Bytes.toBytes("plz"),
					Bytes.toBytes("city"));
			byte[] state = result.getValue(Bytes.toBytes("plz"),
					Bytes.toBytes("state"));
			return "city: " + Bytes.toString(city) + " state:"
					+ Bytes.toString(state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}

	private static Configuration getConf() {
		Configuration config = HBaseConfiguration.create();
		// config.setInt("timeout", 3);
		config.set("hbase.master", "localhost:16000");
		config.set("hbase.zookeeper.quorum", "localhost");
		config.set("hbase.zookeeper.property.clientPort", "2222");
		return config;
	}

	private static Connection connect() {
		Configuration config = HBaseConfiguration.create();
		// config.setInt("timeout", 3);
		config.set("hbase.master", "localhost:16000");
		config.set("hbase.zookeeper.quorum", "localhost");
		config.set("hbase.zookeeper.property.clientPort", "2222");
		try (Connection connection = ConnectionFactory.createConnection(config)) {
			System.out.println("Connection aufgebaut");
			return connection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		boolean inside = true;
		BufferedReader buf = new BufferedReader(
				new InputStreamReader(System.in));
		int input_val;
		String input_str = "";
		while (inside) {
			try {
				System.out
						.println("Nach Stadt (0) oder PLZ (1) suchen? Oder Abbrechen (-1)? ");
				input_val = buf.read();
				// Zeilenende auslesen, da diese sonst als naechste Eingabe
				// gewertet wird
				buf.readLine();
				input_val = Character.getNumericValue(input_val);
				switch (input_val) {
				case 0:
					System.out.print("Bitte den Namen der Stadt eingeben: ");
					input_str = buf.readLine().toUpperCase();
					getPlzByCity(input_str);
					break;
				case 1:
					System.out.print("Bitte die PLZ der Stadt eingeben: ");
					input_str = buf.readLine().toUpperCase();
					System.out.println("Stadt zur PLZ " + input_str
							+ " lautet: " + getCityAndStateByPlz(input_str));
					break;
				case -1:
					inside = false;
					buf.close();

					System.out.println("ByeBye!");
					break;
				default:
					System.out.println("Falsche Eingabe: " + input_val);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
