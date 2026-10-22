package starter.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import starter.application.vanchuyen.KetQuaTinhCuocDTO;
import starter.application.vanchuyen.TinhCuocVanChuyenUseCase;
import starter.application.vanchuyen.YeuCauTinhCuocDTO;
import starter.domain.vanchuyen.TinhKhoiLuongTheTich;
import starter.infrastructure.vanchuyen.ChinhSachCuocTieuChuanAdapter;
import starter.infrastructure.vanchuyen.LuuTruCuocPhiInMemoryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

/**
 * Step Definitions cho Phân hệ 4: Tính cước vận chuyển cồng kềnh & Dịch vụ gia tăng.
 * Áp dụng phương pháp luận OOPSI BDD, liên kết trực tiếp với Use Case Interactor.
 */
public class CuocVanChuyenStepDefinitions {

    private final ChinhSachCuocTieuChuanAdapter chinhSachCuoc = new ChinhSachCuocTieuChuanAdapter();
    private final LuuTruCuocPhiInMemoryAdapter khoLuuTru = new LuuTruCuocPhiInMemoryAdapter();
    private final TinhCuocVanChuyenUseCase useCase = new TinhCuocVanChuyenUseCase(chinhSachCuoc, khoLuuTru);

    private double chieuDai;
    private double chieuRong;
    private double chieuCao;
    private double canNangThuc;
    private boolean coHutChanKhong;
    private final List<String> danhSachDichVuGiaTang = new ArrayList<>();

    private YeuCauTinhCuocDTO yeuCau;
    private KetQuaTinhCuocDTO ketQua;
    private Exception caughtException;

    @Before
    public void lamMoiTrangThaiKichBan() {
        khoLuuTru.xoaDuLieu();
        danhSachDichVuGiaTang.clear();
        coHutChanKhong = false;
        chieuDai = 0;
        chieuRong = 0;
        chieuCao = 0;
        canNangThuc = 0;
        yeuCau = null;
        ketQua = null;
        caughtException = null;
    }

    // =========================================================================
    // Background Steps
    // =========================================================================

    @Given("^biểu phí vận chuyển tiêu chuẩn áp dụng nấc cơ bản (?:.+) VNĐ cho (?:.+) kg đầu tiên$")
    public void thietLapNacCoBan() {
        assertThat(ChinhSachCuocTieuChuanAdapter.CUOC_1KG_DAU).isCloseTo(20000.0, offset(0.01));
    }

    @Given("^nấc lũy tiến là (?:.+) VNĐ cho mỗi (?:.+) kg tiếp theo sau (?:.+) kg đầu$")
    public void thietLapNacLuyTien() {
        assertThat(ChinhSachCuocTieuChuanAdapter.GIA_MOI_BUOC).isCloseTo(5000.0, offset(0.01));
        assertThat(ChinhSachCuocTieuChuanAdapter.BUOC_TRONG_LUONG_KG).isCloseTo(0.5, offset(0.01));
    }

    @Given("^công thức quy đổi thể tích bưu chính quốc tế bằng chiều dài nhân chiều rộng nhân chiều cao chia cho (?:\\d+)$")
    public void thietLapCongThucTheTich() {
        assertThat(TinhKhoiLuongTheTich.HE_SO_QUY_DOI).isCloseTo(5000.0, offset(0.01));
    }

    @Given("^dịch vụ hút chân không giảm (?:\\d+)% thể tích đóng gói với phụ phí cố định (?:.+) VNĐ$")
    public void thietLapDichVuHutChanKhong() {
        assertThat(TinhKhoiLuongTheTich.TI_LE_GIAM_THE_TICH_HUT).isCloseTo(0.5, offset(0.01));
        assertThat(chinhSachCuoc.layPhuPhiDichVu("HUT_CHAN_KHONG")).isCloseTo(15000.0, offset(0.01));
    }

    @Given("^phụ phí gói quà sinh nhật là (?:.+) VNĐ và viết thiệp chúc mừng là (?:.+) VNĐ$")
    public void thietLapPhuPhiGiaTang() {
        assertThat(chinhSachCuoc.layPhuPhiDichVu("GOI_QUA")).isCloseTo(20000.0, offset(0.01));
        assertThat(chinhSachCuoc.layPhuPhiDichVu("VIET_THIEP")).isCloseTo(10000.0, offset(0.01));
    }

    // =========================================================================
    // Given Steps: Thiết lập dữ liệu kiện hàng & Dịch vụ gia tăng
    // =========================================================================

