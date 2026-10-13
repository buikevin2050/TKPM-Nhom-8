package starter.domain.vanchuyen;

import java.util.Objects;

/**
 * Domain Service tính khối lượng thể tích của kiện hàng.
 * Công thức: (dài * rộng * cao) / 5000.0 (kg).
 * Hút chân không giảm 50% khối lượng thể tích quy đổi.
 */
public class TinhKhoiLuongTheTich {

    public static final double HE_SO_QUY_DOI = 5000.0;
    public static final double TI_LE_GIAM_THE_TICH_HUT = 0.5;

    /**
     * Tính khối lượng thể tích tiêu chuẩn từ kích thước kiện hàng.
     *
     * @param kichThuoc kích thước kiện hàng (dài, rộng, cao theo cm)
     * @return khối lượng thể tích (kg)
     */
    public double tinh(KichThuoc kichThuoc) {
        Objects.requireNonNull(kichThuoc, "Kích thước không được để trống (null)");
        return kichThuoc.tinhTheTichCm3() / HE_SO_QUY_DOI;
    }

    /**
     * Tính khối lượng thể tích sau khi áp dụng dịch vụ hút chân không (giảm 50%).
     *
     * @param kichThuoc kích thước kiện hàng
     * @return khối lượng thể tích sau hút (kg)
     */
    public double tinhSauHutChanKhong(KichThuoc kichThuoc) {
        return tinh(kichThuoc) * TI_LE_GIAM_THE_TICH_HUT;
    }

    /**
     * Tính khối lượng thể tích dựa trên cờ có hút chân không hay không.
     *
     * @param kichThuoc       kích thước kiện hàng
     * @param coHutChanKhong  true nếu có áp dụng hút chân không
     * @return khối lượng thể tích hiệu lực (kg)
     */
    public double tinh(KichThuoc kichThuoc, boolean coHutChanKhong) {
        return coHutChanKhong ? tinhSauHutChanKhong(kichThuoc) : tinh(kichThuoc);
    }
}
