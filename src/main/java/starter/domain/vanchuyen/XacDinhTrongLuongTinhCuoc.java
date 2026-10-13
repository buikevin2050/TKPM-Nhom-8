package starter.domain.vanchuyen;

import java.util.Objects;

/**
 * Domain Policy xác định trọng lượng tính cước cuối cùng.
 * Quy tắc: Trọng lượng tính cước = Max(cân nặng thực tế, khối lượng thể tích hiệu lực).
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
     * @return trọng lượng tính cước (kg)
     */
    public double tinh(KichThuoc kichThuoc, double canNangThuc, boolean coHutChanKhong) {
        Objects.requireNonNull(kichThuoc, "Kích thước không được null");
        if (canNangThuc < 0) {
            throw new IllegalArgumentException("Cân nặng thực tế không được âm");
        }

        double khoiLuongTheTichHieuLuc = boTinhKhoiLuongTheTich.tinh(kichThuoc, coHutChanKhong);
        return Math.max(canNangThuc, khoiLuongTheTichHieuLuc);
    }

    /**
     * Xác định trọng lượng tính cước khi không áp dụng hút chân không.
     */
    public double tinh(KichThuoc kichThuoc, double canNangThuc) {
        return tinh(kichThuoc, canNangThuc, false);
    }
}
