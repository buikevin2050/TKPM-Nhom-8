package starter.stepdefinitions;

import io.cucumber.datatable.DataTable;
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
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

/**
 * Cucumber Step Definitions cho Phân hệ 4: Tính cước vận chuyển cồng kềnh & Dịch vụ gia tăng.
 * Ánh xạ kịch bản Gherkin tiếng Việt vào tầng Application và Infrastructure thuần tiếng Việt.
 * Sử dụng AssertJ assertions độc quyền.
 */
public class CuocVanChuyenStepDefinitions {

    private final ChinhSachCuocTieuChuanAdapter chinhSachCuocAdapter = new ChinhSachCuocTieuChuanAdapter();
    private final LuuTruCuocPhiInMemoryAdapter luuTruAdapter = new LuuTruCuocPhiInMemoryAdapter();
    private final TinhCuocVanChuyenUseCase useCase = new TinhCuocVanChuyenUseCase(chinhSachCuocAdapter, luuTruAdapter);

    private double chieuDai;
    private double chieuRong;
    private double chieuCao;
    private double canNangThuc;
    private boolean coHutChanKhong;
    private final List<String> danhSachDichVuGiaTang = new ArrayList<>();

    private YeuCauTinhCuocDTO yeuCau;
    private KetQuaTinhCuocDTO ketQua;

    @Before
    public void lamMoiTrangThaiKichBan() {
        luuTruAdapter.xoaDuLieu();
        danhSachDichVuGiaTang.clear();
        coHutChanKhong = false;
        chieuDai = 0;
        chieuRong = 0;
        chieuCao = 0;
        canNangThuc = 0;
        yeuCau = null;
        ketQua = null;
    }

    // ==========================================
    // Background Steps
    // ==========================================

    @Given("^chính sách cước cơ sở là (?:.+) VND cho (?:.+) kg đầu tiên và (?:.+) VND cho mỗi (?:.+) kg tiếp theo$")
    public void thietLapChinhSachCuoc() {
        assertThat(chinhSachCuocAdapter).isNotNull();
    }

    @Given("biểu phí vận chuyển tiêu chuẩn có giá khởi điểm là {double} VNĐ cho {double} kg đầu tiên")
    public void thietLapChinhSachCuocDouble(double giaKhoiDiem, double kgDau) {
        assertThat(giaKhoiDiem).isEqualTo(20000.0);
        assertThat(kgDau).isEqualTo(1.0);
    }

    @Given("^phần trọng lượng sau (?:.+) kg được làm tròn lên theo bước (?:.+) kg, tương đương (?:.+) VND cho mỗi bước (?:.+) kg$")
    public void thietLapQuyTacLamTron() {
        assertThat(ChinhSachCuocTieuChuanAdapter.GIA_MOI_BUOC).isEqualTo(2500.0);
    }

    @Given("^khối lượng thể tích bằng chiều dài nhân chiều rộng nhân chiều cao tính bằng cm rồi chia cho (?:\\d+)$")
    public void thietLapQuyTacKhoiLuongTheTich() {
        assertThat(TinhKhoiLuongTheTich.HE_SO_QUY_DOI).isEqualTo(5000.0);
    }

    @Given("trọng lượng tính cước bằng giá trị lớn hơn giữa cân nặng thực tế và khối lượng thể tích")
    public void thietLapQuyTacTrongLuongTinhCuoc() {
        assertThat(useCase).isNotNull();
    }

    @Given("^dịch vụ hút chân không giảm (?:\\d+)% thể tích đóng gói và có phụ phí cố định (?:.+) VND$")
    public void thietLapQuyTacHutChanKhong() {
        assertThat(TinhKhoiLuongTheTich.TI_LE_GIAM_THE_TICH_HUT).isEqualTo(0.5);
        assertThat(TinhCuocVanChuyenUseCase.PHI_HUT_CHAN_KHONG).isEqualTo(15000.0);
    }

