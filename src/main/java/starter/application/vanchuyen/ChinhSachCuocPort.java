package starter.application.vanchuyen;

/**
 * Output Port định nghĩa giao tiếp tính cước vận chuyển và phụ phí dịch vụ gia tăng.
 * Tuân thủ Open-Closed Principle (OCP) - cho phép mở rộng các loại dịch vụ gia tăng qua Port.
 */
public interface ChinhSachCuocPort {

    /**
     * Tính cước vận chuyển cơ bản dựa trên trọng lượng tính cước.
     *
     * @param trongLuongTinhCuoc trọng lượng tính cước (kg)
     * @return cước phí cơ bản (VNĐ)
     */
    double tinhCuocCoBan(double trongLuongTinhCuoc);

    /**
     * Tra cứu phụ phí của một dịch vụ gia tăng cụ thể.
     *
     * @param tenDichVu tên mã dịch vụ gia tăng (ví dụ: "HutChanKhong", "GoiQuaSinhNhat", "VietThiepMung")
     * @return phụ phí tương ứng (VNĐ), hoặc 0.0 nếu dịch vụ không tính phí / không tồn tại
     */
    double layPhuPhiDichVu(String tenDichVu);
}