    @Given("^kiện hàng có cân nặng thực tế ([0-9.,]+) kg và kích thước ([0-9.,]+) cm x ([0-9.,]+) cm x ([0-9.,]+) cm$")
    public void thietLapKienHang(String canNangStr, String daiStr, String rongStr, String caoStr) {
        this.canNangThuc = phanTichSo(canNangStr);
        this.chieuDai = phanTichSo(daiStr);
        this.chieuRong = phanTichSo(rongStr);
        this.chieuCao = phanTichSo(caoStr);
        this.coHutChanKhong = false;
        this.danhSachDichVuGiaTang.clear();
    }

    @Given("^kiện hàng có kích thước ([0-9.,-]+) cm x ([0-9.,-]+) cm x ([0-9.,-]+) cm và cân nặng ([0-9.,-]+) kg$")
    public void thietLapKichThuocVaCanNang(String daiStr, String rongStr, String caoStr, String canNangStr) {
        this.chieuDai = phanTichSo(daiStr);
        this.chieuRong = phanTichSo(rongStr);
        this.chieuCao = phanTichSo(caoStr);
        this.canNangThuc = phanTichSo(canNangStr);
    }

    @Given("^kiện hàng có thông tin kích thước ([0-9.,-]+) cm x ([0-9.,-]+) cm x ([0-9.,-]+) cm và cân nặng ([0-9.,-]+) kg$")
    public void thietLapThongTinKichThuocVaCanNang(String daiStr, String rongStr, String caoStr, String canNangStr) {
        thietLapKichThuocVaCanNang(daiStr, rongStr, caoStr, canNangStr);
    }

    @And("^khách hàng chọn các dịch vụ gia tăng \"([^\"]*)\"$")
    public void chonCacDichVuGiaTang(String dsDichVu) {
        napDanhSachDichVu(dsDichVu);
    }

    @And("^dịch vụ gia tăng được chọn là \"([^\"]*)\"$")
    public void chonDichVuGiaTangDuocChon(String dsDichVu) {
        napDanhSachDichVu(dsDichVu);
    }

