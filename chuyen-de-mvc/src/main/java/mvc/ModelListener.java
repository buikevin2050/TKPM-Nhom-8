package mvc;

/**
 * Interface đóng vai trò Observer trong mẫu thiết kế Observer kết hợp MVC.
 * Được View cài đặt để nhận thông báo tự động từ Model khi dữ liệu thay đổi.
 */
public interface ModelListener {
    void onDataChanged(double ketQuaMoi);
}