    @Given("^phí gói quà là (?:.+) VND và phí thiệp chúc mừng là (?:.+) VND$")
    public void thietLapPhuPhiDichVu() {
        assertThat(TinhCuocVanChuyenUseCase.PHI_GOI_QUA).isEqualTo(20000.0);
        assertThat(TinhCuocVanChuyenUseCase.PHI_THIEP).isEqualTo(10000.0);
    }

    // ==========================================
    // Package Specifications (Given / When)
    // ==========================================

    @Given("^kiện(?: hàng| thú bông)? có cân nặng thực tế ([0-9.,]+) kg và kích thước ([0-9.,]+) cm x ([0-9.,]+) cm x ([0-9.,]+) cm$")
    public void ganCanNangVaKichThuoc(String canNangStr, String daiStr, String rongStr, String caoStr) {
        this.canNangThuc = phanTichSo(canNangStr);
        this.chieuDai = phanTichSo(daiStr);
        this.chieuRong = phanTichSo(rongStr);
        this.chieuCao = phanTichSo(caoStr);
        this.coHutChanKhong = false;
        this.danhSachDichVuGiaTang.clear();
    }

    @Given("^yêu cầu tính cước cho kiện thú bông nặng ([0-9.,]+) kg, kích thước ([0-9.,]+) cm x ([0-9.,]+) cm x ([0-9.,]+) cm$")
    public void ganYeuCauThuBong(String canNangStr, String daiStr, String rongStr, String caoStr) {
        ganCanNangVaKichThuoc(canNangStr, daiStr, rongStr, caoStr);
    }

    @Given("^yêu cầu tính cước có kiện thú bông cỡ (.+) với cân nặng thực tế ([0-9.,]+) kg và kích thước ([0-9.,]+) x ([0-9.,]+) x ([0-9.,]+) cm$")
    public void ganYeuCauCoKichCo(String kichCo, String canNangStr, String daiStr, String rongStr, String caoStr) {
        ganCanNangVaKichThuoc(canNangStr, daiStr, rongStr, caoStr);
    }

    @Given("^Request Model tính cước gồm kiện thú bông nặng ([0-9.,]+) kg, kích thước ([0-9.,]+) x ([0-9.,]+) x ([0-9.,]+) cm$")
    public void ganRequestModel(String canNangStr, String daiStr, String rongStr, String caoStr) {
        ganCanNangVaKichThuoc(canNangStr, daiStr, rongStr, caoStr);
    }

    @Given("^yêu cầu tính cước có dữ liệu kiện hàng:$")
    public void ganDuLieuTuDataTable(DataTable dataTable) {
        List<Map<String, String>> hang = dataTable.asMaps(String.class, String.class);
        if (!hang.isEmpty()) {
            Map<String, String> dong = hang.get(0);
            this.canNangThuc = phanTichSo(dong.get("Cân nặng thực tế (kg)"));
            this.chieuDai = phanTichSo(dong.get("Dài (cm)"));
            this.chieuRong = phanTichSo(dong.get("Rộng (cm)"));
            this.chieuCao = phanTichSo(dong.get("Cao (cm)"));
            this.coHutChanKhong = false;
            this.danhSachDichVuGiaTang.clear();
        }
    }

    @Given("yêu cầu không chọn dịch vụ hút chân không")
    public void khongChonHutChanKhong() {
        this.coHutChanKhong = false;
    }

    @Given("^khối lượng thể tích ban đầu của kiện hàng là ([0-9.,]+) kg$")
    public void kiemTraTheTichBanDauGiven(String expected) {
        double expectedVol = phanTichSo(expected);
        double actualVol = (chieuDai * chieuRong * chieuCao) / TinhKhoiLuongTheTich.HE_SO_QUY_DOI;
        assertThat(actualVol).isCloseTo(expectedVol, offset(0.01));
    }

