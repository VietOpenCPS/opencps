package org.opencps.keypay.model;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.opencps.keypay.security.HashFunction;
import org.opencps.keypay.security.MD5;
import org.opencps.keypay.service.restful.KPJsonRest;
import org.opencps.keypay.service.restful.KPRest;

public class KeyPay {

    //các tham số gửi đi

    private String merchant_trans_id;
    private String merchant_code;
    private String good_code;
    private String net_cost;
    private String ship_fee;
    private String tax;
    private String bank_code;
    private String service_code;
    private String version;
    private String command;
    private String currency_code;
    private String secure_hash;
    private String desc_1;
    private String desc_2;
    private String desc_3;
    private String desc_4;
    private String desc_5;
    private String xml_description;
    private String current_locale;
    private String country_code;
    private String return_url;
    private String internal_bank;
    //secure key
    private String merchant_secure_key;
    //keypay url
    private String keypay_url;
    //các tham số trả về từ KeyPay
    private String trans_id;
    private String response_code;

    public KeyPay() {
    }

    /**
     * Constructor - Lấy các tham số khi gửi đơn hàng sang KeyPay
     *
     * @param merchant_trans_id
     * @param merchant_code
     * @param good_code
     * @param net_cost
     * @param ship_fee
     * @param tax
     * @param bank_code
     * @param service_code
     * @param version
     * @param command
     * @param currency_code
     * @param desc_1
     * @param desc_2
     * @param desc_3
     * @param desc_4
     * @param desc_5
     * @param xml_description
     * @param current_locale
     * @param country_code
     * @param return_url
     * @param internal_bank
     * @param merchant_secure_key
     */
    public KeyPay(String merchant_trans_id, String merchant_code, String good_code, String net_cost,
            String ship_fee, String tax, String bank_code, String service_code, String version,
            String command, String currency_code, String desc_1, String desc_2, String desc_3,
            String desc_4, String desc_5, String xml_description, String current_locale,
            String country_code, String return_url, String internal_bank, String merchant_secure_key) {
        this.merchant_trans_id = merchant_trans_id;
        this.merchant_code = merchant_code;
        this.good_code = good_code;
        this.net_cost = net_cost;
        this.ship_fee = ship_fee;
        this.tax = tax;
        this.bank_code = bank_code;
        this.service_code = service_code;
        this.version = version;
        this.command = command;
        this.currency_code = currency_code;
        this.desc_1 = desc_1;
        this.desc_2 = desc_2;
        this.desc_3 = desc_3;
        this.desc_4 = desc_4;
        this.desc_5 = desc_5;
        this.xml_description = xml_description;
        this.current_locale = current_locale;
        this.country_code = country_code;
        this.return_url = return_url;
        this.internal_bank = internal_bank;
        //get merchant key 
        this.merchant_secure_key = merchant_secure_key;
        //tính secure hash khi gửi đi
        this.secure_hash = getSecureHash();
    }

