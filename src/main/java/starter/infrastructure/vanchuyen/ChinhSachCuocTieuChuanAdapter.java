package starter.infrastructure.vanchuyen;

import starter.application.vanchuyen.ChinhSachCuocPort;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter triển khai chính sách cước cơ bản và phụ phí dịch vụ gia tăng tiêu chuẩn.
 * Quy tắc:
 * - Cước cơ sở bậc thang: 20.000 VNĐ cho 1,0 kg đầu tiên; 5.000 VNĐ cho mỗi bước 0,5 kg tiếp theo (làm tròn lên).
 * - Phụ phí dịch vụ gia tăng:
 *   + "HUT_CHAN_KHONG" (Hút chân không): 15.000 VNĐ
 *   + "GOI_QUA" (Gói quà sinh nhật): 20.000 VNĐ
 *   + "VIET_THIEP" (Viết thiệp chúc mừng): 10.000 VNĐ
 */
public class ChinhSachCuocTieuChuanAdapter implements ChinhSachCuocPort {

    public static final double CUOC_1KG_DAU = 20000.0;
    public static final double GIA_MOI_BUOC = 5000.0;
    public static final double BUOC_TRONG_LUONG_KG = 0.5;

    public static final String DICH_VU_HUT_CHAN_KHONG = "HUT_CHAN_KHONG";
    public static final String DICH_VU_GOI_QUA = "GOI_QUA";
    public static final String DICH_VU_VIET_THIEP = "VIET_THIEP";

    private final Map<String, Double> bangPhuPhi = new HashMap<>();

    public ChinhSachCuocTieuChuanAdapter() {
        // Mã chuẩn
        bangPhuPhi.put(DICH_VU_HUT_CHAN_KHONG.toLowerCase(), 15000.0);
        bangPhuPhi.put(DICH_VU_GOI_QUA.toLowerCase(), 20000.0);
        bangPhuPhi.put(DICH_VU_VIET_THIEP.toLowerCase(), 10000.0);

        // Hỗ trợ thêm các mã định danh tương đương và tiếng Việt không dấu
        bangPhuPhi.put("hutchankhong", 15000.0);
        bangPhuPhi.put("hut_chan_khong", 15000.0);
        bangPhuPhi.put("goiquasinhnhat", 20000.0);
        bangPhuPhi.put("goi_qua_sinh_nhat", 20000.0);
        bangPhuPhi.put("goiqua", 20000.0);
        bangPhuPhi.put("goi_qua", 20000.0);
        bangPhuPhi.put("vietthiepmung", 10000.0);
        bangPhuPhi.put("viet_thiep_mung", 10000.0);
        bangPhuPhi.put("vietthiep", 10000.0);
        bangPhuPhi.put("viet_thiep", 10000.0);
        bangPhuPhi.put("thiep", 10000.0);
    }

    @Override
    public double tinhCuocCoBan(double trongLuongTinhCuoc) {
        if (trongLuongTinhCuoc < 0) {
            throw new IllegalArgumentException("Trọng lượng tính cước không được âm");
        }
        if (trongLuongTinhCuoc == 0) {
            return 0.0;
        }
        if (trongLuongTinhCuoc <= 1.0) {
            return CUOC_1KG_DAU;
        }

        double phanVuot1Kg = trongLuongTinhCuoc - 1.0;
        int soBuoc = (int) Math.ceil(phanVuot1Kg / BUOC_TRONG_LUONG_KG);
        return CUOC_1KG_DAU + (soBuoc * GIA_MOI_BUOC);
    }

    @Override
    public double layPhuPhiDichVu(String tenDichVu) {
        if (tenDichVu == null) {
            return 0.0;
        }
        String key = tenDichVu.trim().toLowerCase().replace("_", "").replace(" ", "");
        for (Map.Entry<String, Double> entry : bangPhuPhi.entrySet()) {
            String entryKey = entry.getKey().replace("_", "").replace(" ", "");
            if (key.equals(entryKey)) {
                return entry.getValue();
            }
        }
        if (key.contains("hut") || key.contains("hút")) {
            return 15000.0;
        }
        if (key.contains("goi") || key.contains("gói")) {
            return 20000.0;
        }
        if (key.contains("thiep") || key.contains("thiệp")) {
            return 10000.0;
        }
        return 0.0;
    }
}