    private void napDanhSachDichVu(String dsDichVu) {
        danhSachDichVuGiaTang.clear();
        this.coHutChanKhong = false;
        if (dsDichVu != null && !dsDichVu.isBlank() && !dsDichVu.equalsIgnoreCase("KHONG")) {
            String[] parts = dsDichVu.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                danhSachDichVuGiaTang.add(trimmed);
                String normalized = trimmed.toLowerCase().replace("_", "").replace(" ", "");
                if (normalized.contains("hut") || normalized.contains("hút")) {
                    this.coHutChanKhong = true;
                }
            }
        }
    }

    // =========================================================================
    // When Steps: Thực thi Use Case tính cước
    // =========================================================================

    @When("hệ thống tính cước vận chuyển không áp dụng hút chân không")
    public void thucHienTinhCuocKhongHut() {
        this.coHutChanKhong = false;
        this.yeuCau = new YeuCauTinhCuocDTO(chieuDai, chieuRong, chieuCao, canNangThuc, false, danhSachDichVuGiaTang);
        this.ketQua = useCase.thucThi(yeuCau);
    }

    @When("hệ thống tính cước có áp dụng dịch vụ hút chân không")
    public void thucHienTinhCuocCoHut() {
        this.coHutChanKhong = true;
        if (!danhSachDichVuGiaTang.contains(ChinhSachCuocTieuChuanAdapter.DICH_VU_HUT_CHAN_KHONG)) {
            danhSachDichVuGiaTang.add(ChinhSachCuocTieuChuanAdapter.DICH_VU_HUT_CHAN_KHONG);
        }
        this.yeuCau = new YeuCauTinhCuocDTO(chieuDai, chieuRong, chieuCao, canNangThuc, true, danhSachDichVuGiaTang);
        this.ketQua = useCase.thucThi(yeuCau);
    }

    @When("hệ thống tính cước và lưu kết quả vào hệ thống")
    public void thucHienTinhCuocVaLuuHeThong() {
        this.yeuCau = new YeuCauTinhCuocDTO(chieuDai, chieuRong, chieuCao, canNangThuc, coHutChanKhong, danhSachDichVuGiaTang);
        this.ketQua = useCase.thucThi(yeuCau);
    }

    @When("hệ thống thực hiện tính toán cước phí vận chuyển")
    public void thucHienTinhToanCuocPhiVanChuyen() {
        this.yeuCau = new YeuCauTinhCuocDTO(chieuDai, chieuRong, chieuCao, canNangThuc, coHutChanKhong, danhSachDichVuGiaTang);
        this.ketQua = useCase.thucThi(yeuCau);
    }

    @When("hệ thống kiểm tra và tính cước kiện hàng")
    public void kiemTraVaTinhCuocKienHang() {
        try {
            this.yeuCau = new YeuCauTinhCuocDTO(chieuDai, chieuRong, chieuCao, canNangThuc, coHutChanKhong, danhSachDichVuGiaTang);
            this.ketQua = useCase.thucThi(yeuCau);
        } catch (Exception ex) {
            this.caughtException = ex;
        }
    }

    // =========================================================================
    // Then Steps: Kiểm chứng kết quả đầu ra (OOPSI Output)
    // =========================================================================

    @Then("^khối lượng thể tích là ([0-9.,]+) kg$")
    public void kiemTraKhoiLuongTheTich(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getKhoiLuongTheTich()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @Then("^khối lượng thể tích ban đầu là ([0-9.,]+) kg$")
    public void kiemTraKhoiLuongTheTichBanDau(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getKhoiLuongTheTichBanDau()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @And("^khối lượng thể tích sau hút chân không là ([0-9.,]+) kg$")
    public void kiemTraKhoiLuongTheTichSauHut(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getKhoiLuongTheTichSauHut()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @And("^trọng lượng tính cước là ([0-9.,]+) kg$")
    public void kiemTraTrongLuongTinhCuoc(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTrongLuongTinhCuoc()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @And("^trọng lượng tính cước sau hút chân không là ([0-9.,]+) kg$")
    public void kiemTraTrongLuongTinhCuocSauHut(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTrongLuongTinhCuoc()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @And("^cước cơ bản là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraCuocCoBan(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getCuocPhiCoBan()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^phụ phí hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraPhuPhiHutChanKhong(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getPhuPhiHutChanKhong()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^phụ phí gói quà là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraPhuPhiGoiQua(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getPhuPhiGoiQua()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^phụ phí thiệp chúc mừng là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraPhuPhiThiep(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getPhuPhiThiep()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^tổng phụ phí là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraTongPhuPhi(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTongPhuPhi()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^tổng cước thanh toán là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraTongCuocThanhToan(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTongTienCuoc()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("hệ thống tạo mã phiếu tính cước hợp lệ và lưu trữ kết quả thành công")
    public void kiemTraLuuKetQuaThanhCong() {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getMaPhieuTinhCuoc()).isNotNull().startsWith("VC-");
        Optional<KetQuaTinhCuocDTO> luuMoiNhat = khoLuuTru.layKetQuaMoiNhat();
        assertThat(luuMoiNhat).isPresent();
        assertThat(luuMoiNhat.get().getMaPhieuTinhCuoc()).isEqualTo(ketQua.getMaPhieuTinhCuoc());
        assertThat(luuMoiNhat.get().getTongTienCuoc()).isEqualTo(ketQua.getTongTienCuoc());
    }

    @Then("^hệ thống từ chối tính cước với thông báo lỗi \"([^\"]*)\"$")
    public void kiemTraThongBaoLoi(String thongBaoLoiMongMuon) {
        assertThat(caughtException)
                .as("Hệ thống phải từ chối yêu cầu và ném IllegalArgumentException")
                .isNotNull()
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(caughtException.getMessage()).isEqualTo(thongBaoLoiMongMuon);
    }

    @Then("^hệ thống hiển thị cảnh báo \"([^\"]*)\"$")
    public void kiemTraHienThiCanhBao(String canhBaoMongMuon) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.isVuotTranVanChuyen()).isTrue();
        assertThat(ketQua.getCanhBao()).isEqualTo(canhBaoMongMuon);
    }

    // =========================================================================
    // Tiện ích phân tích chuỗi số & Tiền tệ
    // =========================================================================

    private double phanTichSo(String raw) {
        if (raw == null) return 0.0;
        String cleaned = raw.replaceAll("[^0-9,.-]", "").trim();
        cleaned = cleaned.replace(",", ".");
        return Double.parseDouble(cleaned);
    }

    private double phanTichTienTe(String raw) {
        if (raw == null) return 0.0;
        String cleaned = raw.replaceAll("[^0-9,.-]", "").trim();
        cleaned = cleaned.replace(".", "").replace(",", ".");
        return Double.parseDouble(cleaned);
    }
}
