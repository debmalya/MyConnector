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
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * @author debmalyajash
 *
 */
public class DataSource {
	
	private BasicDataSource dataSource;

	/**
	 * Creates data source for the specified driver class.
	 * @param driverClass
	 * @param userName - database user name.
	 * @param password - database user password.
	 * @param url - to connect
	 */
	public DataSource(final String driverClass,final String userName,final String password,final String url) {
		dataSource = new BasicDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driverClass);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
	}
	
	public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
