package starter.application.vanchuyen;

import starter.domain.vanchuyen.KichThuoc;
import starter.domain.vanchuyen.TinhKhoiLuongTheTich;
import starter.domain.vanchuyen.XacDinhTrongLuongTinhCuoc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Use Case Interactor (Control) điều phối nghiệp vụ tính cước vận chuyển cồng kềnh.
 * Tuân thủ Single Responsibility Principle (SRP), Dependency Inversion Principle (DIP)
 * và Open-Closed Principle (OCP) - tra cứu phụ phí dịch vụ gia tăng hoàn toàn qua ChinhSachCuocPort.
 */
public class TinhCuocVanChuyenUseCase {

    public static final double TRAN_CHIEU_DAI_CM = 200.0;
    public static final double TRAN_THE_TICH_KG = 100.0;
    public static final String CANH_BAO_VUOT_TRAN = "Vượt quá kích thước tối đa của vận chuyển tiêu chuẩn, chuyển sang đường vận chuyển đặc biệt";

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

        // 1. Tính toán các mức khối lượng thể tích quy đổi
        double khoiLuongTheTichBanDau = boTinhKhoiLuongTheTich.tinh(kichThuoc);
        double khoiLuongTheTichSauHut = boTinhKhoiLuongTheTich.tinhSauHutChanKhong(kichThuoc);
        double khoiLuongTheTich = boTinhKhoiLuongTheTich.tinh(kichThuoc, coHutChanKhong);

        // 2. Xác định trọng lượng tính cước
        double trongLuongTinhCuoc = boXacDinhTrongLuong.tinh(kichThuoc, canNangThuc, coHutChanKhong);

        // 3. Tính cước cơ bản qua Port theo biểu phí bậc thang
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

        // 7. Kiểm tra kiện hàng vượt trần vận chuyển tiêu chuẩn
        boolean vuotTran = (kichThuoc.getChieuDai() > TRAN_CHIEU_DAI_CM) || (khoiLuongTheTichBanDau > TRAN_THE_TICH_KG);
        String canhBao = vuotTran ? CANH_BAO_VUOT_TRAN : null;

        // 8. Sinh mã phiếu tính cước hệ thống
        String maPhieuTinhCuoc = "VC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 9. Tạo chuỗi tóm tắt
        String tomTat = String.format(
                "Mã phiếu: %s | Thể tích ban đầu: %.2f kg, Thể tích hiệu lực: %.2f kg, Trọng lượng tính cước: %.2f kg | Cước cơ bản: %.0f VND, Phụ phí: %.0f VND | Tổng cước: %.0f VND%s",
                maPhieuTinhCuoc, khoiLuongTheTichBanDau, khoiLuongTheTich, trongLuongTinhCuoc,
                cuocPhiCoBan, tongPhuPhi, tongTienCuoc,
                (canhBao != null ? " [CẢNH BÁO: " + canhBao + "]" : "")
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
                tomTat,
                maPhieuTinhCuoc,
                vuotTran,
                canhBao
        );

        // 10. Lưu kết quả qua Output Port
        khoLuuTru.luuKetQua(ketQua);

        return ketQua;
    }
}
