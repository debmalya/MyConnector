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
import java.sql.DriverManager;
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
public class DBConnectionUtil {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		if ((args == null) || (args.length < 4)) {
			System.out.println("Usage: java -jar DBConnectionUtil dburl username password query");
			return;
		}
		String url = args[0];
		String userName = args[1];
		String password = args[2];

		executeQuery(url, userName, password, args[3]);

	}

	/**
	 * @param args
	 * @param url
	 * @param userName
	 * @param password
	 * @throws SQLException 
	 */
	public static void executeQuery(String url, String userName, String password, String query) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		if (url == null || userName == null || password == null || query == null) {
			throw new IllegalArgumentException("Some of the arguments are null. Please check.");
		}
		
		try {
			String appendSSLinputURL = url + "?autoReconnect=true&useSSL=false";
			conn = DriverManager.getConnection(appendSSLinputURL, userName, password);
			conn.setAutoCommit(false);

			if (conn != null) {
				boolean flag = false;

				try {
					st = conn.createStatement();
					st.setFetchSize(50);
					// execute the query, and get a java resultset
					rs = st.executeQuery(query);
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
		} catch (SQLException ex) {
			System.out.println("ERRor occurred. " + ex.getMessage());
			throw ex;
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.err.println("ERRor occurred while closing result set " + e.getMessage());
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					System.err.println("ERRor occurred while closing statement " + e.getMessage());
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.err.println("ERRor occurred while closing connection " + e.getMessage());
				}
			}
		}
	}

}
