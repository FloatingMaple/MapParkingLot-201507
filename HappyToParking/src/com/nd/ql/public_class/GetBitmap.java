package com.nd.ql.public_class;
import java.net.URLEncoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GetBitmap {
	public static Bitmap getBitmap(String string) {
		Bitmap mBitmap = null;
		try {
			String path = "http://" + PublicPath.coonPath
					+ ":80/QuickLife/response_picture?path="
					+ URLEncoder.encode(string, "gbk");// ���뷽ʽ����ת��Ϊutf-8
			mBitmap = BitmapFactory.decodeStream(GetNetConnection
					.getConnection(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmap;
	}
}
