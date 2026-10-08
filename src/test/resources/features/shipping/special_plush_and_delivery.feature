# language: vi
@PhanHe4 @NV11 @NV12
Tính năng: Tính cước giao hàng cồng kềnh và dịch vụ gia tăng cho thú bông
  Để khách hàng nhận được mức cước và tổng chi phí minh bạch
  Là hệ thống xử lý yêu cầu tính cước giao hàng
  Tôi muốn tính trọng lượng tính cước và các dịch vụ được chọn từ dữ liệu yêu cầu

  Quy tắc: Khối lượng thể tích bằng Dài nhân Rộng nhân Cao chia 5000, với kích thước tính bằng cm
  Quy tắc: Trọng lượng tính cước là giá trị lớn hơn giữa cân nặng thực tế và khối lượng thể tích
  Quy tắc: Cước cơ sở là 20.000 VNĐ cho 1 kg đầu tiên; mỗi 0,5 kg tiếp theo hoặc phần lẻ làm tròn lên tính 2.500 VNĐ

  @NV11
  Khung tình huống: Chọn đúng trọng lượng tính cước cho hàng nhỏ gọn nặng cân và thú bông cồng kềnh siêu nhẹ
    Cho yêu cầu tính cước có dữ liệu kiện hàng:
      | Loại hàng             | Cân nặng thực tế (kg) | Dài (cm) | Rộng (cm) | Cao (cm) | Khối lượng thể tích (kg) | Trọng lượng tính cước (kg) | Cước cơ sở (VNĐ) |
      | <loai_hang>           | <can_nang>            | <dai>    | <rong>    | <cao>   | <khoi_luong_the_tich>     | <trong_luong_tinh_cuoc>    | <cuoc_co_so>     |
    Và yêu cầu không chọn dịch vụ hút chân không
    Khi Use Case tính cước giao hàng xử lý yêu cầu
    Thì Response Model phải trả về khối lượng thể tích là <khoi_luong_the_tich> kg
    Và Response Model phải trả về trọng lượng tính cước là <trong_luong_tinh_cuoc> kg
    Và Response Model phải trả về cước cơ sở là <cuoc_co_so> VNĐ

    Dữ liệu:
      | loai_hang                   | can_nang | dai | rong | cao | khoi_luong_the_tich | trong_luong_tinh_cuoc | cuoc_co_so |
      | Hàng nhỏ gọn nặng cân       | 4        | 20  | 20   | 20  | 1,6                 | 4                     | 35.000     |
      | Thú bông cồng kềnh siêu nhẹ | 0,5      | 100 | 60   | 40  | 48                  | 48                    | 255.000    |

  @NV11 @NV12
  Khung tình huống: Đánh giá mức giảm cước khi hút chân không thú bông cỡ lớn
    Cho yêu cầu tính cước có kiện thú bông cỡ <kich_co> với cân nặng thực tế <can_nang> kg và kích thước <dai> x <rong> x <cao> cm
    Và khối lượng thể tích ban đầu của kiện hàng là <the_tich_ban_dau> kg
    Khi Use Case tính cước giao hàng xử lý yêu cầu có dịch vụ hút chân không
    Thì Response Model phải trả về khối lượng thể tích sau hút chân không là <the_tich_sau_hut> kg
    Và trọng lượng tính cước sau hút chân không là <trong_luong_tinh_cuoc> kg
    Và cước cơ sở khi không hút chân không là <cuoc_truoc_hut> VNĐ
    Và tổng cước sau khi cộng phụ phí hút chân không là <tong_cuoc_sau_hut> VNĐ
    Và số tiền tiết kiệm so với không hút chân không là <tien_tiet_kiem> VNĐ

    Dữ liệu:
      | kich_co | can_nang | dai | rong | cao | the_tich_ban_dau | the_tich_sau_hut | trong_luong_tinh_cuoc | cuoc_truoc_hut | tong_cuoc_sau_hut | tien_tiet_kiem |
      | 1 m     | 2        | 100 | 60   | 40  | 48               | 24               | 24                    | 255.000        | 150.000          | 105.000        |
      | 1,2 m   | 3        | 120 | 70   | 50  | 84               | 42               | 42                    | 435.000        | 240.000          | 195.000        |
      | 1,5 m   | 4        | 150 | 80   | 60  | 144              | 72               | 72                    | 735.000        | 390.000          | 345.000        |

  @NV11 @NV12 @StoreAblePort
  Kịch bản: Tính tổng chi phí trọn bộ dịch vụ và lưu kết quả qua StoreAble Port
    Cho Request Model tính cước gồm kiện thú bông nặng 2 kg, kích thước 100 x 60 x 40 cm
    Và khách hàng chọn hút chân không, gói quà sinh nhật và viết thiệp chúc mừng
    Khi Use Case tính cước giao hàng xử lý yêu cầu
    Thì Response Model phải trả về khối lượng thể tích sau hút chân không là 24 kg
    Và Response Model phải trả về trọng lượng tính cước là 24 kg
    Và Response Model phải trả về cước cơ sở là 135.000 VNĐ
    Và Response Model phải trả về phụ phí hút chân không là 15.000 VNĐ
    Và Response Model phải trả về phụ phí gói quà là 20.000 VNĐ
    Và Response Model phải trả về phụ phí viết thiệp là 10.000 VNĐ
    Và Response Model phải trả về tổng chi phí là 180.000 VNĐ
    Và StoreAble Port phải nhận lưu kết quả tính cước tương ứng với Response Model đó
