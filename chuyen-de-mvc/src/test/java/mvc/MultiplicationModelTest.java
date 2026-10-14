package mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Kiểm thử thành phần MultiplicationModel")
class MultiplicationModelTest {

    private MultiplicationModel model;

    @BeforeEach
    void setUp() {
        model = new MultiplicationModel();
    }

    @Test
    @DisplayName("Kiểm tra phép nhân hai số dương: 4 * 5 = 20.0")
    void testTinhTich_SoDuong() {
        model.tinhTich(4.0, 5.0);

        assertThat(model.getSoA()).isEqualTo(4.0);
        assertThat(model.getSoB()).isEqualTo(5.0);
        assertThat(model.getKetQua()).isEqualTo(20.0);
    }

    @Test
    @DisplayName("Kiểm tra phép nhân với 0: 10 * 0 = 0.0")
    void testTinhTich_VoiSoKhong() {
        model.tinhTich(10.0, 0.0);

        assertThat(model.getSoA()).isEqualTo(10.0);
        assertThat(model.getSoB()).isEqualTo(0.0);
        assertThat(model.getKetQua()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Kiểm tra phép nhân số âm: -3 * 5 = -15.0")
    void testTinhTich_SoAm() {
        model.tinhTich(-3.0, 5.0);

        assertThat(model.getSoA()).isEqualTo(-3.0);
        assertThat(model.getSoB()).isEqualTo(5.0);
        assertThat(model.getKetQua()).isEqualTo(-15.0);
    }

    @Test
    @DisplayName("Kiểm tra cơ chế Observer: listener nhận đúng kết quả mới khi tinhTich() được gọi")
    void testObserverMechanism() {
        AtomicBoolean wasNotified = new AtomicBoolean(false);
        AtomicReference<Double> receivedValue = new AtomicReference<>();

        model.addListener(ketQuaMoi -> {
            wasNotified.set(true);
            receivedValue.set(ketQuaMoi);
        });

        model.tinhTich(4.0, 5.0);

        assertThat(wasNotified.get())
                .as("Listener phải được kích hoạt sau khi Model tính toán")
                .isTrue();
        assertThat(receivedValue.get())
                .as("Listener phải nhận được đúng giá trị kết quả từ Model")
                .isEqualTo(20.0);
    }
}
