package starter.domain.vanchuyen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

@DisplayName("Kiểm thử Tầng Domain: Tính khối lượng thể tích và trọng lượng tính cước")
class KhiTinhKhoiLuongTheTichTest {

    private final TinhKhoiLuongTheTich boTinhKhoiLuongTheTich = new TinhKhoiLuongTheTich();
    private final XacDinhTrongLuongTinhCuoc boXacDinhTrongLuong = new XacDinhTrongLuongTinhCuoc(boTinhKhoiLuongTheTich);

    @Nested
    @DisplayName("Kiểm tra tính hợp lệ của Kích Thước")
    class KiemTraKichThuocTests {

        @Test
        @DisplayName("Ném ngoại lệ IllegalArgumentException khi chiều dài nhỏ hơn hoặc bằng 0")
        void nemNgoaiLeKhiChieuDaiKhongHopLe() {
            assertThatThrownBy(() -> new KichThuoc(0, 20, 20))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Kích thước phải lớn hơn 0");

            assertThatThrownBy(() -> new KichThuoc(-5, 20, 20))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Kích thước phải lớn hơn 0");
        }

        @Test
        @DisplayName("Ném ngoại lệ IllegalArgumentException khi chiều rộng nhỏ hơn hoặc bằng 0")
        void nemNgoaiLeKhiChieuRongKhongHopLe() {
            assertThatThrownBy(() -> new KichThuoc(20, 0, 20))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Kích thước phải lớn hơn 0");

            assertThatThrownBy(() -> new KichThuoc(20, -10, 20))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Kích thước phải lớn hơn 0");
        }

        @Test
        @DisplayName("Ném ngoại lệ IllegalArgumentException khi chiều cao nhỏ hơn hoặc bằng 0")
        void nemNgoaiLeKhiChieuCaoKhongHopLe() {
            assertThatThrownBy(() -> new KichThuoc(20, 20, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Kích thước phải lớn hơn 0");

            assertThatThrownBy(() -> new KichThuoc(20, 20, -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Kích thước phải lớn hơn 0");
        }
    }

    @Nested
    @DisplayName("Kiểm tra tính khối lượng thể tích quy đổi")
    class TinhKhoiLuongTheTichTests {

        @ParameterizedTest(name = "Kích thước {0}x{1}x{2} cm -> Khối lượng thể tích: {3} kg")
        @CsvSource({
                "20, 20, 20, 1.6",
                "100, 60, 40, 48.0",
                "120, 70, 50, 84.0",
                "150, 80, 60, 144.0"
        })
        void tinhKhoiLuongTheTichChinhXac(double chieuDai, double chieuRong, double chieuCao, double expectedKhoiLuong) {
            KichThuoc kichThuoc = new KichThuoc(chieuDai, chieuRong, chieuCao);

            double ketQua = boTinhKhoiLuongTheTich.tinh(kichThuoc);

            assertThat(ketQua).isCloseTo(expectedKhoiLuong, offset(0.001));
        }

        @ParameterizedTest(name = "Kích thước {0}x{1}x{2} cm sau hút chân không -> {3} kg")
        @CsvSource({
                "100, 60, 40, 24.0",
                "120, 70, 50, 42.0",
                "150, 80, 60, 72.0"
        })
        void tinhKhoiLuongTheTichSauHutChanKhong(double chieuDai, double chieuRong, double chieuCao, double expectedSauHut) {
            KichThuoc kichThuoc = new KichThuoc(chieuDai, chieuRong, chieuCao);

            double ketQua = boTinhKhoiLuongTheTich.tinhSauHutChanKhong(kichThuoc);

            assertThat(ketQua).isCloseTo(expectedSauHut, offset(0.001));
        }
    }

    @Nested
    @DisplayName("Kiểm tra quy tắc xác định trọng lượng tính cước")
    class XacDinhTrongLuongTinhCuocTests {

        @Test
        @DisplayName("Hàng nhỏ gọn nặng cân: Cân nặng thực tế lớn hơn khối lượng thể tích")
        void canNangThucLonHonTheTich() {
            KichThuoc kichThuoc = new KichThuoc(20, 20, 20); // Thể tích = 1.6 kg
            double canNangThuc = 4.0;

            double trongLuongTinhCuoc = boXacDinhTrongLuong.tinh(kichThuoc, canNangThuc, false);

            assertThat(trongLuongTinhCuoc).isEqualTo(4.0);
        }

        @Test
        @DisplayName("Thú bông cồng kềnh siêu nhẹ: Khối lượng thể tích lớn hơn cân nặng thực tế")
        void theTichLonHonCanNangThuc() {
            KichThuoc kichThuoc = new KichThuoc(100, 60, 40); // Thể tích = 48.0 kg
            double canNangThuc = 0.5;

            double trongLuongTinhCuoc = boXacDinhTrongLuong.tinh(kichThuoc, canNangThuc, false);

            assertThat(trongLuongTinhCuoc).isEqualTo(48.0);
        }

        @Test
        @DisplayName("Hút chân không giảm thể tích: Trọng lượng tính cước cập nhật theo thể tích sau hút")
        void apDungHutChanKhongGiamTrongLuong() {
            KichThuoc kichThuoc = new KichThuoc(100, 60, 40); // Thể tích ban đầu = 48 kg, sau hút = 24 kg
            double canNangThuc = 2.0;

            double trongLuongTinhCuoc = boXacDinhTrongLuong.tinh(kichThuoc, canNangThuc, true);

            assertThat(trongLuongTinhCuoc).isEqualTo(24.0);
        }

        @Test
        @DisplayName("Cân nặng thực tế lớn hơn khối lượng thể tích sau hút chân không")
        void canNangThucLonHonTheTichSauHut() {
            KichThuoc kichThuoc = new KichThuoc(100, 60, 40); // Sau hút = 24 kg
            double canNangThuc = 30.0;

            double trongLuongTinhCuoc = boXacDinhTrongLuong.tinh(kichThuoc, canNangThuc, true);

            assertThat(trongLuongTinhCuoc).isEqualTo(30.0);
        }

        @Test
        @DisplayName("Ném ngoại lệ khi cân nặng thực tế âm")
        void nemNgoaiLeKhiCanNangThucAm() {
            KichThuoc kichThuoc = new KichThuoc(20, 20, 20);

            assertThatThrownBy(() -> boXacDinhTrongLuong.tinh(kichThuoc, -1.0, false))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Cân nặng thực tế không được âm");
        }
    }
}
