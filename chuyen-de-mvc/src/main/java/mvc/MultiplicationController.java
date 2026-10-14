package mvc;

/**
 * Thành phần «control» trong kiến trúc MVC.
 * Tiếp nhận yêu cầu từ người dùng (hoặc giao diện), kiểm tra tính hợp lệ của dữ liệu,
 * và điều phối cập nhật trạng thái trong Model.
 * Controller hoàn toàn không trực tiếp in hay vẽ kết quả ra màn hình.
 */
public class MultiplicationController {
    private final MultiplicationModel model;
    private final MultiplicationView view;

    public MultiplicationController(MultiplicationModel model, MultiplicationView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Xử lý yêu cầu tính tích hai số.
     *
     * @param a số thứ nhất
     * @param b số thứ hai
     * @throws IllegalArgumentException nếu một trong hai số là NaN
     */
    public void xuLyTinhTich(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) {
            throw new IllegalArgumentException("Dữ liệu nhập vào phải là số hợp lệ!");
        }
        model.tinhTich(a, b);
    }

    public MultiplicationModel getModel() {
        return model;
    }

    public MultiplicationView getView() {
        return view;
    }
}
