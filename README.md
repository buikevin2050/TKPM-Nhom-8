# ĐỀ TÀI 08: HỆ THỐNG QUẢN LÝ BÁN HÀNG BÚP BÊ & THÚ BÔNG
## PHÂN HỆ 4: TÍNH CƯỚC VẬN CHUYỂN CỒNG KỀNH & DỊCH VỤ GIA TĂNG (OOPSI BDD)

Dự án áp dụng phương pháp phát triển hướng hành vi **BDD (Behavior-Driven Development)** theo khung tư duy **OOPSI** của Thầy Lê Huỳnh Phước, kết hợp kiểm thử tự động hóa sống động với **Serenity BDD & Cucumber**, xây dựng trên nền tảng **Kiến trúc Lục giác (Hexagonal / Clean Architecture)**.

---

### 1. Khung Phương Pháp Luận OOPSI

Tài liệu đặc tả nghiệp vụ và kịch bản kiểm thử được tổ chức theo cấu trúc OOPSI:

- **O (Outcome - Mục tiêu & Kết quả nghiệp vụ)**:
  - **Điểm đau (Pain Point)**: Chi phí vận chuyển kiện hàng thú bông quá cao do kích thước cồng kềnh, khiến khách hàng từ bỏ giỏ hàng hoặc hủy đơn.
  - **Mục tiêu (Goal)**: Giảm tỷ lệ hủy đơn hàng thông qua dịch vụ hút chân không giảm 50% thể tích và cơ chế minh bạch biểu phí bậc thang.
  - Định dạng: `In order to ... As a ... I want to ...`
- **O (Output - Kết quả hữu hình kiểm chứng)**:
  - Khối lượng thể tích quy đổi (kg).
  - Trọng lượng tính cước (kg).
  - Cước vận chuyển cơ bản bậc thang (VNĐ).
  - Phụ phí từng dịch vụ gia tăng (Hút chân không, Gói quà sinh nhật, Viết thiệp chúc mừng).
  - Tổng cước thanh toán cuối cùng (VNĐ).
  - Cờ cảnh báo kiện hàng vượt kích thước trần vận chuyển tiêu chuẩn.
  - Mã phiếu tính cước được lưu trữ vào hệ thống qua Port.
- **P (Process - Tiến trình thực thi)**:
  - Thiết lập thông số kiện hàng (kích thước dài, rộng, cao, cân nặng thực tế) và danh sách dịch vụ chọn -> Hệ thống tiếp nhận yêu cầu -> Thực hiện quy trình nghiệp vụ -> Xuất kết quả.
- **S (Scenario / Rule - Quy tắc và Kịch bản kiểm thử)**:
  - Phân nhóm theo từ khóa `Rule:` đại diện cho các quy tắc nghiệp vụ cốt lõi:
    - **Rule 1**: Trọng lượng tính cước theo chuẩn bưu chính quốc tế.
    - **Rule 2**: Tối ưu chi phí qua Dịch vụ hút chân không (NV12).
    - **Rule 3**: Kết hợp đa dịch vụ gia tăng quà tặng.
    - **Rule 4**: Biểu phí cước vận chuyển bậc thang.
    - **Rule 5**: Kiểm tra tính hợp lệ dữ liệu đầu vào và các trường hợp biên.
- **I (Inputs - Dữ liệu đầu vào)**:
  - Bảng dữ liệu `Scenario Outline` với các bộ dữ liệu thực tế bao phủ đầy đủ Happy Path, Edge Cases và Exception.

---

### 2. Kiến Trúc Mã Nguồn (Hexagonal / Clean Architecture)

```
src/
├── main/java/starter/
│   ├── domain/vanchuyen/                     # Domain Layer (Pure Business Logic)
│   │   ├── KichThuoc.java                    # Value Object (Dài, Rộng, Cao)
│   │   ├── TinhKhoiLuongTheTich.java         # Domain Service (Quy đổi thể tích & Hút chân không)
│   │   └── XacDinhTrongLuongTinhCuoc.java    # Domain Policy (Max(Thực tế, Thể tích quy đổi))
│   ├── application/vanchuyen/                # Application Layer (Use Cases & Ports)
│   │   ├── ChinhSachCuocPort.java            # Output Port tính cước & phụ phí
│   │   ├── LuuTruCuocPhiPort.java            # Output Port lưu trữ phiếu tính cước
│   │   ├── YeuCauTinhCuocDTO.java            # Input DTO (Request Model)
│   │   ├── KetQuaTinhCuocDTO.java            # Output DTO (Response Model)
│   │   └── TinhCuocVanChuyenUseCase.java     # Use Case Interactor (Control)
│   └── infrastructure/vanchuyen/             # Infrastructure Layer (Adapters)
│       ├── ChinhSachCuocTieuChuanAdapter.java# Adapter triển khai ChinhSachCuocPort
│       └── LuuTruCuocPhiInMemoryAdapter.java # In-Memory Adapter triển khai LuuTruCuocPhiPort
└── test/
    ├── java/starter/
    │   ├── CucumberTestSuite.java            # JUnit Runner tích hợp Cucumber & Serenity
    │   ├── domain/vanchuyen/                 # Domain Unit Tests (AssertJ)
    │   ├── application/vanchuyen/            # Use Case Unit Tests (AssertJ)
    │   └── stepdefinitions/                  # Step Definitions ánh xạ Gherkin
    └── resources/
        └── features/shipping/
            └── tinh_cuoc_van_chuyen_cong_kenh.feature # Đặc tả BDD Gherkin chuẩn OOPSI
```

---

### 3. Hướng Dẫn Chạy Kiểm Thử & Xuất Báo Cáo Living Documentation

#### Chạy toàn bộ kiểm thử và sinh báo cáo:
```bash
mvn clean verify
```

#### Xem báo cáo Serenity Living Documentation:
Mở file sau trên trình duyệt:
- Báo cáo chi tiết tương tác: `target/site/serenity/index.html`
- Báo cáo tổng hợp một trang: `target/site/serenity/serenity-summary.html`
