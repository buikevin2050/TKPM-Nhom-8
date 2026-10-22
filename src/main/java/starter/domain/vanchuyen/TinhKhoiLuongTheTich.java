package starter.domain.vanchuyen;

import java.util.Objects;

/**
 * Domain Service tính khối lượng thể tích của kiện hàng theo chuẩn bưu chính quốc tế.
 * Công thức: (dài * rộng * cao) / 5000.0 (kg).
 * Xử lý làm tròn số thực đến 2 chữ số thập phân.
 */
public class TinhKhoiLuongTheTich {

    public static final double HE_SO_QUY_DOI = 5000.0;
    public static final double TI_LE_GIAM_THE_TICH_HUT = 0.5;

    /**
     * Tính khối lượng thể tích tiêu chuẩn từ kích thước kiện hàng.
     * Làm tròn đến 2 chữ số thập phân.
     *
     * @param kichThuoc kích thước kiện hàng (dài, rộng, cao theo cm)
     * @return khối lượng thể tích (kg) làm tròn 2 chữ số thập phân
     */
    public double tinh(KichThuoc kichThuoc) {
        Objects.requireNonNull(kichThuoc, "Kích thước không được để trống (null)");
        double raw = (kichThuoc.getChieuDai() * kichThuoc.getChieuRong() * kichThuoc.getChieuCao()) / HE_SO_QUY_DOI;
        return lamTron2ChuSo(raw);
    }

    /**
     * Tính khối lượng thể tích sau khi áp dụng dịch vụ hút chân không (giảm 50%).
     *
     * @param kichThuoc kích thước kiện hàng
     * @return khối lượng thể tích sau hút (kg) làm tròn 2 chữ số thập phân
     */
    public double tinhSauHutChanKhong(KichThuoc kichThuoc) {
        double raw = tinh(kichThuoc) * TI_LE_GIAM_THE_TICH_HUT;
        return lamTron2ChuSo(raw);
    }

    /**
     * Tính khối lượng thể tích dựa trên cờ có hút chân không hay không.
     *
     * @param kichThuoc      kích thước kiện hàng
     * @param coHutChanKhong true nếu có áp dụng hút chân không
     * @return khối lượng thể tích hiệu lực (kg)
     */
    public double tinh(KichThuoc kichThuoc, boolean coHutChanKhong) {
        return coHutChanKhong ? tinhSauHutChanKhong(kichThuoc) : tinh(kichThuoc);
    }

    private double lamTron2ChuSo(double giaTri) {
        return Math.round(giaTri * 100.0) / 100.0;
    }
}
