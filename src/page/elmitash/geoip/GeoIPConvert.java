package page.elmitash.geoip;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * ftp://ftp.apnic.net/pub/stats/apnic/delegated-apnic-extended-latest
 * file convert to csv format(db-ip.com)
 *
 * @author elmitash
 *
 */
public class GeoIPConvert {
	public static void main(String args[]) throws Exception {
		Path path = Paths.get("input/delegated-apnic-extended-latest");
		Path writeFilePath = Paths.get("output/dbip-country-lite.csv");
		try (Stream<String> stream = Files.lines(path);
				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(writeFilePath.toFile(), false)))) {
			List<CountryIPRangeDto> dtoList = new ArrayList<>();
			stream.forEach(line -> {
				try {
					CountryIPRangeDto dto = filtering(line);
					if (dto == null)
						return;
					dtoList.add(dto);
					//	System.out.println(results[0] + " " + results[1] + "-" + results[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			// sort first num asc
			Collections.sort(dtoList, new Comparator<CountryIPRangeDto>() {
				@Override
				public int compare(CountryIPRangeDto dto1, CountryIPRangeDto dto2) {
					return dto1.firstNum - dto2.firstNum > 0 ? 1 : -1;
				}
			});

			// merge sequancial ip
			for (int i = 0; i < dtoList.size(); i++) {
				if (i == 0)
					continue;

				CountryIPRangeDto prevDto = dtoList.get(i - 1);
				CountryIPRangeDto currDto = dtoList.get(i);

				if (prevDto.country.equals(currDto.country) && prevDto.lastNum == currDto.firstNum - 1) {
					prevDto.lastNum = currDto.lastNum;
					prevDto.lastIp = currDto.lastIp;
					dtoList.remove(i);
					i--;
				}
			}

			dtoList.forEach(dto -> {
				try {
					bw.write(dto.toString() + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//apnic|CN|ipv4|69.231.128.0|16384|20171229|allocated|A91CD28A
	/**
	 * @param line
	 * @return CountryIPRangeDto
	 */
	private static CountryIPRangeDto filtering(String line) throws Exception {
		if (line.length() != 0) {
			String[] values = line.split("\\|");

			// System.out.println("length: " + values.length);
			if (values.length == 8) {
				// System.out.println("type: " + values[2]);
				if ("ipv4".equals(values[2])) {
					long firstNum = ip2long(values[3]);
					long lastNum = firstNum + Long.parseLong(values[4]) - 1;
					String lastIp = long2ip(lastNum);
					// country, firstIp, lastIp, firstNum, lastNum
					return new CountryIPRangeDto(values[1], values[3], lastIp, firstNum, lastNum);
				}
			}
		}
		return null;
	}

	//Converts a String that represents an IP to an int.
	private static long ip2long(String ip) throws Exception {

		long result = 0;
		String[] ipAddressInArray = ip.split("\\.");

		for (int i = 3; i >= 0; i--) {
			long value = Long.parseLong(ipAddressInArray[3 - i]);
			result |= value << (i * 8);
		}

		return result;
	}

	private static String long2ip(long number) throws Exception {
		return ((number >> 24) & 0xFF) + "."
				+ ((number >> 16) & 0xFF) + "."
				+ ((number >> 8) & 0xFF) + "."
				+ (number & 0xFF);
	}
}
