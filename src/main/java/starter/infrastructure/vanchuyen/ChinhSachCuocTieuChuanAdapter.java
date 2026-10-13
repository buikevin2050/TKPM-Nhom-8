package starter.infrastructure.vanchuyen;

import starter.application.vanchuyen.ChinhSachCuocPort;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter triển khai chính sách cước cơ bản và phụ phí dịch vụ gia tăng tiêu chuẩn.
 * Quy tắc:
 * - Cước cơ sở: 20.000 VNĐ cho 1,0 kg đầu tiên; 2.500 VNĐ cho mỗi bước 0,5 kg tiếp theo (làm tròn lên).
 * - Phụ phí dịch vụ gia tăng:
 *   + "HutChanKhong": 15.000 VNĐ
 *   + "GoiQuaSinhNhat": 20.000 VNĐ
 *   + "VietThiepMung": 10.000 VNĐ
 */
public class ChinhSachCuocTieuChuanAdapter implements ChinhSachCuocPort {

    public static final double CUOC_1KG_DAU = 20000.0;
    public static final double GIA_MOI_BUOC = 2500.0;
    public static final double BUOC_TRONG_LUONG_KG = 0.5;

    public static final String DICH_VU_HUT_CHAN_KHONG = "HutChanKhong";
    public static final String DICH_VU_GOI_QUA = "GoiQuaSinhNhat";
    public static final String DICH_VU_VIET_THIEP = "VietThiepMung";

    private final Map<String, Double> bangPhuPhi = new HashMap<>();

    public ChinhSachCuocTieuChuanAdapter() {
        bangPhuPhi.put(DICH_VU_HUT_CHAN_KHONG.toLowerCase(), 15000.0);
        bangPhuPhi.put(DICH_VU_GOI_QUA.toLowerCase(), 20000.0);
        bangPhuPhi.put(DICH_VU_VIET_THIEP.toLowerCase(), 10000.0);
        // Hỗ trợ thêm các mã định danh tương đương
        bangPhuPhi.put("hut_chan_khong", 15000.0);
        bangPhuPhi.put("goi_qua", 20000.0);
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
        String key = tenDichVu.trim().toLowerCase();
        if (bangPhuPhi.containsKey(key)) {
            return bangPhuPhi.get(key);
        }
        if (key.contains("hút chân không")) {
            return 15000.0;
        }
        if (key.contains("gói quà")) {
            return 20000.0;
        }
        if (key.contains("thiệp")) {
            return 10000.0;
        }
        return 0.0;
    }
}
