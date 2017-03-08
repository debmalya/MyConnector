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
package JOOQ;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.impl.DSL;

import util.DataSource;

/**
 * @author debmalyajash
 *
 */
public class JOOQUtil {
	/**
	 * 
	 * @param databseDriver
	 * @param databaseUserName
	 * @param databsePassword
	 * @param databaseURL
	 * @param query
	 * @throws SQLException
	 * 
	 * https://www.jooq.org/java-8-and-sql
	 * https://github.com/jOOQ/jOOQ-s-Java-8-Goodies
	 * https://github.com/jOOQ/jOOL
	 * 
	 */
	public void streamResult(final String databseDriver, final String databaseUserName, final String databsePassword,
			final String databaseURL, final String query) throws SQLException {
		DataSource dataSource = new DataSource(databseDriver, databaseUserName, databsePassword, databaseURL);
		if (dataSource != null) {
			try (Connection dataSourceConnection = dataSource.getConnection()) {
				if (dataSourceConnection != null) {
					String[] queries = query.split(",");
					for (String eachQuery : queries) {
						DSL.using(dataSourceConnection).fetch(eachQuery).map(rs -> {
							int columnCount = rs.fieldsRow().size();
							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < columnCount; i++) {
								sb.append(rs.get(i));
								sb.append(",");
							}
							return sb.toString();
						}).forEach(System.out::println);

					}
				}
			}
		}
		
	}
}
