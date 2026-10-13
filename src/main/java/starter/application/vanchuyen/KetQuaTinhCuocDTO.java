package starter.application.vanchuyen;

import java.util.Objects;

/**
 * Response Model (Output DTO) kết quả tính cước vận chuyển và dịch vụ gia tăng.
 * Bất biến (immutable), không làm rò rỉ thực thể miền ra ngoài.
 */
public final class KetQuaTinhCuocDTO {

    private final double khoiLuongTheTichBanDau;
    private final double khoiLuongTheTichSauHut;
    private final double khoiLuongTheTich;
    private final double trongLuongTinhCuoc;
    private final double cuocPhiCoBan;
    private final double tongPhuPhi;
    private final double tongTienCuoc;
    private final double cuocChuaHut;
    private final double tienTietKiem;
    private final double phuPhiHutChanKhong;
    private final double phuPhiGoiQua;
    private final double phuPhiThiep;
    private final String tomTat;

    public KetQuaTinhCuocDTO(double khoiLuongTheTichBanDau,
                            double khoiLuongTheTichSauHut,
                            double khoiLuongTheTich,
                            double trongLuongTinhCuoc,
                            double cuocPhiCoBan,
                            double tongPhuPhi,
                            double tongTienCuoc,
                            double cuocChuaHut,
                            double tienTietKiem,
                            double phuPhiHutChanKhong,
                            double phuPhiGoiQua,
                            double phuPhiThiep,
                            String tomTat) {
        this.khoiLuongTheTichBanDau = khoiLuongTheTichBanDau;
        this.khoiLuongTheTichSauHut = khoiLuongTheTichSauHut;
        this.khoiLuongTheTich = khoiLuongTheTich;
        this.trongLuongTinhCuoc = trongLuongTinhCuoc;
        this.cuocPhiCoBan = cuocPhiCoBan;
        this.tongPhuPhi = tongPhuPhi;
        this.tongTienCuoc = tongTienCuoc;
        this.cuocChuaHut = cuocChuaHut;
        this.tienTietKiem = tienTietKiem;
        this.phuPhiHutChanKhong = phuPhiHutChanKhong;
        this.phuPhiGoiQua = phuPhiGoiQua;
        this.phuPhiThiep = phuPhiThiep;
        this.tomTat = tomTat;
    }

    public double getKhoiLuongTheTichBanDau() {
        return khoiLuongTheTichBanDau;
    }

    public double getKhoiLuongTheTichSauHut() {
        return khoiLuongTheTichSauHut;
    }

    public double getKhoiLuongTheTich() {
        return khoiLuongTheTich;
    }

    public double getTrongLuongTinhCuoc() {
        return trongLuongTinhCuoc;
    }

    public double getCuocPhiCoBan() {
        return cuocPhiCoBan;
    }

    public double getTongPhuPhi() {
        return tongPhuPhi;
    }

    public double getTongTienCuoc() {
        return tongTienCuoc;
    }

    public double getCuocChuaHut() {
        return cuocChuaHut;
    }

    public double getTienTietKiem() {
        return tienTietKiem;
    }

    public double getPhuPhiHutChanKhong() {
        return phuPhiHutChanKhong;
    }

    public double getPhuPhiGoiQua() {
        return phuPhiGoiQua;
    }

    public double getPhuPhiThiep() {
        return phuPhiThiep;
    }

    public String getTomTat() {
        return tomTat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KetQuaTinhCuocDTO that = (KetQuaTinhCuocDTO) o;
        return Double.compare(that.khoiLuongTheTichBanDau, khoiLuongTheTichBanDau) == 0 &&
                Double.compare(that.khoiLuongTheTichSauHut, khoiLuongTheTichSauHut) == 0 &&
                Double.compare(that.khoiLuongTheTich, khoiLuongTheTich) == 0 &&
                Double.compare(that.trongLuongTinhCuoc, trongLuongTinhCuoc) == 0 &&
                Double.compare(that.cuocPhiCoBan, cuocPhiCoBan) == 0 &&
                Double.compare(that.tongPhuPhi, tongPhuPhi) == 0 &&
                Double.compare(that.tongTienCuoc, tongTienCuoc) == 0 &&
                Double.compare(that.cuocChuaHut, cuocChuaHut) == 0 &&
                Double.compare(that.tienTietKiem, tienTietKiem) == 0 &&
                Double.compare(that.phuPhiHutChanKhong, phuPhiHutChanKhong) == 0 &&
                Double.compare(that.phuPhiGoiQua, phuPhiGoiQua) == 0 &&
                Double.compare(that.phuPhiThiep, phuPhiThiep) == 0 &&
                Objects.equals(tomTat, that.tomTat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(khoiLuongTheTichBanDau, khoiLuongTheTichSauHut, khoiLuongTheTich,
                trongLuongTinhCuoc, cuocPhiCoBan, tongPhuPhi, tongTienCuoc, cuocChuaHut,
                tienTietKiem, phuPhiHutChanKhong, phuPhiGoiQua, phuPhiThiep, tomTat);
    }

    @Override
    public String toString() {
        return tomTat != null ? tomTat :
                String.format("KetQuaTinhCuocDTO{trongLuongTinhCuoc=%.1f kg, tongTienCuoc=%.0f VND}",
                        trongLuongTinhCuoc, tongTienCuoc);
    }
}
