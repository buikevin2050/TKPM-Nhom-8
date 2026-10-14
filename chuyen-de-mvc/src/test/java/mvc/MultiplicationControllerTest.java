package mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Kiểm thử thành phần MultiplicationController")
class MultiplicationControllerTest {

    private MultiplicationModel model;
    private MultiplicationView view;
    private MultiplicationController controller;

    @BeforeEach
    void setUp() {
        model = new MultiplicationModel();
        view = new MultiplicationView();
        model.addListener(view);
        controller = new MultiplicationController(model, view);
    }

    @Test
    @DisplayName("Kiểm tra điều phối thành công: truyền 6.0 và 7.0, model và view nhận giá trị 42.0")
    void testXuLyTinhTich_ThanhCong() {
        controller.xuLyTinhTich(6.0, 7.0);

        assertThat(model.getKetQua())
                .as("Model phải cập nhật kết quả 42.0")
                .isEqualTo(42.0);
        assertThat(model.getSoA()).isEqualTo(6.0);
        assertThat(model.getSoB()).isEqualTo(7.0);

        assertThat(view.getGiaTriHienThi())
                .as("View phải tự động đồng bộ kết quả 42.0 thông qua Observer")
                .isEqualTo(42.0);
    }

    @Test
    @DisplayName("Kiểm tra validation lỗi: truyền Double.NaN ném IllegalArgumentException và không đổi trạng thái Model")
    void testXuLyTinhTich_ValidationLoi_DoubleNaN() {
        // Trạng thái ban đầu của model
        double initialKetQua = model.getKetQua();
        double initialSoA = model.getSoA();
        double initialSoB = model.getSoB();

        // Kiểm tra với tham số a = NaN
        assertThatThrownBy(() -> controller.xuLyTinhTich(Double.NaN, 5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dữ liệu nhập vào phải là số hợp lệ!");

        // Kiểm tra với tham số b = NaN
        assertThatThrownBy(() -> controller.xuLyTinhTich(4.0, Double.NaN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dữ liệu nhập vào phải là số hợp lệ!");

        // Xác nhận trạng thái của Model không bị thay đổi
        assertThat(model.getKetQua()).isEqualTo(initialKetQua);
        assertThat(model.getSoA()).isEqualTo(initialSoA);
        assertThat(model.getSoB()).isEqualTo(initialSoB);
    }
}
