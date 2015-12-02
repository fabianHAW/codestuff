package hbase.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.file.tfile.TFile.Reader.Scanner;

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
		Configuration config = HBaseConfiguration.create();
		// config.setInt("timeout", 3);
		config.set("hbase.master", "localhost:16000");
		config.set("hbase.zookeeper.quorum", "localhost");
		config.set("hbase.zookeeper.property.clientPort", "2222");

		// HTableDescriptor tableD = new
		// HTableDescriptor(TableName.valueOf("bigdata"));

		Table table;
		try (Connection connection = ConnectionFactory.createConnection(config); Admin admin = connection.getAdmin()) {
			System.out.println("Connection aufgebaut");
			table = connection.getTable(TableName.valueOf("bigdata"));

			System.out.println(table.getName());
			Scan scan = new Scan();
			System.out.println("1");
			scan.setStartRow(Bytes.toBytes("71646"));
			System.out.println("2");
			scan.setCaching(3);
			System.out.println("3");
			ResultScanner results = table.getScanner(scan);
			System.out.println("4");
			for (Result r : results) {
				System.out.println(new String(r.getRow()));
				// scanner.ProcessRow(r);
				// if ( count++ >= 3 ) break;
			}
			// for(byte[] item : table.getTableDescriptor().getFamiliesKeys()){
			// System.out.println(new String(item));
			// }
			// ResultScanner scan = table.getScanner(Bytes.toBytes("plz"),
			// Bytes.toBytes("city"));
			//
			// for (Result item : scan){
			// System.out.println(new String(item.getRow()));
			// }
			// scan.close();

			// Scan scan = new Scan();
			// System.out.println("inside");
			// scan.addColumn(Bytes.toBytes("plz"), Bytes.toBytes("city"));
			// scan.addFamily(Bytes.toBytes("plz:city"));

			// System.out.println(scan.getMaxResultSize());
			// ResultScanner scanner = table.getScanner(scan);
			// for (Result item : scanner) {
			// System.out.println(Bytes.toString(item.getRow()));
			// }

			// Get g = new Get(Bytes.toBytes("07419:1"));
			// System.out.println("inside");
			// System.out.println(g.getMaxResultsPerColumnFamily());
			// Result result = table.get(g);
			// System.out.println("inside");
			// System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("plz"),
			// Bytes.toBytes("city"))));

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
	static List<String> getCityAndStateByPlz(String plz) {
		Connection connection = connect();
		if (connection != null) {

		}
		return null;
	}

	private static Connection connect() {
		Configuration config = HBaseConfiguration.create();
		// config.setInt("timeout", 3);
		config.set("hbase.master", "localhost:16000");
		config.set("hbase.zookeeper.quorum", "localhost");
		config.set("hbase.zookeeper.property.clientPort", "2222");
		try (Connection connection = ConnectionFactory.createConnection(config); Admin admin = connection.getAdmin()) {
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
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		int input_val;
		String input_str = "";
		while (inside) {
			try {
				System.out.println("Nach Stadt (0) oder PLZ (1) suchen? Oder Abbrechen (-1)? ");
				input_val = buf.read();
				// Zeilenende auslesen, da diese sonst als naechste Eingabe
				// gewertet wird
				buf.readLine();
				input_val = Character.getNumericValue(input_val);
				switch (input_val) {
				case 0:
					System.out.print("Bitte den Namen der Stadt eingeben: ");
					input_str = buf.readLine().toUpperCase();
					System.out.println("PLZ zu " + input_str + " lautet: " + getPlzByCity(input_str));
					break;
				case 1:
					System.out.print("Bitte die PLZ der Stadt eingeben: ");
					input_str = buf.readLine().toUpperCase();
					System.out.println("Stadt zur PLZ " + input_str + " lautet: " + getCityAndStateByPlz(input_str));
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