    @When("kiện hàng có chiều dài {double} cm, chiều rộng {double} cm, chiều cao {double} cm")
    public void ganKichThuocDouble(double dai, double rong, double cao) {
        this.chieuDai = dai;
        this.chieuRong = rong;
        this.chieuCao = cao;
    }

    @Given("khách hàng chọn hút chân không, gói quà sinh nhật và thiệp chúc mừng")
    public void chonGoiSinhNhatTronBo() {
        this.coHutChanKhong = true;
        this.danhSachDichVuGiaTang.clear();
        this.danhSachDichVuGiaTang.add("HUT_CHAN_KHONG");
        this.danhSachDichVuGiaTang.add("GOI_QUA");
        this.danhSachDichVuGiaTang.add("THIEP");
    }

    @Given("khách hàng chọn hút chân không, gói quà sinh nhật và viết thiệp chúc mừng")
    public void chonGoiSinhNhatTronBoVi() {
        chonGoiSinhNhatTronBo();
    }

    // ==========================================
    // Use Case Execution (When)
    // ==========================================

    @When("hệ thống tính cước vận chuyển không áp dụng hút chân không")
    public void tinhCuocKhongHutChanKhong() {
        this.coHutChanKhong = false;
        this.yeuCau = new YeuCauTinhCuocDTO(chieuDai, chieuRong, chieuCao, canNangThuc, false, danhSachDichVuGiaTang);
        this.ketQua = useCase.thucThi(yeuCau);
    }

    @When("hệ thống tính cước có áp dụng dịch vụ hút chân không")
    public void tinhCuocCoHutChanKhong() {
        this.coHutChanKhong = true;
        if (!this.danhSachDichVuGiaTang.contains("HUT_CHAN_KHONG")) {
            this.danhSachDichVuGiaTang.add("HUT_CHAN_KHONG");
        }
        this.yeuCau = new YeuCauTinhCuocDTO(chieuDai, chieuRong, chieuCao, canNangThuc, true, danhSachDichVuGiaTang);
        this.ketQua = useCase.thucThi(yeuCau);
    }

    @When("Use Case tính cước giao hàng xử lý yêu cầu có dịch vụ hút chân không")
    public void xuLyYeuCauCoHutChanKhongVi() {
        tinhCuocCoHutChanKhong();
    }

    @When("hệ thống tính cước và lưu kết quả qua ShippingStorePort")
    public void tinhCuocVaLuuKetQua() {
        this.yeuCau = new YeuCauTinhCuocDTO(chieuDai, chieuRong, chieuCao, canNangThuc, coHutChanKhong, danhSachDichVuGiaTang);
        this.ketQua = useCase.thucThi(yeuCau);
    }

    @When("Use Case tính cước giao hàng xử lý yêu cầu")
    public void xuLyYeuCauGiaoHangVi() {
        tinhCuocVaLuuKetQua();
    }

    // ==========================================
    // Verifications & Assertions (Then / And)
    // ==========================================

    @Then("^khối lượng thể tích là ([0-9.,]+) kg$")
    @Then("^Response Model phải trả về khối lượng thể tích là ([0-9.,]+) kg$")
    public void kiemTraKhoiLuongTheTich(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getKhoiLuongTheTich()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @Then("khối lượng thể tích quy đổi phải bằng {double} kg")
    public void kiemTraKhoiLuongTheTichDouble(double expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getKhoiLuongTheTich()).isCloseTo(expected, offset(0.01));
    }