    /**
     * tính Secure Hash khi gửi đi
     *
     * @return
     */
    private String getSecureHash() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("version", version);
        fields.put("current_locale", current_locale);
        fields.put("command", command);
        fields.put("merchant_trans_id", merchant_trans_id);
        fields.put("merchant_code", merchant_code);
        fields.put("country_code", country_code);
        fields.put("good_code", good_code);
        fields.put("net_cost", net_cost);
        fields.put("ship_fee", ship_fee);
        fields.put("tax", tax);
        fields.put("service_code", service_code);
        fields.put("currency_code", currency_code);
        fields.put("return_url", return_url);
        HashFunction hf = new HashFunction();
        return hf.hashAllFields(fields, merchant_secure_key);
    }

    /**
     * Buid URL to send redirect to KeyPay
     *
     * @param params
     * @return
     */
    public String buildURL(Map params) {
        String url_redirect = keypay_url;
        try {
            Iterator i = params.keySet().iterator();
            String param = "";
            while (i.hasNext()) {
                String key = (String) i.next();
                String value = ((String[]) params.get(key))[0];
                param += key + "=" + URLEncoder.encode(value, "UTF-8") + "&";
            }
            url_redirect += param + "secure_hash=" + secure_hash;
        } catch (Exception e) {
            System.out.println("ERROR Build URL");
        }
        return url_redirect;
    }

    /**
     * Constructor - Lấy dữ liệu trả về từ KeyPay
     *
     * @param request
     */
    public KeyPay(HttpServletRequest request) {
        try {
            this.command = request.getParameter("command");
            this.merchant_trans_id = request.getParameter("merchant_trans_id");
            this.merchant_code = request.getParameter("merchant_code");
            this.response_code = request.getParameter("response_code");
            this.trans_id = request.getParameter("trans_id");
            this.good_code = request.getParameter("good_code");
            this.net_cost = request.getParameter("net_cost");
            this.ship_fee = request.getParameter("ship_fee");
            this.tax = request.getParameter("tax");
            this.service_code = request.getParameter("service_code");
            this.currency_code = request.getParameter("currency_code");
            this.bank_code = request.getParameter("bank_code");
            this.secure_hash = request.getParameter("secure_hash");
            this.desc_1 = new String(request.getParameter("desc_1").getBytes("ISO-8859-1"), "UTF-8");
            this.desc_2 = new String(request.getParameter("desc_2").getBytes("ISO-8859-1"), "UTF-8");
            this.desc_3 = new String(request.getParameter("desc_3").getBytes("ISO-8859-1"), "UTF-8");
            this.desc_4 = new String(request.getParameter("desc_4").getBytes("ISO-8859-1"), "UTF-8");
            this.desc_5 = request.getParameter("desc_5");
        } catch (Exception e) {
            System.out.println("ERROE get data KeyPay return");
        }
    }

    /**
     * Tính secure hash ở đại lý khi thanh toán xong
     *
     * @return
     */
    public String getSecureHashMerchant() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("command", command);
        fields.put("merchant_trans_id", merchant_trans_id);
        fields.put("merchant_code", merchant_code);
        fields.put("response_code", response_code);
        fields.put("trans_id", trans_id);
        fields.put("good_code", good_code);
        fields.put("net_cost", net_cost);
        fields.put("ship_fee", ship_fee);
        fields.put("tax", tax);
        fields.put("service_code", service_code);
        fields.put("currency_code", currency_code);
        fields.put("bank_code", bank_code);
        HashFunction hf = new HashFunction();
        return hf.hashAllFields(fields, merchant_secure_key);
    }

    /**
     * Kiểm tra secure hash có đúng không
     *
     * @param secure_merchant
     * @return
     */
    public boolean checkSecureHash(String secure_merchant) {
        if (secure_hash == null || secure_merchant == null) {
            return false;
        } else if (secure_hash.equals(secure_merchant)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * QueryBillStatus - Hàm kiểm tra trạng thái giao dịch - RESTful
     *
     * @return
     */
    public String queryBillStatusRESTful() {
        String sc_querry = "";
        try {
            MD5 md5 = new MD5();
            sc_querry = md5.getMD5Hash(merchant_trans_id + good_code + trans_id
                    + merchant_code + merchant_secure_key);
        } catch (Exception e) {
            System.out.println("ERROR calculate secure hash Query Bill");
        }
        KPRest kp = new KPRest();
        return kp.QuerryBillStatus(merchant_trans_id, good_code, trans_id, merchant_code, sc_querry);
    }

    /**
     * QueryBillStatus - Hàm kiểm tra trạng thái giao dịch - RESTful (JSON)
     *
     * @return
     */
    public String queryBillStatusRESTful_JSON(String merchant_trans_id, String good_code,
    		String trans_id, String merchant_code, String merchant_secure_key) {
        String sc_querry = "";
        try {
            MD5 md5 = new MD5();
            sc_querry = md5.getMD5Hash(merchant_trans_id + good_code + trans_id
                    + merchant_code + merchant_secure_key);
        } catch (Exception e) {
        }
        KPJsonRest kpJson = new KPJsonRest();
        return kpJson.QuerryBillStatus(merchant_trans_id, good_code,
                trans_id, merchant_code, sc_querry);
    }

    /**
     * QueryBillStatus - Hàm kiểm tra trạng thái giao dịch - RESTful (JSON)
     *
     * @return
     */
    public String queryBillStatusRESTful_JSON() {
        String sc_querry = "";
        try {
            MD5 md5 = new MD5();
            sc_querry = md5.getMD5Hash(merchant_trans_id + good_code + trans_id
                    + merchant_code + merchant_secure_key);
        } catch (Exception e) {
            System.out.println("ERROR calculate secure hash Query Bill");
        }
        KPJsonRest kpJson = new KPJsonRest();
        return kpJson.QuerryBillStatus(merchant_trans_id, good_code,
                trans_id, merchant_code, sc_querry);
    }
    
    /**
     * Hàm mao trạng thái trả về từ KeyPay
     *
     * @param response_code
     * @return
     */
    public String genMsgReturn(String response_code) {
        String msg;
        switch (Integer.parseInt(response_code)) {
            case 0:
                //success
                msg = "Thanh toán thành công";
                break;
            case 1:
                //merchant code sai
                msg = "Đại lý không tồn tại trong hệ thống";
                break;
            case 2:
                //secure hash sai
                msg = "Chuỗi mã hóa không hợp lệ";
                break;
            case 3:
                //merchant trans id khong hop le
                msg = "Mã giao dịch không hợp lệ";
                break;
            case 4:
                //trans id khong ton tai
                msg = "Đã có lỗi xảy ra khi thực hiện thanh toán";
                break;
            case 5:
                //ma dich vu khong hop le
                msg = "Mã dịch vụ không hợp lệ";
                break;
            case 6:
                //giao dich da gui confirm
                msg = "Giao dịch đã tồn tại trong hệ thống";
                break;
            case 7:
                //ma qgia khong hop le
                msg = "Mã quốc gia không hợp lệ";
                break;
            case 8:
                //timeout
                msg = "Không nhận được kết quả trả về từ Ngân hàng do quá thời gian thực hiện giao dịch";
                break;
            case 9:
                //mo ta don hang khong hop le
                msg = "Mô tả đơn hàng không hợp lệ";
                break;
            case 10:
                //ma don hang khong hop le
                msg = "Mã đơn hàng không hợp lệ";
                break;
            case 11:
                //net cost fail
                msg = "Số tiền thanh toán không hợp lệ";
                break;
            case 12:
                //ship fee fail
                msg = "Phí vận chuyển không hợp lệ";
                break;
            case 13:
                //tax fail
                msg = "Thuế không hợp lệ";
                break;
            case 14:
                //merchant code chua duoc cau hinh phi de thanh toan
                msg = "Đại lý chưa được cấu hình phí";
                break;
            case 15:
                //sai ma ngan hang
                msg = "Mã ngân hàng không nằm trong hệ thống chấp nhận thanh toán";
                break;
            case 16:
                //so tien dai ly ko nam trong khoang cho phep (dai ly o day la keypay so voi BN)
                msg = "Số tiền thanh toán không nằm trong khoảng cho phép";
                break;
            case 17:
                //tai khoan ko du tien
                msg = "Tài khoản không đủ tiền thực hiện giao dịch";
                break;
            case 18:
                //huy bo giao dich
                msg = "Bạn đã hủy bỏ giao dịch";
                break;
            case 19:
                //trans date time ko hop le
                msg = "Thời gian thực hiện thanh toán không hợp lệ";
                break;
            case 20:
                //otp type ko hop le
                msg = "Kiểu nhận mã OTP không đúng";
                break;
            case 21:
                //otp nhap ko dung
                msg = "Xác thực OTP không thành công";
                break;
            case 25:
                //sai thong tin chu the lan 1
                msg = "Nhập sai thông tin chủ thẻ lần 1";
                break;
            case 26:
                //sai thong tin chu the lan 2
                msg = "Nhập sai thông tin chủ thẻ lần 2";
                break;
            case 27:
                //sai thong tin chu the lan 3 - redirect
                msg = "Nhập sai thông tin chủ thẻ lần 3";
                break;
            case 30:
                // phien ban khong hop le
                msg = "Phiên bản thanh toán không hợp lệ";
                break;
            case 31:
                //ma lenh khong hop le
                msg = "Yêu cầu không hợp lệ";
                break;
            case 32:
                //country code khong hop le
                msg = "Loại tiền tệ không hợp lệ";
                break;
            case 33:
                //ngon ngu khong hop le
                msg = "Ngôn ngữ không hợp lệ";
                break;
            case 34:
                //desc 1 khong hop le
                msg = "Thông tin thêm (desc 1) không hợp lệ";
                break;
            case 35:
                //desc 2 khong hop le
                msg = "Thông tin thêm (desc 2) không hợp lệ";
                break;
            case 36:
                //desc 3 khong hop le
                msg = "Thông tin thêm (desc 3) không hợp lệ";
                break;
            case 37:
                //desc 4 khong hop le
                msg = "Thông tin thêm (desc 4) không hợp lệ";
                break;
            case 38:
                //desc 5 khong hop le
                msg = "Thông tin thêm (desc 5) không hợp lệ";
                break;
            case 39:
                //url khong hop le
                msg = "Chuỗi URL trả về không hợp lệ";
                break;
            case 40:
                //internal bank //desc 1 khong hop le
                msg = "Loại thẻ không hợp lệ";
                break;
            case 41:
                //the nghi van
                msg = "Phát hiện thẻ nghi vấn";
                break;
            case 54:
                //the het han
                msg = "Thẻ ngân hàng hết hạn";
                break;
            case 57:
                //chua dang ky ibanking
                msg = "Thẻ ngân hàng chưa đăng ký dịch vụ thanh toán trực tuyến";
                break;
            case 61:
                //qua han muc giao dich trong ngay
                msg = "Thẻ sử dụng đã quá hạn mức giao dịch trong ngày";
                break;
            case 62:
                //the bi khoa
                msg = "Thẻ bị khóa";
                break;
            case 65:
                //qua han muc giao dich trong 1 lan gdich
                msg = "Thẻ quá hạn mức trong một lần giao dịch";
                break;
            case 97:
                //bank chua san sang
                msg = "Kết nối tới Ngân Hàng không thành công";
                break;
            case 98:
                //giao dich ko hop le
                msg = "Giao dịch không được phép";
                break;
            case 99:
                //loi ko xac dinh
                msg = "Đã có lỗi xảy ra khi thực hiện thanh toán";
                break;
            default:
                //loi ko xac dinh
                msg = "Đã có lỗi xảy ra khi thực hiện thanh toán";
                break;
        }
        return msg;
    }

    public String getMerchant_secure_key() {
        return merchant_secure_key;
    }

    public void setMerchant_secure_key(String merchant_secure_key) {
        this.merchant_secure_key = merchant_secure_key;
    }

    public String getKeypay_url() {
        return keypay_url;
    }

    public void setKeypay_url(String keypay_url) {
        this.keypay_url = keypay_url;
    }

    public String getInternal_bank() {
        return internal_bank;
    }

    public void setInternal_bank(String internal_bank) {
        this.internal_bank = internal_bank;
    }

    public String getXml_description() {
        return xml_description;
    }

    public void setXml_description(String xml_description) {
        this.xml_description = xml_description;
    }

    public String getCurrent_locale() {
        return current_locale;
    }

    public void setCurrent_locale(String current_locale) {
        this.current_locale = current_locale;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getDesc_1() {
        return desc_1;
    }

    public void setDesc_1(String desc_1) {
        this.desc_1 = desc_1;
    }

    public String getDesc_2() {
        return desc_2;
    }

    public void setDesc_2(String desc_2) {
        this.desc_2 = desc_2;
    }

    public String getDesc_3() {
        return desc_3;
    }

    public void setDesc_3(String desc_3) {
        this.desc_3 = desc_3;
    }

    public String getDesc_4() {
        return desc_4;
    }

    public void setDesc_4(String desc_4) {
        this.desc_4 = desc_4;
    }

    public String getDesc_5() {
        return desc_5;
    }

    public void setDesc_5(String desc_5) {
        this.desc_5 = desc_5;
    }

    public String getMerchant_trans_id() {
        return merchant_trans_id;
    }

    public void setMerchant_trans_id(String merchant_trans_id) {
        this.merchant_trans_id = merchant_trans_id;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getGood_code() {
        return good_code;
    }

    public void setGood_code(String good_code) {
        this.good_code = good_code;
    }

    public String getNet_cost() {
        return net_cost;
    }

    public void setNet_cost(String net_cost) {
        this.net_cost = net_cost;
    }

    public String getShip_fee() {
        return ship_fee;
    }

    public void setShip_fee(String ship_fee) {
        this.ship_fee = ship_fee;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getSecure_hash() {
        return secure_hash;
    }

    public void setSecure_hash(String secure_hash) {
        this.secure_hash = secure_hash;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

}
