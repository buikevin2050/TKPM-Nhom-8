package starter.domain.vanchuyen;

import java.util.Objects;

/**
 * Value Object biểu diễn kích thước 3 chiều của kiện hàng (cm).
 * Bất biến (immutable), không có phương thức setter và tự kiểm tra tính hợp lệ trong constructor.
 */
public final class KichThuoc {

    private final double chieuDai;
    private final double chieuRong;
    private final double chieuCao;

    public KichThuoc(double chieuDai, double chieuRong, double chieuCao) {
        if (chieuDai <= 0 || chieuRong <= 0 || chieuCao <= 0) {
            throw new IllegalArgumentException("Kích thước kiện hàng phải lớn hơn 0");
        }
        this.chieuDai = chieuDai;
        this.chieuRong = chieuRong;
        this.chieuCao = chieuCao;
    }

    public double getChieuDai() {
        return chieuDai;
    }

    public double getChieuRong() {
        return chieuRong;
    }

    public double getChieuCao() {
        return chieuCao;
    }

    /**
     * Tính thể tích đóng gói theo đơn vị cm³.
     */
    public double tinhTheTichCm3() {
        return chieuDai * chieuRong * chieuCao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KichThuoc that = (KichThuoc) o;
        return Double.compare(that.chieuDai, chieuDai) == 0 &&
                Double.compare(that.chieuRong, chieuRong) == 0 &&
                Double.compare(that.chieuCao, chieuCao) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chieuDai, chieuRong, chieuCao);
    }

    @Override
    public String toString() {
        return String.format("%s x %s x %s cm", chieuDai, chieuRong, chieuCao);
    }
}
