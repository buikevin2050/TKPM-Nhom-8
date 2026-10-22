Feature: Phân hệ 4: Tính cước vận chuyển cồng kềnh và dịch vụ gia tăng cho búp bê và thú bông
  In order to giải quyết điểm đau phí vận chuyển thú bông cồng kềnh quá cao và giảm tỷ lệ khách hàng hủy đơn hàng
  As a Nhân viên quản lý bán hàng và vận hành đơn hàng thú bông
  I want to hệ thống tự động xác định trọng lượng tính cước theo chuẩn bưu chính, tối ưu qua hút chân không và tính cước bậc thang cùng phụ phí quà tặng

  Background:
    Given biểu phí vận chuyển tiêu chuẩn áp dụng nấc cơ bản 20.000 VNĐ cho 1.0 kg đầu tiên
    And nấc lũy tiến là 5.000 VNĐ cho mỗi 0.5 kg tiếp theo sau 1.0 kg đầu
    And công thức quy đổi thể tích bưu chính quốc tế bằng chiều dài nhân chiều rộng nhân chiều cao chia cho 5000
    And dịch vụ hút chân không giảm 50% thể tích đóng gói với phụ phí cố định 15.000 VNĐ
    And phụ phí gói quà sinh nhật là 20.000 VNĐ và viết thiệp chúc mừng là 10.000 VNĐ

  Rule: Rule 1: Trọng lượng tính cước theo chuẩn bưu chính quốc tế
    Example: Kiện hàng nhỏ gọn tính theo cân nặng thực tế khi thể tích quy đổi nhỏ hơn
      Given kiện hàng có cân nặng thực tế 1.5 kg và kích thước 20.0 cm x 15.0 cm x 10.0 cm
      When hệ thống tính cước vận chuyển không áp dụng hút chân không
      Then khối lượng thể tích là 0.6 kg
      And trọng lượng tính cước là 1.5 kg

    Example: Thú bông khổng lồ cồng kềnh tính theo khối lượng thể tích khi thể tích lớn hơn
      Given kiện hàng có cân nặng thực tế 2.0 kg và kích thước 100.0 cm x 60.0 cm x 50.0 cm
      When hệ thống tính cước vận chuyển không áp dụng hút chân không
      Then khối lượng thể tích là 60.0 kg
      And trọng lượng tính cước là 60.0 kg

  Rule: Rule 2: Tối ưu chi phí qua Dịch vụ hút chân không (NV12)
    Example: Kiện thú bông cồng kềnh giảm một nửa thể tích khi chọn hút chân không
      Given kiện hàng có cân nặng thực tế 2.0 kg và kích thước 100.0 cm x 60.0 cm x 50.0 cm
      When hệ thống tính cước có áp dụng dịch vụ hút chân không
      Then khối lượng thể tích ban đầu là 60.0 kg
      And khối lượng thể tích sau hút chân không là 30.0 kg
      And trọng lượng tính cước sau hút chân không là 30.0 kg
      And phụ phí hút chân không là 15.000 VNĐ

  Rule: Rule 3: Kết hợp đa dịch vụ gia tăng quà tặng
    Example: Khách hàng chọn kết hợp hút chân không, gói quà sinh nhật và viết thiệp chúc mừng
      Given kiện hàng có cân nặng thực tế 2.0 kg và kích thước 100.0 cm x 60.0 cm x 50.0 cm
      And khách hàng chọn các dịch vụ gia tăng "HUT_CHAN_KHONG, GOI_QUA, VIET_THIEP"
      When hệ thống tính cước và lưu kết quả vào hệ thống
      Then phụ phí hút chân không là 15.000 VNĐ
      And phụ phí gói quà là 20.000 VNĐ
      And phụ phí thiệp chúc mừng là 10.000 VNĐ
      And tổng phụ phí là 45.000 VNĐ
      And hệ thống tạo mã phiếu tính cước hợp lệ và lưu trữ kết quả thành công

  Rule: Rule 4: Biểu phí cước vận chuyển bậc thang
    Scenario Outline: Tính toán cước cơ bản bậc thang và tổng cước thanh toán cho nhiều kiện hàng
      Given kiện hàng có cân nặng thực tế <can_nang_thuc> kg và kích thước <dai> cm x <rong> cm x <cao> cm
      And dịch vụ gia tăng được chọn là "<dich_vu>"
      When hệ thống thực hiện tính toán cước phí vận chuyển
      Then trọng lượng tính cước là <trong_luong_tinh_cuoc> kg
      And cước cơ bản là <cuoc_co_ban> VNĐ
      And tổng phụ phí là <tong_phu_phi> VNĐ
      And tổng cước thanh toán là <tong_cuoc> VNĐ

      Examples:
        | can_nang_thuc | dai   | rong  | cao  | dich_vu                             | trong_luong_tinh_cuoc | cuoc_co_ban | tong_phu_phi | tong_cuoc |
        | 0.8           | 15.0  | 10.0  | 10.0 | KHONG                               | 0.8                   | 20.000      | 0            | 20.000    |
        | 1.5           | 20.0  | 15.0  | 10.0 | KHONG                               | 1.5                   | 25.000      | 0            | 25.000    |
        | 2.0           | 30.0  | 20.0  | 15.0 | GOI_QUA                             | 2.0                   | 30.000      | 20.000       | 50.000    |
        | 2.0           | 100.0 | 60.0  | 50.0 | HUT_CHAN_KHONG                      | 30.0                  | 310.000     | 15.000       | 325.000   |
        | 2.0           | 100.0 | 60.0  | 50.0 | HUT_CHAN_KHONG, GOI_QUA             | 30.0                  | 310.000     | 35.000       | 345.000   |
        | 2.0           | 100.0 | 60.0  | 50.0 | HUT_CHAN_KHONG, GOI_QUA, VIET_THIEP | 30.0                  | 310.000     | 45.000       | 355.000   |
        | 2.0           | 100.0 | 60.0  | 50.0 | KHONG                               | 60.0                  | 610.000     | 0            | 610.000   |

  Rule: Rule 5: Kiểm tra tính hợp lệ dữ liệu đầu vào và các trường hợp biên
    Scenario Outline: Từ chối tính cước khi kích thước kiện hàng không hợp lệ
      Given kiện hàng có thông tin kích thước <dai> cm x <rong> cm x <cao> cm và cân nặng 2.0 kg
      When hệ thống kiểm tra và tính cước kiện hàng
      Then hệ thống từ chối tính cước với thông báo lỗi "<thong_bao_loi>"

      Examples:
        | dai   | rong  | cao  | thong_bao_loi                      |
        | 0.0   | 50.0  | 40.0 | Kích thước kiện hàng phải lớn hơn 0 |
        | -10.0 | 50.0  | 40.0 | Kích thước kiện hàng phải lớn hơn 0 |
        | 50.0  | 0.0   | 40.0 | Kích thước kiện hàng phải lớn hơn 0 |
        | 50.0  | -5.0  | 40.0 | Kích thước kiện hàng phải lớn hơn 0 |
        | 50.0  | 40.0  | 0.0  | Kích thước kiện hàng phải lớn hơn 0 |
        | 50.0  | 40.0  | -2.0 | Kích thước kiện hàng phải lớn hơn 0 |

    Scenario Outline: Từ chối tính cước khi cân nặng thực tế không hợp lệ
      Given kiện hàng có thông tin kích thước 50.0 cm x 40.0 cm x 30.0 cm và cân nặng <can_nang> kg
      When hệ thống kiểm tra và tính cước kiện hàng
      Then hệ thống từ chối tính cước với thông báo lỗi "Cân nặng thực tế phải lớn hơn 0"

      Examples:
        | can_nang |
        | 0.0      |
        | -1.5     |

    Scenario Outline: Gắn cờ cảnh báo khi kiện hàng vượt trần kích thước vận chuyển tiêu chuẩn
      Given kiện hàng có kích thước <dai> cm x <rong> cm x <cao> cm và cân nặng <can_nang> kg
      When hệ thống thực hiện tính toán cước phí vận chuyển
      Then hệ thống hiển thị cảnh báo "Vượt quá kích thước tối đa của vận chuyển tiêu chuẩn, chuyển sang đường vận chuyển đặc biệt"

      Examples:
        | dai   | rong  | cao   | can_nang | ghi_chu                                                |
        | 210.0 | 50.0  | 40.0  | 5.0      | Chiều dài 210 cm vượt quá trần 200 cm                 |
        | 150.0 | 120.0 | 100.0 | 5.0      | Khối lượng quy đổi 360 kg vượt quá trần 100 kg         |
