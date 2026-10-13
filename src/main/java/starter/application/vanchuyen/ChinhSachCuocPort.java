package starter.application.vanchuyen;

/**
 * Output Port định nghĩa giao tiếp tính cước vận chuyển cơ bản.
 * Được sở hữu bởi tầng Application (Dependency Inversion Principle).
 */
public interface ChinhSachCuocPort {

    /**
     * Tính cước vận chuyển cơ bản dựa trên trọng lượng tính cước.
     * Quy tắc: 20.000 VNĐ cho 1,0 kg đầu tiên; 2.500 VNĐ cho mỗi 0,5 kg tiếp theo (làm tròn lên).
     *
     * @param trongLuongTinhCuoc trọng lượng tính cước (kg)
     * @return cước phí cơ bản (VNĐ)
     */
    double tinhCuocCoBan(double trongLuongTinhCuoc);
}
