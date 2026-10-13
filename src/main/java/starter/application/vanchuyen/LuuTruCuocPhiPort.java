package starter.application.vanchuyen;

import java.util.Optional;

/**
 * Output Port định nghĩa giao tiếp lưu trữ kết quả tính cước.
 * Được sở hữu bởi tầng Application.
 */
public interface LuuTruCuocPhiPort {

    /**
     * Lưu kết quả tính cước vào kho lưu trữ (adapter).
     *
     * @param ketQua kết quả tính cước cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    boolean luuKetQua(KetQuaTinhCuocDTO ketQua);

    /**
     * Lấy kết quả tính cước mới nhất được lưu trong hệ thống.
     *
     * @return Optional chứa kết quả tính cước mới nhất
     */
    Optional<KetQuaTinhCuocDTO> layKetQuaMoiNhat();
}
