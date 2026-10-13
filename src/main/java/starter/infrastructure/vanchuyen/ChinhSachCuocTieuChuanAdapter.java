package starter.infrastructure.vanchuyen;

import starter.application.vanchuyen.ChinhSachCuocPort;

/**
 * Adapter triển khai chính sách cước cơ bản theo biểu phí chuẩn.
 * Quy tắc:
 * - 20.000 VNĐ cho 1,0 kg đầu tiên.
 * - 2.500 VNĐ cho mỗi bước 0,5 kg tiếp theo (làm tròn lên).
 */
public class ChinhSachCuocTieuChuanAdapter implements ChinhSachCuocPort {

    public static final double CUOC_1KG_DAU = 20000.0;
    public static final double GIA_MOI_BUOC = 2500.0;
    public static final double BUOC_TRONG_LUONG_KG = 0.5;

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
}
