Feature: Phân hệ 4: Tính cước vận chuyển cồng kềnh và dịch vụ gia tăng thú bông
  Để khách hàng biết chính xác cước vận chuyển và phụ phí dịch vụ
  Là hệ thống tính cước cho kiện hàng thú bông
  Tôi muốn tính trọng lượng tính cước và tổng chi phí theo dữ liệu yêu cầu

  Background:
    Given chính sách cước cơ sở là 20.000 VND cho 1,0 kg đầu tiên và 5.000 VND cho mỗi 1,0 kg tiếp theo
    And phần trọng lượng sau 1,0 kg được làm tròn lên theo bước 0,5 kg, tương đương 2.500 VND cho mỗi bước 0,5 kg
    And khối lượng thể tích bằng chiều dài nhân chiều rộng nhân chiều cao tính bằng cm rồi chia cho 5000
    And trọng lượng tính cước bằng giá trị lớn hơn giữa cân nặng thực tế và khối lượng thể tích
    And dịch vụ hút chân không giảm 50% thể tích đóng gói và có phụ phí cố định 15.000 VND
    And phí gói quà là 20.000 VND và phí thiệp chúc mừng là 10.000 VND

  @NV11
  Scenario Outline: So sánh cân nặng thực tế và khối lượng thể tích để xác định trọng lượng tính cước
    Given kiện hàng có cân nặng thực tế <can_nang_thuc_te> kg và kích thước <chieu_dai> cm x <chieu_rong> cm x <chieu_cao> cm
    When hệ thống tính cước vận chuyển không áp dụng hút chân không
    Then khối lượng thể tích là <khoi_luong_the_tich> kg
    And trọng lượng tính cước là <trong_luong_tinh_cuoc> kg
    And cước cơ sở là <cuoc_co_so> VND

    Examples:
      | can_nang_thuc_te | chieu_dai | chieu_rong | chieu_cao | khoi_luong_the_tich | trong_luong_tinh_cuoc | cuoc_co_so |
      | 4,0              | 20        | 20         | 20        | 1,6                 | 4,0                   | 35.000     |
      | 0,5              | 100       | 60         | 40        | 48,0                | 48,0                  | 255.000    |
      | 8,0              | 20        | 20         | 20        | 1,6                 | 8,0                   | 55.000     |

  @NV11 @NV12
  Scenario Outline: Tối ưu cước vận chuyển bằng dịch vụ hút chân không
    Given kiện thú bông có cân nặng thực tế <can_nang_thuc_te> kg và kích thước <chieu_dai> cm x <chieu_rong> cm x <chieu_cao> cm
    When hệ thống tính cước có áp dụng dịch vụ hút chân không
    Then khối lượng thể tích ban đầu là <khoi_luong_the_tich_ban_dau> kg
    And khối lượng thể tích sau hút chân không là <khoi_luong_the_tich_sau_hut> kg
    And trọng lượng tính cước sau hút chân không là <trong_luong_tinh_cuoc> kg
    And cước vận chuyển không hút chân không là <cuoc_truoc_hut> VND
    And tổng cước sau khi cộng phí hút chân không là <tong_cuoc_sau_hut> VND
    And khách hàng tiết kiệm được <tien_tiet_kiem> VND so với không hút chân không

    Examples:
      | can_nang_thuc_te | chieu_dai | chieu_rong | chieu_cao | khoi_luong_the_tich_ban_dau | khoi_luong_the_tich_sau_hut | trong_luong_tinh_cuoc | cuoc_truoc_hut | tong_cuoc_sau_hut | tien_tiet_kiem |
      | 2,0              | 100       | 60         | 40        | 48,0                        | 24,0                        | 24,0                  | 255.000        | 150.000          | 105.000        |
      | 3,0              | 120       | 70         | 50        | 84,0                        | 42,0                        | 42,0                  | 435.000        | 240.000          | 195.000        |
      | 4,0              | 150       | 80         | 60        | 144,0                       | 72,0                        | 72,0                  | 735.000        | 390.000          | 345.000        |

  @NV11 @NV12 @Persistence
  Scenario: Tính gói sinh nhật trọn bộ và xác nhận lưu kết quả qua ShippingStorePort
    Given yêu cầu tính cước cho kiện thú bông nặng 2,0 kg, kích thước 100 cm x 60 cm x 40 cm
    And khách hàng chọn hút chân không, gói quà sinh nhật và thiệp chúc mừng
    When hệ thống tính cước và lưu kết quả qua ShippingStorePort
    Then khối lượng thể tích sau hút chân không là 24,0 kg
    And trọng lượng tính cước là 24,0 kg
    And cước cơ sở là 135.000 VND
    And phụ phí hút chân không là 15.000 VND
    And phụ phí gói quà là 20.000 VND
    And phụ phí thiệp chúc mừng là 10.000 VND
    And tổng chi phí là 180.000 VND
    And ShippingStorePort nhận được kết quả đã lưu với tổng chi phí 180.000 VND