    @Then("^khối lượng thể tích ban đầu là ([0-9.,]+) kg$")
    public void kiemTraKhoiLuongTheTichBanDau(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getKhoiLuongTheTichBanDau()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @Then("^khối lượng thể tích sau hút chân không là ([0-9.,]+) kg$")
    @Then("^Response Model phải trả về khối lượng thể tích sau hút chân không là ([0-9.,]+) kg$")
    public void kiemTraKhoiLuongTheTichSauHut(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getKhoiLuongTheTich()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @And("^trọng lượng tính cước là ([0-9.,]+) kg$")
    @And("^Response Model phải trả về trọng lượng tính cước là ([0-9.,]+) kg$")
    public void kiemTraTrongLuongTinhCuoc(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTrongLuongTinhCuoc()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @And("trọng lượng dùng để tính cước phải là {double} kg")
    public void kiemTraTrongLuongTinhCuocDouble(double expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTrongLuongTinhCuoc()).isCloseTo(expected, offset(0.01));
    }

    @And("^trọng lượng tính cước sau hút chân không là ([0-9.,]+) kg$")
    @And("^Response Model phải trả về trọng lượng tính cước sau hút chân không là ([0-9.,]+) kg$")
    public void kiemTraTrongLuongTinhCuocSauHut(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTrongLuongTinhCuoc()).isCloseTo(phanTichSo(expected), offset(0.01));
    }

    @And("^cước cơ sở là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^Response Model phải trả về cước cơ sở là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraCuocCoSo(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getCuocPhiCoBan()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("cước phí vận chuyển cơ bản phải bằng {double} VNĐ")
    public void kiemTraCuocCoSoDouble(double expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getCuocPhiCoBan()).isCloseTo(expected, offset(0.01));
    }

    @And("^cước vận chuyển không hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^cước cơ sở khi không hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^Response Model phải trả về cước cơ sở khi không hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraCuocChuaHut(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getCuocChuaHut()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^tổng cước sau khi cộng phí hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^tổng cước sau khi cộng phụ phí hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^Response Model phải trả về tổng cước sau khi cộng phụ phí hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraTongCuocSauHut(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTongTienCuoc()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^khách hàng tiết kiệm được ([0-9.,]+) (?:VND|VNĐ) so với không hút chân không$")
    @And("^số tiền tiết kiệm so với không hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^Response Model phải trả về số tiền tiết kiệm so với không hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraTienTietKiem(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTienTietKiem()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^phụ phí hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^Response Model phải trả về phụ phí hút chân không là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraPhuPhiHutChanKhong(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getPhuPhiHutChanKhong()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^phụ phí gói quà là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^Response Model phải trả về phụ phí gói quà là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraPhuPhiGoiQua(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getPhuPhiGoiQua()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^phụ phí thiệp chúc mừng là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^Response Model phải trả về phụ phí viết thiệp là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraPhuPhiThiep(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getPhuPhiThiep()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^tổng chi phí là ([0-9.,]+) (?:VND|VNĐ)$")
    @And("^Response Model phải trả về tổng chi phí là ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraTongChiPhi(String expected) {
        assertThat(ketQua).isNotNull();
        assertThat(ketQua.getTongTienCuoc()).isCloseTo(phanTichTienTe(expected), offset(0.01));
    }

    @And("^ShippingStorePort nhận được kết quả đã lưu với tổng chi phí ([0-9.,]+) (?:VND|VNĐ)$")
    public void kiemTraLuuKetQuaStorePort(String expectedFeeStr) {
        double expectedFee = phanTichTienTe(expectedFeeStr);
        Optional<KetQuaTinhCuocDTO> moiNhat = luuTruAdapter.layKetQuaMoiNhat();
        assertThat(moiNhat).isPresent();
        assertThat(moiNhat.get().getTongTienCuoc()).isCloseTo(expectedFee, offset(0.01));
    }

    @And("StoreAble Port phải nhận lưu kết quả tính cước tương ứng với Response Model đó")
    public void kiemTraLuuKetQuaStorePortKhongThamSo() {
        Optional<KetQuaTinhCuocDTO> moiNhat = luuTruAdapter.layKetQuaMoiNhat();
        assertThat(moiNhat).isPresent();
        assertThat(moiNhat.get()).isEqualTo(ketQua);
    }

    // ==========================================
    // Tiện ích chuyển đổi chuỗi số và tiền tệ
    // ==========================================

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
