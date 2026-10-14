package mvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Thành phần «model» trong kiến trúc MVC.
 * Lưu trữ trạng thái dữ liệu (số a, số b, kết quả) và thực hiện nghiệp vụ tính tích.
 * 100% độc lập, không phụ thuộc và không import bất kỳ thư viện GUI nào.
 */
public class MultiplicationModel {
    private double soA;
    private double soB;
    private double ketQua;
    private final List<ModelListener> listeners = new ArrayList<>();

    /**
     * Đăng ký Observer (Listener) để theo dõi sự thay đổi trạng thái của Model.
     *
     * @param listener đối tượng cài đặt ModelListener
     */
    public void addListener(ModelListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Thực hiện logic tính tích hai số: ketQua = a * b.
     * Sau khi tính toán xong, tự động thông báo tới toàn bộ Observers đã đăng ký.
     *
     * @param a số thứ nhất
     * @param b số thứ hai
     */
    public void tinhTich(double a, double b) {
        this.soA = a;
        this.soB = b;
        this.ketQua = a * b;
        notifyObservers();
    }

    /**
     * Thông báo cho toàn bộ các listener về kết quả mới.
     */
    private void notifyObservers() {
        for (ModelListener listener : listeners) {
            listener.onDataChanged(this.ketQua);
        }
    }

    public double getKetQua() {
        return ketQua;
    }

    public double getSoA() {
        return soA;
    }

    public double getSoB() {
        return soB;
    }
}
