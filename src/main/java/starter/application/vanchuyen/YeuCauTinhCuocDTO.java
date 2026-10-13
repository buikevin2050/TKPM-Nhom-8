package starter.application.vanchuyen;

import starter.domain.vanchuyen.KichThuoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Request Model (Input DTO) cho nghiệp vụ tính cước vận chuyển cồng kềnh.
 * Đóng gói và kiểm tra tính hợp lệ của dữ liệu đầu vào.
 */
public final class YeuCauTinhCuocDTO {

    private final double chieuDai;
    private final double chieuRong;
    private final double chieuCao;
    private final double canNangThuc;
    private final boolean coHutChanKhong;
    private final List<String> danhSachDichVuGiaTang;

    public YeuCauTinhCuocDTO(double chieuDai,
                            double chieuRong,
                            double chieuCao,
                            double canNangThuc,
                            boolean coHutChanKhong,
                            List<String> danhSachDichVuGiaTang) {
        kiemTraHopLe(chieuDai, chieuRong, chieuCao, canNangThuc);

        this.chieuDai = chieuDai;
        this.chieuRong = chieuRong;
        this.chieuCao = chieuCao;
        this.canNangThuc = canNangThuc;

        List<String> dichVu = danhSachDichVuGiaTang == null ? new ArrayList<>() : new ArrayList<>(danhSachDichVuGiaTang);
        boolean coYeuCauHut = coHutChanKhong || chuaDichVuHut(dichVu);

        if (coYeuCauHut && !chuaDichVuHut(dichVu)) {
            dichVu.add("HUT_CHAN_KHONG");
        }

        this.coHutChanKhong = coYeuCauHut;
        this.danhSachDichVuGiaTang = Collections.unmodifiableList(dichVu);
    }

    public YeuCauTinhCuocDTO(double chieuDai, double chieuRong, double chieuCao, double canNangThuc) {
        this(chieuDai, chieuRong, chieuCao, canNangThuc, false, Collections.emptyList());
    }

    public YeuCauTinhCuocDTO(double chieuDai, double chieuRong, double chieuCao, double canNangThuc, boolean coHutChanKhong) {
        this(chieuDai, chieuRong, chieuCao, canNangThuc, coHutChanKhong, Collections.emptyList());
    }

    private void kiemTraHopLe(double chieuDai, double chieuRong, double chieuCao, double canNangThuc) {
        if (chieuDai <= 0) {
            throw new IllegalArgumentException("Chiều dài phải lớn hơn 0");
        }
        if (chieuRong <= 0) {
            throw new IllegalArgumentException("Chiều rộng phải lớn hơn 0");
        }
        if (chieuCao <= 0) {
            throw new IllegalArgumentException("Chiều cao phải lớn hơn 0");
        }
        if (canNangThuc < 0) {
            throw new IllegalArgumentException("Cân nặng thực tế không được âm");
        }
    }

    private static boolean chuaDichVuHut(List<String> dichVu) {
        for (String dv : dichVu) {
            if (dv != null && (dv.equalsIgnoreCase("HUT_CHAN_KHONG") || dv.toLowerCase().contains("hút chân không"))) {
                return true;
            }
        }
        return false;
    }

    public KichThuoc layKichThuoc() {
        return new KichThuoc(chieuDai, chieuRong, chieuCao);
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

    public double getCanNangThuc() {
        return canNangThuc;
    }

    public boolean isCoHutChanKhong() {
        return coHutChanKhong;
    }

    public List<String> getDanhSachDichVuGiaTang() {
        return danhSachDichVuGiaTang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YeuCauTinhCuocDTO that = (YeuCauTinhCuocDTO) o;
        return Double.compare(that.chieuDai, chieuDai) == 0 &&
                Double.compare(that.chieuRong, chieuRong) == 0 &&
                Double.compare(that.chieuCao, chieuCao) == 0 &&
                Double.compare(that.canNangThuc, canNangThuc) == 0 &&
                coHutChanKhong == that.coHutChanKhong &&
                Objects.equals(danhSachDichVuGiaTang, that.danhSachDichVuGiaTang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chieuDai, chieuRong, chieuCao, canNangThuc, coHutChanKhong, danhSachDichVuGiaTang);
    }
}
