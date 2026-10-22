package starter.application.vanchuyen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import starter.infrastructure.vanchuyen.ChinhSachCuocTieuChuanAdapter;
import starter.infrastructure.vanchuyen.LuuTruCuocPhiInMemoryAdapter;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Kiểm thử Use Case: TinhCuocVanChuyenUseCase")
class KhiTinhCuocVanChuyenUseCaseTest {

    private ChinhSachCuocTieuChuanAdapter chinhSachCuocAdapter;
    private LuuTruCuocPhiInMemoryAdapter luuTruAdapter;
    private TinhCuocVanChuyenUseCase useCase;

    @BeforeEach
    void khoiTao() {
        chinhSachCuocAdapter = new ChinhSachCuocTieuChuanAdapter();
        luuTruAdapter = new LuuTruCuocPhiInMemoryAdapter();
        useCase = new TinhCuocVanChuyenUseCase(chinhSachCuocAdapter, luuTruAdapter);
    }

    @Test
    @DisplayName("Ném ngoại lệ khi kích thước trong yêu cầu không hợp lệ (<= 0)")
    void nemNgoaiLeKhiKichThuocKhongHopLe() {
        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(-10, 20, 20, 2.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kích thước kiện hàng phải lớn hơn 0");

        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(20, 0, 20, 2.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kích thước kiện hàng phải lớn hơn 0");

        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(20, 20, -5, 2.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kích thước kiện hàng phải lớn hơn 0");
    }

    @Test
    @DisplayName("Ném ngoại lệ khi cân nặng thực tế trong yêu cầu không hợp lệ (<= 0)")
    void nemNgoaiLeKhiCanNangThucKhongHopLe() {
        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(20, 20, 20, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cân nặng thực tế phải lớn hơn 0");

        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(20, 20, 20, -1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cân nặng thực tế phải lớn hơn 0");
    }

    @Test
    @DisplayName("Tính cước kiện hàng tiêu chuẩn không hút chân không và lưu kết quả vào kho")
    void tinhCuocKienHangTieuChuan() {
        // Given: 4.0 kg thực tế, 20x20x20 cm (thể tích = 1.6 kg)
        YeuCauTinhCuocDTO yeuCau = new YeuCauTinhCuocDTO(20, 20, 20, 4.0);

        // When
        KetQuaTinhCuocDTO ketQua = useCase.thucThi(yeuCau);

        // Then
        assertThat(ketQua.getKhoiLuongTheTich()).isEqualTo(1.6);
        assertThat(ketQua.getTrongLuongTinhCuoc()).isEqualTo(4.0);
        // Cước cơ bản bậc thang: 20.000 cho 1kg đầu + 6 bước * 5000 = 50.000 VND
        assertThat(ketQua.getCuocPhiCoBan()).isEqualTo(50000.0);
        assertThat(ketQua.getTongPhuPhi()).isEqualTo(0.0);
        assertThat(ketQua.getTongTienCuoc()).isEqualTo(50000.0);
        assertThat(ketQua.getMaPhieuTinhCuoc()).startsWith("VC-");
        assertThat(ketQua.isVuotTranVanChuyen()).isFalse();
        assertThat(ketQua.getCanhBao()).isNull();

        // Kiểm tra lưu kết quả qua Port
        Optional<KetQuaTinhCuocDTO> daLuu = luuTruAdapter.layKetQuaMoiNhat();
        assertThat(daLuu).isPresent();
        assertThat(daLuu.get()).isEqualTo(ketQua);
    }

    @Test
    @DisplayName("Tính cước có hút chân không và xác định số tiền tiết kiệm")
    void tinhCuocCoHutChanKhongVaTietKiem() {
        // Given: 2.0 kg thực tế, 100x60x50 cm (ban đầu 60.0 kg, sau hút 30.0 kg)
        YeuCauTinhCuocDTO yeuCau = new YeuCauTinhCuocDTO(100, 60, 50, 2.0, true, List.of("HUT_CHAN_KHONG"));

        // When
        KetQuaTinhCuocDTO ketQua = useCase.thucThi(yeuCau);

        // Then
        assertThat(ketQua.getKhoiLuongTheTichBanDau()).isEqualTo(60.0);
        assertThat(ketQua.getKhoiLuongTheTichSauHut()).isEqualTo(30.0);
        assertThat(ketQua.getTrongLuongTinhCuoc()).isEqualTo(30.0);
        // Cước chưa hút 60kg: 20.000 + 118 * 5000 = 610.000 VND
        assertThat(ketQua.getCuocChuaHut()).isEqualTo(610000.0);
        // Cước sau hút 30kg: 20.000 + 58 * 5000 = 310.000 VND
        assertThat(ketQua.getCuocPhiCoBan()).isEqualTo(310000.0);
        assertThat(ketQua.getPhuPhiHutChanKhong()).isEqualTo(15000.0);
        assertThat(ketQua.getTongTienCuoc()).isEqualTo(325000.0);
        // Tiết kiệm: 610.000 - 325.000 = 285.000 VND
        assertThat(ketQua.getTienTietKiem()).isEqualTo(285000.0);
    }

    @Test
    @DisplayName("Tính cước trọn bộ gói sinh nhật đầy đủ phụ phí")
    void tinhCuocTronBoGoiSinhNhat() {
        List<String> dichVu = List.of("HUT_CHAN_KHONG", "GOI_QUA", "VIET_THIEP");
        YeuCauTinhCuocDTO yeuCau = new YeuCauTinhCuocDTO(100, 60, 50, 2.0, true, dichVu);

        // When
        KetQuaTinhCuocDTO ketQua = useCase.thucThi(yeuCau);

        // Then
        assertThat(ketQua.getTrongLuongTinhCuoc()).isEqualTo(30.0);
        assertThat(ketQua.getCuocPhiCoBan()).isEqualTo(310000.0);
        assertThat(ketQua.getPhuPhiHutChanKhong()).isEqualTo(15000.0);
        assertThat(ketQua.getPhuPhiGoiQua()).isEqualTo(20000.0);
        assertThat(ketQua.getPhuPhiThiep()).isEqualTo(10000.0);
        assertThat(ketQua.getTongPhuPhi()).isEqualTo(45000.0);
        assertThat(ketQua.getTongTienCuoc()).isEqualTo(355000.0);

        assertThat(luuTruAdapter.layKetQuaMoiNhat()).contains(ketQua);
    }

    @Test
    @DisplayName("Gắn cờ cảnh báo khi kiện hàng vượt kích thước trần vận chuyển")
    void canhBaoKienHangVuotTranVanChuyen() {
        // Chiều dài 210 cm (> 200 cm)
        YeuCauTinhCuocDTO yeuCauVuotChieuDai = new YeuCauTinhCuocDTO(210, 50, 40, 5.0);
        KetQuaTinhCuocDTO ketQuaDai = useCase.thucThi(yeuCauVuotChieuDai);
        assertThat(ketQuaDai.isVuotTranVanChuyen()).isTrue();
        assertThat(ketQuaDai.getCanhBao()).isEqualTo(TinhCuocVanChuyenUseCase.CANH_BAO_VUOT_TRAN);

        // Khối lượng thể tích quy đổi 360 kg (> 100 kg)
        YeuCauTinhCuocDTO yeuCauVuotTheTich = new YeuCauTinhCuocDTO(150, 120, 100, 5.0);
        KetQuaTinhCuocDTO ketQuaTheTich = useCase.thucThi(yeuCauVuotTheTich);
        assertThat(ketQuaTheTich.isVuotTranVanChuyen()).isTrue();
        assertThat(ketQuaTheTich.getCanhBao()).isEqualTo(TinhCuocVanChuyenUseCase.CANH_BAO_VUOT_TRAN);
    }
}
