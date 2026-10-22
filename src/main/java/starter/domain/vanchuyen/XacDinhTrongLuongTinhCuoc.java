package starter.domain.vanchuyen;

import java.util.Objects;

/**
 * Domain Policy xác định trọng lượng tính cước cuối cùng.
 * Quy tắc:
 * - Nếu coHutChanKhong == true thì thể tích tính cước = thể tích ban đầu * 0.5.
 * - Trọng lượng tính cước cuối cùng = Max(cân nặng thực tế, thể tích sau xử lý).
 * - Làm tròn 1 chữ số thập phân để đảm bảo độ chính xác số thực.
 */
public class XacDinhTrongLuongTinhCuoc {

    private final TinhKhoiLuongTheTich boTinhKhoiLuongTheTich;

    public XacDinhTrongLuongTinhCuoc() {
        this(new TinhKhoiLuongTheTich());
    }

    public XacDinhTrongLuongTinhCuoc(TinhKhoiLuongTheTich boTinhKhoiLuongTheTich) {
        this.boTinhKhoiLuongTheTich = Objects.requireNonNull(boTinhKhoiLuongTheTich, "Bộ tính khối lượng thể tích không được null");
    }

    /**
     * Xác định trọng lượng tính cước dựa trên kích thước, cân nặng thực tế và tùy chọn hút chân không.
     *
     * @param kichThuoc       kích thước kiện hàng
     * @param canNangThuc     cân nặng thực tế (kg)
     * @param coHutChanKhong  có áp dụng dịch vụ hút chân không hay không
     * @return trọng lượng tính cước (kg) làm tròn 1 chữ số thập phân
     */
    public double tinh(KichThuoc kichThuoc, double canNangThuc, boolean coHutChanKhong) {
        Objects.requireNonNull(kichThuoc, "Kích thước không được null");
        if (canNangThuc <= 0) {
            throw new IllegalArgumentException("Cân nặng thực tế phải lớn hơn 0");
        }

        double theTichSauXuLy = boTinhKhoiLuongTheTich.tinh(kichThuoc, coHutChanKhong);
        double trongLuong = Math.max(canNangThuc, theTichSauXuLy);
        return Math.round(trongLuong * 100.0) / 100.0;
    }

    /**
     * Xác định trọng lượng tính cước khi không áp dụng hút chân không.
     */
    public double tinh(KichThuoc kichThuoc, double canNangThuc) {
        return tinh(kichThuoc, canNangThuc, false);
    }
}
