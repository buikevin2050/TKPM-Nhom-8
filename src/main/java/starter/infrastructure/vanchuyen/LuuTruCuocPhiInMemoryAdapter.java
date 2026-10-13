package starter.infrastructure.vanchuyen;

import starter.application.vanchuyen.KetQuaTinhCuocDTO;
import starter.application.vanchuyen.LuuTruCuocPhiPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * In-Memory Adapter triển khai LuuTruCuocPhiPort.
 * Giúp cô lập môi trường kiểm thử (không phụ thuộc CSDL thực tế) và tăng tốc độ test.
 */
public class LuuTruCuocPhiInMemoryAdapter implements LuuTruCuocPhiPort {

    private final List<KetQuaTinhCuocDTO> lichSuTinhCuoc = Collections.synchronizedList(new ArrayList<>());
    private final AtomicReference<KetQuaTinhCuocDTO> ketQuaMoiNhat = new AtomicReference<>();

    @Override
    public boolean luuKetQua(KetQuaTinhCuocDTO ketQua) {
        Objects.requireNonNull(ketQua, "Kết quả cần lưu không được null");
        ketQuaMoiNhat.set(ketQua);
        return lichSuTinhCuoc.add(ketQua);
    }

    @Override
    public Optional<KetQuaTinhCuocDTO> layKetQuaMoiNhat() {
        return Optional.ofNullable(ketQuaMoiNhat.get());
    }

    public List<KetQuaTinhCuocDTO> layTatCaKetQua() {
        return List.copyOf(lichSuTinhCuoc);
    }

    public void xoaDuLieu() {
        lichSuTinhCuoc.clear();
        ketQuaMoiNhat.set(null);
    }
}
