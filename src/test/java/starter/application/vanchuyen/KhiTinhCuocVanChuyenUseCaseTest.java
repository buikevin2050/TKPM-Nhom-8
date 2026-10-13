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
    @DisplayName("Ném ngoại lệ khi kích thước trong yêu cầu không hợp lệ")
    void nemNgoaiLeKhiKichThuocKhongHopLe() {
        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(-10, 20, 20, 2.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Chiều dài phải lớn hơn 0");

        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(20, 0, 20, 2.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Chiều rộng phải lớn hơn 0");

        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(20, 20, -5, 2.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Chiều cao phải lớn hơn 0");
    }

    @Test
    @DisplayName("Ném ngoại lệ khi cân nặng thực tế trong yêu cầu âm")
    void nemNgoaiLeKhiCanNangThucAm() {
        assertThatThrownBy(() -> new YeuCauTinhCuocDTO(20, 20, 20, -1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cân nặng thực tế không được âm");
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
        assertThat(ketQua.getCuocPhiCoBan()).isEqualTo(35000.0);
        assertThat(ketQua.getTongPhuPhi()).isEqualTo(0.0);
        assertThat(ketQua.getTongTienCuoc()).isEqualTo(35000.0);

        // Kiểm tra lưu kết quả qua Port
        Optional<KetQuaTinhCuocDTO> daLuu = luuTruAdapter.layKetQuaMoiNhat();
        assertThat(daLuu).isPresent();
        assertThat(daLuu.get()).isEqualTo(ketQua);
    }

    @Test
    @DisplayName("Tính cước có hút chân không và xác định số tiền tiết kiệm")
    void tinhCuocCoHutChanKhongVaTietKiem() {
        // Given: 2.0 kg thực tế, 100x60x40 cm (ban đầu 48 kg, sau hút 24 kg)
        YeuCauTinhCuocDTO yeuCau = new YeuCauTinhCuocDTO(100, 60, 40, 2.0, true, List.of("HUT_CHAN_KHONG"));

        // When
        KetQuaTinhCuocDTO ketQua = useCase.thucThi(yeuCau);

        // Then
        assertThat(ketQua.getKhoiLuongTheTichBanDau()).isEqualTo(48.0);
        assertThat(ketQua.getKhoiLuongTheTichSauHut()).isEqualTo(24.0);
        assertThat(ketQua.getTrongLuongTinhCuoc()).isEqualTo(24.0);
        assertThat(ketQua.getCuocChuaHut()).isEqualTo(255000.0);
        assertThat(ketQua.getCuocPhiCoBan()).isEqualTo(135000.0);
        assertThat(ketQua.getPhuPhiHutChanKhong()).isEqualTo(15000.0);
        assertThat(ketQua.getTongTienCuoc()).isEqualTo(150000.0);
        assertThat(ketQua.getTienTietKiem()).isEqualTo(105000.0);
    }

    @Test
    @DisplayName("Tính cước trọn bộ gói sinh nhật đầy đủ phụ phí")
    void tinhCuocTronBoGoiSinhNhat() {
        List<String> dichVu = List.of("HUT_CHAN_KHONG", "GOI_QUA", "THIEP");
        YeuCauTinhCuocDTO yeuCau = new YeuCauTinhCuocDTO(100, 60, 40, 2.0, true, dichVu);

        // When
        KetQuaTinhCuocDTO ketQua = useCase.thucThi(yeuCau);

        // Then
        assertThat(ketQua.getTrongLuongTinhCuoc()).isEqualTo(24.0);
        assertThat(ketQua.getCuocPhiCoBan()).isEqualTo(135000.0);
        assertThat(ketQua.getPhuPhiHutChanKhong()).isEqualTo(15000.0);
        assertThat(ketQua.getPhuPhiGoiQua()).isEqualTo(20000.0);
        assertThat(ketQua.getPhuPhiThiep()).isEqualTo(10000.0);
        assertThat(ketQua.getTongPhuPhi()).isEqualTo(45000.0);
        assertThat(ketQua.getTongTienCuoc()).isEqualTo(180000.0);

        assertThat(luuTruAdapter.layKetQuaMoiNhat()).contains(ketQua);
    }
}
