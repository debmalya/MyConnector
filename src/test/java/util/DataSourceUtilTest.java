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

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author debmalyajash
 *
 */
public class DataSourceUtilTest {

	static DataSource postgreSQLDataSource = null;
	static Connection postgreSQLDataSourceConnection = null;
	static Properties properties = null;
	static String[] queries = null;

	@BeforeClass
	public static void setUp() {
		properties = new Properties();
		InputStream propertiesStream = null;
		try {
			propertiesStream = ClassLoader.getSystemResource("config.properties").openStream();
			properties.load(propertiesStream);
			postgreSQLDataSource = new DataSource(properties.getProperty("database.driver"),
					properties.getProperty("database.username"), properties.getProperty("datbase.password"),
					properties.getProperty("database.url"));
			try {
				postgreSQLDataSourceConnection = postgreSQLDataSource.getConnection();
				String query = properties.getProperty("database.query");
				if (query != null) {
					queries = query.split(",");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (propertiesStream != null) {
				try {
					propertiesStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Test method for
	 * {@link util.DataSourceUtil#executeQuery(java.sql.Connection, java.lang.String)}
	 * .
	 */
//	@Test
	public void testExecuteQueryMySQL() {
		DataSource mySQLDataSource = new DataSource("com.mysql.jdbc.Driver", "root", "passw0rd",
				"jdbc:mysql://localhost/mysql");
		Assert.assertNotNull(postgreSQLDataSource);
		try {
			Connection mySqlConnection = mySQLDataSource.getConnection();
			Assert.assertNotNull(mySqlConnection);
			DataSourceUtil.executeQuery(mySqlConnection, "select * from db");
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Test method for
	 * {@link util.DataSourceUtil#executeQuery(java.sql.Connection, java.lang.String)}
	 * .
	 */
	@Test
	public void testExecuteQueryPostgreSQL() {

		Assert.assertNotNull(postgreSQLDataSource);
		try {
			Connection postgreSqlConnection = postgreSQLDataSource.getConnection();
			Assert.assertNotNull(postgreSqlConnection);
			for (String eachQuery : queries) {
				DataSourceUtil.executeQuery(postgreSqlConnection, eachQuery);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.assertFalse(e.getMessage(), true);
		}
	}

}
