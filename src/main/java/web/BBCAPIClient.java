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
package web;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.jsoup.Jsoup;

import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

/**
 * @author debmalyajash
 *
 */
public class BBCAPIClient {

	private static final JsonParser parser = new JsonParser();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public static void getBBCRadioPrograms(final String URL) throws IOException {
		String json = Jsoup.connect(URL).ignoreContentType(true).execute().body();
		if (json != null) {
			JSONArray radioPrograms = (JSONArray) JsonPath.read(json, "$.schedule.day.broadcasts[*].programme");
			for (int i = 0; i < radioPrograms.size(); i++) {
				LinkedHashMap<String,String> eachProgrma = (LinkedHashMap<String,String>) radioPrograms.get(i);
				String synopsis = eachProgrma.get("short_synopsis");
				String firstBroadCastDay = eachProgrma.get("first_broadcast_date");
				System.out.println((i+1) + " " + firstBroadCastDay + " synopsis :" + synopsis);
			}
		}

	}
}
