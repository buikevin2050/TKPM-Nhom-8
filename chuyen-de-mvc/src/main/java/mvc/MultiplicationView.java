package mvc;

/**
 * Thành phần «view» trong kiến trúc MVC.
 * Cài đặt ModelListener để tự động nhận thông báo cập nhật từ Model
 * và hiển thị kết quả ra màn hình mà không cần Controller can thiệp vào quá trình hiển thị.
 */
public class MultiplicationView implements ModelListener {
    private double giaTriHienThi;

    /**
     * Callback được Model gọi khi dữ liệu kết quả có sự thay đổi.
     *
     * @param ketQuaMoi giá trị tích mới tính được từ Model
     */
    @Override
    public void onDataChanged(double ketQuaMoi) {
        this.giaTriHienThi = ketQuaMoi;
        veLaiManHinh();
    }

    /**
     * Giả lập hành vi vẽ lại giao diện / in kết quả mới ra console.
     */
    public void veLaiManHinh() {
        System.out.println("[VIEW - TỰ ĐỘNG LÀM MỚI] Kết quả tích là: " + this.giaTriHienThi);
    }

    public double getGiaTriHienThi() {
        return giaTriHienThi;
    }
}
