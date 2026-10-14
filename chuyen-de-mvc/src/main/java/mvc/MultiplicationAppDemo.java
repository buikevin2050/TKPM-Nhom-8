package mvc;

/**
 * Lớp chạy chương trình chính (Bootstrap / Main Entry)
 * Minh họa toàn bộ luồng hoạt động chuẩn của mẫu thiết kế MVC kết hợp Observer.
 */
public class MultiplicationAppDemo {

    public static void main(String[] args) {
        System.out.println("===================================================================");
        System.out.println("   DEMO MẪU THIẾT KẾ MODEL - VIEW - CONTROLLER (MVC)");
        System.out.println("   BÀI TOÁN TÍNH TÍCH HAI SỐ: 4.0 * 5.0 = 20.0");
        System.out.println("===================================================================\n");

        // Bước 1: Khởi tạo Model và View độc lập
        System.out.println("[Bước 1] Khởi tạo Model và View độc lập...");
        MultiplicationModel model = new MultiplicationModel();
        MultiplicationView view = new MultiplicationView();

        // Bước 2: View đăng ký lắng nghe Model (Observer Pattern)
        System.out.println("[Bước 2] View đăng ký lắng nghe sự kiện thay đổi từ Model (Observer Pattern)...");
        model.addListener(view);

        // Bước 3: Tạo Controller truyền Model và View vào constructor
        System.out.println("[Bước 3] Khởi tạo Controller truyền Model và View...");
        MultiplicationController controller = new MultiplicationController(model, view);

        // Bước 4: Giả lập người dùng nhập 4.0 và 5.0 rồi bấm tính (gọi controller.xuLyTinhTich)
        double soA = 4.0;
        double soB = 5.0;
        System.out.println("[Bước 4] Người dùng nhập: a = " + soA + ", b = " + soB + " và nhấn 'Tính Tích'");
        System.out.println("        -> Controller tiếp nhận, validate và chuyển lệnh sang Model...");
        controller.xuLyTinhTich(soA, soB);

        // Bước 5: Kiểm tra kết quả tự động xuất hiện trên màn hình console qua Observer
        System.out.println("\n[Bước 5] Kết quả hiển thị tại View: " + view.getGiaTriHienThi());
        System.out.println("        -> Luồng khép kín: User -> Controller -> Model -> (Notify) -> View.");
        System.out.println("===================================================================");
    }
}
