package com.nd.ql.public_class;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class PullResponseServletData {

	public static String readInpuString(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}// 将缓冲区的数据写到一个数组中
			is.close();
			// baos.close();
			byte[] result = baos.toByteArray();
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
			return "获取失败";
		}
	}
}
