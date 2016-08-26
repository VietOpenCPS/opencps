package org.opencps.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "{\"conghoaxahoi\":{},\"doclaptudo\":{},\"giaydangky\":{},\"kinhgui\":{},\"thongtinthuongnhan\":\"Công ty TNHH Phát Triển\",\"diachilienhe\":\"Hà Nội\",\"sodienthoai\":\"09999998877\",\"sofax\":\"0998383837\",\"email\":\"09999@gfd.vn\",\"tencosochebien\":\"Cơ sở 1\",\"diachicosochebien\":\"Hà Nội\",\"macosochebien\":\"11029\",\"thongtindangky\":{},\"bangthongtindangky\":[{\"col1\":\"1\",\"col2\":\"2\",\"col3\":\"2\",\"col4\":\"3\",\"col5\":\"4\",\"col6\":\"4\"}],\"hosokemtheo\":{},\"dangkynuoithuysanlb\":{},\"dangkynuoithuysan\":false,\"hdmuathuysannguyenlieulb\":{},\"hdmuathuysannguyenlieu\":false,\"giaychungnhanlb\":{},\"giaychungnhan\":true,\"hdmuathuysanthuongphamlb\":{},\"hdmuathuysanthuongpham\":false,\"loicamdoan\":{},\"diadiem2\":\"Hà Nội\",\"ngaythangnam2\":\"19/08/2016\",\"chucdanhky2\":\"Giám đốc\",\"tennguoiky2\":\"ANNNN\",\"foodxacnhan\":{},\"thuongnhandangky\":{},\"kyhoten1\":{},\"kyhoten2\":{},\"hieuluchopdong\":{},\"ghichu13\":{},\"ghichu14\":{},\"ghichu15\":{},\"ghichu16\":{},\"gachngang\":{},\"gachgiua\":{}}";
			System.out.println("JsonUtils.main()"+quote(str));
	}

	public static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "{}";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
            case '\\':
            	sb.append("\\");
            	sb.append("\\");
                break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
	public static String quoteHTML(String string) {
		
		if (string == null || string.length() == 0) {
            return "{}";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
            case '\\':
            	sb.append("REPLACEKEY");
                break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
		String result = sb.toString().replaceAll("REPLACEKEYn", "<br />");
        return result;
    }
}
