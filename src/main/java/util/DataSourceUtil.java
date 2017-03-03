/**
 * Copyright 2015-2016 Debmalya Jash
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jakewharton.fliptables.FlipTableConverters;

/**
 * @author debmalyajash
 *
 */
public class DataSourceUtil {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		if ((args == null) || (args.length < 5)) {
			System.out.println("Usage: java -jar DataSourceUtil.jar driverclassname dburl username password query");
			return;
		} 
		
       DataSource dataSource = new DataSource(args[0], args[2], args[3], args[1]);
       executeQuery(dataSource.getConnection(),args[4]);
	}

	/**
	 * @param connection
	 * @param queryString
	 */
	public static void executeQuery(Connection connection, String queryString) {
		
		Statement st = null;
		ResultSet rs = null;
		
		if (connection != null) {
			boolean flag = false;

			try {
				st = connection.createStatement();
				st.setFetchSize(50);
				// execute the query, and get a java resultset
				rs = st.executeQuery(queryString);
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				String[] headers = new String[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					headers[i - 1] = metaData.getColumnLabel(i);
				}
				List<String[]> rowList = new ArrayList<String[]>();
				
				
				while (rs.next()) {
					flag = true;
					String[] values = new String[columnCount];
					for (int i = 1; i <= columnCount; i++) {
						values[i - 1] = rs.getString(headers[i - 1]);
					}
					rowList.add(values);
				}
				String[][] rows = new String[rowList.size()][columnCount];
				for (int i = 0; i < rowList.size(); i++) {
					rows[i] = rowList.get(i);
				}

				
				if (!flag) {
					System.out.println("no data availble for this query");
				} else {
					System.out.println(FlipTableConverters.fromObjects(headers, rows));
				}

			} catch (Exception e) {
				System.out.println("Wrong input for database query , " + e.getMessage());

			}

		}
	}

}
