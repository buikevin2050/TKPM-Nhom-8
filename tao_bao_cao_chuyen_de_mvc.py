import os
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

def set_cell_background(cell, hex_color):
    """Đặt màu nền cho ô trong bảng"""
    shading_elm = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{hex_color}"/>')
    cell._tc.get_or_add_tcPr().append(shading_elm)

def style_heading(heading, color_rgb, size_pt, space_before=14, space_after=6):
    heading.paragraph_format.space_before = Pt(space_before)
    heading.paragraph_format.space_after = Pt(space_after)
    for r in heading.runs:
        r.font.name = 'Times New Roman'
        r.font.size = Pt(size_pt)
        r.font.bold = True
        r.font.color.rgb = color_rgb

def create_mvc_comprehensive_document():
    doc = Document()

    # Cấu hình lề trang chuẩn A4
    for section in doc.sections:
        section.top_margin = Inches(0.8)
        section.bottom_margin = Inches(0.8)
        section.left_margin = Inches(1.0)
        section.right_margin = Inches(1.0)

    # Đặt font chữ mặc định
    style = doc.styles['Normal']
    font = style.font
    font.name = 'Times New Roman'
    font.size = Pt(11.5)
    font.color.rgb = RGBColor(0x21, 0x25, 0x29)

    color_primary = RGBColor(0x1A, 0x36, 0x5D)    # Deep Navy
    color_secondary = RGBColor(0x2B, 0x6C, 0xB0)  # Classic Blue
    color_accent = RGBColor(0xC5, 0x30, 0x30)     # Crimson Red

    # ==================== TRANG BÌA & TIÊU ĐỀ ====================
    title_p = doc.add_paragraph()
    title_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    title_run = title_p.add_run("BÁO CÁO NGHIÊN CỨU CHUYÊN ĐỀ KIẾN TRÚC PHẦN MỀM:\nNGHIÊN CỨU TOÀN DIỆN VỀ MẪU THIẾT KẾ MODEL - VIEW - CONTROLLER (MVC)")
    title_run.font.size = Pt(16.5)
    title_run.font.bold = True
    title_run.font.color.rgb = color_primary

    sub_p = doc.add_paragraph()
    sub_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    sub_run = sub_p.add_run("Học phần: Thiết kế & Kiến trúc Phần mềm\nBáo cáo Chuyên đề Đánh giá Giữa kỳ\nChủ đề: Bản chất Kiến trúc, Nguyên lý Thiết kế, Ứng dụng Thực nghiệm & Lộ trình Tiến hóa")
    sub_run.font.size = Pt(12)
    sub_run.font.italic = True
    sub_run.font.color.rgb = RGBColor(0x4A, 0x55, 0x68)

    doc.add_paragraph().paragraph_format.space_after = Pt(12)

    # ==================== PHẦN I ====================
    h1 = doc.add_heading("PHẦN I: NGUỒN GỐC LỊCH SỬ VÀ BỐI CẢNH RA ĐỜI", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "Mẫu kiến trúc Model - View - Controller (MVC) được sáng tạo bởi nhà khoa học máy tính người Na Uy Trygve Reenskaug "
        "vào khoảng năm 1978 – 1979 khi ông đang làm việc tại trung tâm nghiên cứu danh tiếng Xerox PARC trên ngôn ngữ "
        "lập trình hướng đối tượng sơ khai Smalltalk-80."
    )

    h2 = doc.add_heading("1. Bối cảnh lịch sử và vấn nạn 'Spaghetti Code'", level=2)
    style_heading(h2, color_secondary, 12)
    doc.add_paragraph(
        "Vào cuối thập niên 1970, giao diện đồ họa người dùng (GUI) bắt đầu xuất hiện thay thế cho màn hình dòng lệnh đơn sắc (CLI). "
        "Các lập trình viên thời kỳ này gặp phải một vấn đề nghiêm trọng: do thiếu khuôn mẫu kiến trúc, toàn bộ mã nguồn xử lý sự kiện "
        "chuột/bàn phím, mã nguồn hiển thị điểm ảnh (pixel) và mã nguồn tính toán nghiệp vụ/dữ liệu bị nhồi nhét chung vào một khối nguyên khối (Spaghetti Code).\n"
        "Hậu quả là phần mềm trở nên cực kỳ mong manh: chỉ một thay đổi nhỏ về màu sắc hay bố cục giao diện cũng có thể phá vỡ logic tính toán "
        "nghiệp vụ cốt lõi. Đồng thời, việc kiểm thử tự động là bất khả thi vì logic bị gắn chặt vào các thành phần đồ họa vật lý."
    )

    h2 = doc.add_heading("2. Đột phá tư duy của Trygve Reenskaug", level=2)
    style_heading(h2, color_secondary, 12)
    doc.add_paragraph(
        "Trygve Reenskaug nhận ra rằng: Mô hình dữ liệu trong tư duy máy tính (Mental Model) và cách con người nhìn thấy nó trên màn hình "
        "(Visual Presentation) là hai phạm trù hoàn toàn khác biệt. Ông đã áp dụng nguyên lý Phân tách mối quan tâm (Separation of Concerns - SoC): "
        "chia ứng dụng thành 3 vùng độc lập — Một vùng chuyên chứa tri thức/dữ liệu (Model), một vùng chuyên hiển thị (View), "
        "và một vùng làm cầu nối tiếp nhận tương tác của con người (Controller)."
    )

    # ==================== PHẦN II ====================
    h1 = doc.add_heading("PHẦN II: KHÁI NIỆM, ĐỊNH NGHĨA VÀ MỤC TIÊU THIẾT KẾ", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "• Định nghĩa chuẩn: MVC (Model-View-Controller) là mẫu thiết kế tổ chức các lớp trong phần mềm thành ba thành phần tách biệt. "
        "Mỗi thành phần có trách nhiệm rõ ràng: xử lý dữ liệu, hiển thị giao diện và điều phối yêu cầu.\n"
        "• Ẩn dụ thực tế (Mô hình Nhà hàng 3 bên):\n"
        "   + Khách hàng: Người dùng (User) gửi yêu cầu.\n"
        "   + View (Thực đơn & Không gian ăn uống): Giao diện trực quan để khách xem thông tin và chọn món.\n"
        "   + Controller (Nhân viên phục vụ): Tiếp nhận order, điều phối vào bếp, mang thức ăn ra cho khách (không trực tiếp nấu).\n"
        "   + Model (Khu vực bếp nấu): Chứa nguyên liệu (dữ liệu) và công thức nấu nướng (nghiệp vụ). Đầu bếp không cần biết mặt khách hàng."
    )

    doc.add_paragraph(
        "Bốn mục tiêu thiết kế tối thượng của MVC:\n"
        "1. Giảm thiểu sự phụ thuộc (Decoupling): Tách rời giao diện khỏi quy tắc nghiệp vụ cốt lõi.\n"
        "2. Tăng khả năng kiểm thử tự động (Testability): Cho phép kiểm thử Model và Controller độc lập mà không cần mở GUI.\n"
        "3. Tái sử dụng mã nguồn (Reusability): Một Model duy nhất có thể phục vụ nhiều View khác nhau (Web, Mobile, Console).\n"
        "4. Dễ bảo trì và mở rộng (Maintainability & Extensibility): Lập trình viên Frontend và Backend có thể làm việc song song."
    )

    # ==================== PHẦN III ====================
    h1 = doc.add_heading("PHẦN III: GIẢI PHẪU CHI TIẾT 3 THÀNH PHẦN (MODEL - VIEW - CONTROLLER)", level=1)
    style_heading(h1, color_primary, 13.5)

    table_data = [
        ("THÀNH PHẦN", "STEREOTYPE UML", "TRÁCH NHIỆM CHÍNH (SRP)", "ĐIỀU CẤM KỴ (ANTI-PATTERNS)", "VÍ DỤ LỚP ĐIỂN HÌNH"),
        ("MODEL", "«model»", "Lưu trữ dữ liệu/trạng thái; thực thi validation và business logic; phát thông báo (Observer/events) khi dữ liệu đổi.", "Tuyệt đối không phụ thuộc trực tiếp vào GUI hoặc thư viện đồ họa.", "MultiplicationModel, ProductModel, StudentModel"),
        ("VIEW", "«view»", "Hiển thị biểu mẫu (forms), màn hình (screens), templates; nhận nhập liệu; đăng ký lắng nghe Model để refresh tự động.", "Tuyệt đối không chứa logic tính toán nghiệp vụ (Smart UI).", "MultiplicationView, ProductForm, StudentScreen"),
        ("CONTROLLER", "«control»", "Nhận sự kiện/yêu cầu từ View (input handlers, routes); điều phối gọi Model; chọn View phản hồi kết quả.", "Không trực tiếp vẽ giao diện, không trực tiếp lưu trữ cơ sở dữ liệu.", "MultiplicationController, ProductController")
    ]

    table = doc.add_table(rows=len(table_data), cols=5)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    for r_idx, row in enumerate(table_data):
        for c_idx, val in enumerate(row):
            cell = table.cell(r_idx, c_idx)
            cell.text = val
            cell.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
            if r_idx == 0:
                set_cell_background(cell, "2B6CB0")
                for p in cell.paragraphs:
                    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
                    for r in p.runs:
                        r.font.bold = True
                        r.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)
                        r.font.size = Pt(9.5)
            else:
                set_cell_background(cell, "F7FAFC" if r_idx % 2 == 1 else "EDF2F7")
                for p in cell.paragraphs:
                    for r in p.runs:
                        r.font.size = Pt(9.0)

    doc.add_paragraph().paragraph_format.space_after = Pt(8)

    # ==================== PHẦN IV ====================
    h1 = doc.add_heading("PHẦN IV: CƠ CHẾ HOẠT ĐỘNG & CHU TRÌNH 5 BƯỚC TUẦN TỰ", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "Chu trình xử lý một tác vụ trong kiến trúc MVC diễn ra khép kín qua 5 bước tuần tự:\n"
        "• Bước 1 (User Input): Người dùng thao tác nhập liệu trên giao diện hoặc kích hoạt sự kiện bấm nút.\n"
        "• Bước 2 (View -> Controller): View bắt sự kiện và gửi yêu cầu xử lý sang Controller qua bộ xử lý đầu vào.\n"
        "• Bước 3 (Controller Updates Model): Controller kiểm tra dữ liệu sơ bộ và gọi phương thức của Model để cập nhật trạng thái.\n"
        "• Bước 4 (Model Notifies Observers): Sau khi hoàn thành tính toán, Model phát thông báo thay đổi tới các Observer theo dõi nó.\n"
        "• Bước 5 (View Refreshes): View nhận thông báo từ Model và tự động làm mới giao diện hiển thị cho người dùng."
    )

    doc.add_paragraph(
        "Phân biệt Desktop MVC (Active) và Web MVC (Passive):\n"
        "• Desktop MVC (Active MVC): Ứng dụng chạy trạng thái liên tục trong RAM. View chủ động đăng ký Observer và lắng nghe tín hiệu "
        "để tự động refresh mà không cần Controller can thiệp.\n"
        "• Web MVC (Passive MVC): Chạy trên giao thức HTTP phi trạng thái (Stateless). Chu trình chuyển dịch sang mô hình Yêu cầu - Phản hồi (Request - Response). "
        "View thụ động (Passive View / Humble Object), chỉ kết xuất HTML/JSON khi có Request chuyển từ Controller."
    )

    # ==================== PHẦN V ====================
    h1 = doc.add_heading("PHẦN V: BÀI TOÁN KINH ĐIỂN 'TÍNH TÍCH HAI SỐ' VÀ MÃ NGUỒN MẪU", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "Bài toán mẫu mực 'Tính tích hai số' (Multiplication: 4 * 5 = 20) trong slide bài giảng được hiện thực hóa bằng mã nguồn Java chuẩn mực:"
    )

    code_text = (
        "// 1. Giao diện Observer trừu tượng (DIP)\n"
        "public interface ModelListener {\n"
        "    void onDataChanged(double ketQuaMoi);\n"
        "}\n\n"
        "// 2. Lớp Model («model»)\n"
        "public class MultiplicationModel {\n"
        "    private double soA, soB, ketQua;\n"
        "    private final List<ModelListener> listeners = new ArrayList<>();\n\n"
        "    public void addListener(ModelListener l) { listeners.add(l); }\n"
        "    public void tinhTich(double a, double b) {\n"
        "        this.soA = a; this.soB = b;\n"
        "        this.ketQua = a * b;\n"
        "        notifyObservers();\n"
        "    }\n"
        "    private void notifyObservers() {\n"
        "        for (ModelListener l : listeners) l.onDataChanged(this.ketQua);\n"
        "    }\n"
        "    public double getKetQua() { return this.ketQua; }\n"
        "}\n\n"
        "// 3. Lớp View («view»)\n"
        "public class MultiplicationView implements ModelListener {\n"
        "    private double giaTriHienThi;\n"
        "    @Override\n"
        "    public void onDataChanged(double ketQuaMoi) {\n"
        "        this.giaTriHienThi = ketQuaMoi;\n"
        "        veLaiManHinh();\n"
        "    }\n"
        "    public void veLaiManHinh() {\n"
        "        System.out.println(\"[VIEW - TỰ ĐỘNG LÀM MỚI] Kết quả tích là: \" + this.giaTriHienThi);\n"
        "    }\n"
        "}\n\n"
        "// 4. Lớp Controller («control»)\n"
        "public class MultiplicationController {\n"
        "    private final MultiplicationModel model;\n"
        "    private final MultiplicationView view;\n"
        "    public MultiplicationController(MultiplicationModel m, MultiplicationView v) {\n"
        "        this.model = m; this.view = v;\n"
        "    }\n"
        "    public void xuLyTinhTich(double a, double b) {\n"
        "        if (Double.isNaN(a) || Double.isNaN(b)) throw new IllegalArgumentException(\"Dữ liệu không hợp lệ\");\n"
        "        this.model.tinhTich(a, b);\n"
        "    }\n"
        "}"
    )

    p_code = doc.add_paragraph()
    r_code = p_code.add_run(code_text)
    r_code.font.name = 'Consolas'
    r_code.font.size = Pt(8.5)
    r_code.font.color.rgb = RGBColor(0x2D, 0x37, 0x48)
    p_code.paragraph_format.left_indent = Inches(0.2)

    # ==================== PHẦN VI ====================
    h1 = doc.add_heading("PHẦN VI: MÔ HÌNH HÓA UML VÀ BẢN CHẤT CÁC MỐI QUAN HỆ", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "Trong biểu đồ lớp UML của MVC, các mối quan hệ được định hướng khắt khe:\n"
        "1. View ──> Controller (Association): View nắm giữ biến tham chiếu đến Controller để chuyển tiếp các sự kiện người dùng.\n"
        "2. Controller ──> Model (Association): Controller nắm giữ tham chiếu của Model để gọi lệnh tính toán và nạp tham số.\n"
        "3. View - - ▷ ModelListener (Realization): View hiện thực hóa giao diện trừu tượng để trở thành Observer.\n"
        "4. Model ──> ModelListener (Association): Model chỉ nắm giữ danh sách giao diện trừu tượng, hoàn toàn KHÔNG trỏ tới lớp View cụ thể.\n"
        "-> Ý nghĩa kiến trúc: Mô hình hóa hoàn toàn thỏa mãn nguyên lý Đảo ngược phụ thuộc (DIP) và quy tắc 'Model không phụ thuộc vào GUI'."
    )

    # ==================== PHẦN VII ====================
    h1 = doc.add_heading("PHẦN VII: QUY TẮC THIẾT KẾ VÀ CÁC PHẢN MẪU (ANTI-PATTERNS CẦN TRÁNH)", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "Bảng quy tắc thiết kế chuẩn mực từ slide bài giảng:\n"
        "• Những việc NÊN LÀM: Giữ trách nhiệm rõ ràng (SRP), gắn stereotype «view», «control», «model» trong UML, viết Unit Test cho cả Model và Controller.\n"
        "• Những việc NÊN TRÁNH: Không đưa business logic vào View, không để Controller kiêm việc vẽ UI, không để Model phụ thuộc GUI."
    )

    doc.add_paragraph(
        "Ba căn bệnh phản mẫu (Anti-Patterns) kinh điển cần khắc phục:\n"
        "1. Fat Controller (Controller phình to): Controller nhồi nhét cả logic tính toán và truy vấn SQL -> Khắc phục: chuyển toàn bộ sang Model.\n"
        "2. Smart UI / Fat View (Giao diện thông minh quá mức): View tự làm phép tính -> Khắc phục: View chỉ là Humble Object nhận dữ liệu in ra.\n"
        "3. Anemic Domain Model (Mô hình thiếu máu): Model chỉ chứa getter/setter ngây ngô -> Khắc phục: đóng gói logic nghiệp vụ vào Model."
    )

    # ==================== PHẦN VIII ====================
    h1 = doc.add_heading("PHẦN VIII: CHIẾN LƯỢC KIỂM THỬ ĐƠN VỊ (UNIT TESTING TRONG MVC)", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "Khả năng kiểm thử (Testability) là ưu thế vượt trội của MVC:\n"
        "• Kiểm thử Model độc lập: Do Model không dính líu GUI, bài test JUnit 5 có thể khởi tạo Model, gọi tinhTich(4.0, 5.0) "
        "và khẳng định kết quả getKetQua() == 20.0 trong thời gian vài mili-giây mà không cần bật màn hình.\n"
        "• Kiểm thử Controller điều phối: Dùng kỹ thuật Mocking giả lập View/Model, kiểm tra Controller có ném ngoại lệ khi gặp dữ liệu lỗi "
        "hoặc có gọi đúng phương thức của Model hay không mà không cần người dùng thao tác thực tế."
    )

    # ==================== PHẦN IX ====================
    h1 = doc.add_heading("PHẦN IX: MỐI QUAN HỆ VỚI MÔ HÌNH B-C-E VÀ CLEAN ARCHITECTURE", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "Khi nâng tầm lên các hệ sinh thái lớn theo Clean Architecture (Robert C. Martin):\n"
        "• MVC chỉ là một mẫu thiết kế cục bộ nằm ở tầng ngoại vi (Interface Adapters / Presentation Layer).\n"
        "• Sự tương ứng với mô hình B-C-E: View tương ứng Boundary; Controller tương ứng Control; Model tương ứng Entity.\n"
        "• Khắc phục hạn chế của MVC: Clean Architecture bóc tách chữ 'Model' thành Use Case Interactor (điều phối nghiệp vụ ứng dụng) "
        "và Pure Domain Entities (quy tắc kinh doanh cốt lõi), đồng thời áp dụng DIP qua Interface Ports để tách rời Database ra khỏi nghiệp vụ."
    )

    # ==================== PHẦN X ====================
    h1 = doc.add_heading("PHẦN X: KỊCH BẢN THUYẾT TRÌNH GIỮA KỲ VÀ BỘ CÂU HỎI VẤN ĐÁP", level=1)
    style_heading(h1, color_primary, 13.5)

    doc.add_paragraph(
        "Kịch bản thuyết trình 5 phút tóm tắt:\n"
        "1. Mở đầu: Giới thiệu MVC do Trygve Reenskaug sáng tạo (1979) để giải quyết thảm họa Spaghetti Code thời kỳ đầu của GUI.\n"
        "2. Phân tích 3 khối: Model (dữ liệu/nghiệp vụ, không dính GUI), View (hiển thị, nhận input, lắng nghe Observer), Controller (điều phối luồng).\n"
        "3. Minh chứng qua bài toán tính tích: Nhập 4 và 5 -> Controller điều phối -> Model tính ra 20 -> View tự refresh hiển thị.\n"
        "4. Kết luận: Tuân thủ SRP, áp dụng stereotype UML, tránh Fat Controller/Smart UI để bảo toàn tính bảo trì và kiểm thử tự động."
    )

    qa_list = [
        ("Câu 1: Tại sao trong MVC, mũi tên phụ thuộc mã nguồn lại hướng về Model?",
         "Theo nguyên lý Cấp độ chính sách (Policy & Level), Model chứa nghiệp vụ cốt lõi, ổn định nhất, xa I/O nhất; View và Controller hay thay đổi nên phải phụ thuộc vào Model để bảo vệ hệ thống không bị rung lắc."),
        ("Câu 2: Tại sao Model không gọi trực tiếp lớp MultiplicationView mà phải qua Interface ModelListener?",
         "Để tuân thủ quy tắc 'Model không phụ thuộc vào GUI' và nguyên lý DIP. Dùng Interface trừu tượng giúp Model độc lập hoàn toàn và có thể phát thông báo cho nhiều View cùng lúc."),
        ("Câu 3: Controller khác Model ở điểm nào? Viết luôn hàm tính toán vào Controller có được không?",
         "Controller chỉ làm nhiệm vụ điều phối luồng. Nếu viết hàm tính toán vào Controller sẽ phạm lỗi Fat Controller, vi phạm SRP và không thể tái sử dụng logic khi chuyển nền tảng."),
        ("Câu 4: Mẫu MVC này là Active hay Passive?",
         "Mẫu kết hợp Observer Pattern phát tín hiệu trong slide là Active MVC (Desktop/Stateful). Còn Web hiện đại dùng Passive MVC theo chu trình Request - Response phi trạng thái."),
        ("Câu 5: MVC liên quan gì đến mô hình B-C-E?",
         "B-C-E là sự tiến hóa của MVC: View tương ứng Boundary, Controller tương ứng Control, Model tương ứng Entity. B-C-E trừu tượng hóa ranh giới sâu hơn để độc lập hoàn toàn với Framework và DB.")
    ]

    for q, a in qa_list:
        p_q = doc.add_paragraph()
        run_q = p_q.add_run(q)
        run_q.bold = True
        run_q.font.color.rgb = color_accent
        
        p_a = doc.add_paragraph()
        p_a.add_run("Trả lời: " + a)
        p_a.paragraph_format.left_indent = Inches(0.2)

    # Lưu tệp Word
    output_filename = "BaoCao_ChuyenDe_KienTruc_MVC_HoanChinh.docx"
    doc.save(output_filename)
    print(f"\n===> [THÀNH CÔNG] Đã tạo tệp báo cáo Word hoàn chỉnh: {os.path.abspath(output_filename)}\n")

if __name__ == "__main__":
    create_mvc_comprehensive_document()