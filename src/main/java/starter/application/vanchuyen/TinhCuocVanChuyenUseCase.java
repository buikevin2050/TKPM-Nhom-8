package starter.application.vanchuyen;

import starter.domain.vanchuyen.KichThuoc;
import starter.domain.vanchuyen.TinhKhoiLuongTheTich;
import starter.domain.vanchuyen.XacDinhTrongLuongTinhCuoc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Use Case Interactor (Control) điều phối nghiệp vụ tính cước vận chuyển cồng kềnh.
 * Tuân thủ Single Responsibility Principle (SRP), Dependency Inversion Principle (DIP)
 * và Open-Closed Principle (OCP) - tra cứu phụ phí dịch vụ gia tăng hoàn toàn qua ChinhSachCuocPort.
 */
public class TinhCuocVanChuyenUseCase {

    private final ChinhSachCuocPort chinhSachCuoc;
    private final LuuTruCuocPhiPort khoLuuTru;
    private final TinhKhoiLuongTheTich boTinhKhoiLuongTheTich;
    private final XacDinhTrongLuongTinhCuoc boXacDinhTrongLuong;

    public TinhCuocVanChuyenUseCase(ChinhSachCuocPort chinhSachCuoc, LuuTruCuocPhiPort khoLuuTru) {
        this(chinhSachCuoc, khoLuuTru, new TinhKhoiLuongTheTich(), new XacDinhTrongLuongTinhCuoc());
    }

    public TinhCuocVanChuyenUseCase(ChinhSachCuocPort chinhSachCuoc,
                                   LuuTruCuocPhiPort khoLuuTru,
                                   TinhKhoiLuongTheTich boTinhKhoiLuongTheTich,
                                   XacDinhTrongLuongTinhCuoc boXacDinhTrongLuong) {
        this.chinhSachCuoc = Objects.requireNonNull(chinhSachCuoc, "Chính sách cước không được null");
        this.khoLuuTru = Objects.requireNonNull(khoLuuTru, "Kho lưu trữ cước không được null");
        this.boTinhKhoiLuongTheTich = Objects.requireNonNull(boTinhKhoiLuongTheTich, "Bộ tính khối lượng thể tích không được null");
        this.boXacDinhTrongLuong = Objects.requireNonNull(boXacDinhTrongLuong, "Bộ xác định trọng lượng không được null");
    }

    /**
     * Thực thi quy trình tính cước vận chuyển.
     *
     * @param yeuCau DTO chứa dữ liệu yêu cầu tính cước
     * @return DTO kết quả tính cước bất biến
     */
    public KetQuaTinhCuocDTO thucThi(YeuCauTinhCuocDTO yeuCau) {
        Objects.requireNonNull(yeuCau, "Yêu cầu tính cước không được null");

        KichThuoc kichThuoc = yeuCau.layKichThuoc();
        double canNangThuc = yeuCau.getCanNangThuc();
        boolean coHutChanKhong = yeuCau.isCoHutChanKhong();

        // 1. Tính toán các mức khối lượng thể tích
        double khoiLuongTheTichBanDau = boTinhKhoiLuongTheTich.tinh(kichThuoc);
        double khoiLuongTheTichSauHut = boTinhKhoiLuongTheTich.tinhSauHutChanKhong(kichThuoc);
        double khoiLuongTheTich = boTinhKhoiLuongTheTich.tinh(kichThuoc, coHutChanKhong);

        // 2. Xác định trọng lượng tính cước
        double trongLuongTinhCuoc = boXacDinhTrongLuong.tinh(kichThuoc, canNangThuc, coHutChanKhong);

        // 3. Tính cước cơ bản qua Port
        double cuocPhiCoBan = chinhSachCuoc.tinhCuocCoBan(trongLuongTinhCuoc);

        // 4. Mức cước chuẩn chưa hút chân không để tính tiền tiết kiệm
        double trongLuongChuaHut = Math.max(canNangThuc, khoiLuongTheTichBanDau);
        double cuocChuaHut = chinhSachCuoc.tinhCuocCoBan(trongLuongChuaHut);

        // 5. Tính phụ phí dịch vụ gia tăng qua Port (Tuân thủ OCP - khử trùng lặp dịch vụ)
        double tongPhuPhi = 0.0;
        double phuPhiHutChanKhong = 0.0;
        double phuPhiGoiQua = 0.0;
        double phuPhiThiep = 0.0;

        Set<String> cacDichVuDaTinh = new HashSet<>();
        for (String dv : yeuCau.getDanhSachDichVuGiaTang()) {
            if (dv == null) continue;
            String keyChuan = dv.trim().toLowerCase().replace("_", "").replace(" ", "");
            if (cacDichVuDaTinh.add(keyChuan)) {
                double phi = chinhSachCuoc.layPhuPhiDichVu(dv);
                tongPhuPhi += phi;

                if (keyChuan.contains("hut") || keyChuan.contains("hút")) {
                    phuPhiHutChanKhong = phi;
                } else if (keyChuan.contains("goi") || keyChuan.contains("gói")) {
                    phuPhiGoiQua = phi;
                } else if (keyChuan.contains("thiep") || keyChuan.contains("thiệp")) {
                    phuPhiThiep = phi;
                }
            }
        }

        // 6. Tính tổng tiền cước và tiền tiết kiệm
        double tongTienCuoc = cuocPhiCoBan + tongPhuPhi;
        double tienTietKiem = coHutChanKhong ? Math.max(0.0, cuocChuaHut - (cuocPhiCoBan + phuPhiHutChanKhong)) : 0.0;

        // 7. Tạo chuỗi tóm tắt
        String tomTat = String.format(
                "Tính cước: Thể tích ban đầu: %.1f kg, Thể tích hiệu lực: %.1f kg, Trọng lượng tính cước: %.1f kg | Cước cơ bản: %.0f VND, Phụ phí: %.0f VND | Tổng cước: %.0f VND",
                khoiLuongTheTichBanDau, khoiLuongTheTich, trongLuongTinhCuoc, cuocPhiCoBan, tongPhuPhi, tongTienCuoc
        );

        KetQuaTinhCuocDTO ketQua = new KetQuaTinhCuocDTO(
                khoiLuongTheTichBanDau,
                khoiLuongTheTichSauHut,
                khoiLuongTheTich,
                trongLuongTinhCuoc,
                cuocPhiCoBan,
                tongPhuPhi,
                tongTienCuoc,
                cuocChuaHut,
                tienTietKiem,
                phuPhiHutChanKhong,
                phuPhiGoiQua,
                phuPhiThiep,
                tomTat
        );

        // 8. Lưu kết quả qua Output Port
        khoLuuTru.luuKetQua(ketQua);

        return ketQua;
    }
}
