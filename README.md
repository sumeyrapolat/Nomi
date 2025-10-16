# 📱 Nomi – Jetpack Compose Telefon Rehberi Uygulaması

**Nomi**, kullanıcıların kendi rehberlerini oluşturabildiği, modern ve sade bir **Jetpack Compose** uygulamasıdır.  
Uygulama, Android’in yeni nesil UI mimarisi olan **Compose**, katmanlı yapı prensipleri ve **Clean Architecture** yaklaşımıyla inşa edilmiştir.  
Amaç, hem teknik yetkinlik hem de kullanıcı deneyimi açısından **gerçek bir ürün kalitesine** sahip bir telefon rehberi ortaya koymaktır.

---

## ✨ Genel Bakış

Nomi; kullanıcıların kişilerini görüntüleyip yönetebildiği, fotoğraf ekleyip düzenleyebildiği ve cihaz rehberiyle etkileşime geçebildiği bir kişisel rehber uygulamasıdır.  
Uygulama tamamen **yerel olarak (offline-first)** çalışır, ancak cihazın gerçek rehberiyle entegre edilmiştir.

Uygulamanın bazı temel bileşenleri:
- `ContactsScreen` → Rehberdeki kişilerin listelendiği ana ekran.  
- `ContactDetailBottomSheet` → Seçilen kişinin detaylarının gösterildiği alt sayfa.  
- `AddContactBottomSheet` → Yeni kişi ekleme arayüzü.  
- `ContactsViewModel` → Event–State mimarisiyle çalışan ViewModel.  
- `saveContactToPhone()` → Kişiyi Android’in sistem rehberine kaydeden yardımcı fonksiyon.

---

## 🚀 Uygulama Özellikleri

- 👤 **Kişi Yönetimi**
  - Yeni kişi ekleme (ad, soyad, telefon numarası, fotoğraf)
  - Kişi bilgilerini düzenleme ve silme
  - Rehber kaydetme (cihazın native rehberine ekleme)
  - Rehberdeki mevcut kişilerle eşleşenleri ikonla işaretleme  

- 🧭 **Profil ve Detay Görünümü**
  - Alt bottom sheet içinde kişi detayları gösterilir.  
  - Fotoğrafın gölgesi, görseldeki **baskın renge** göre otomatik değişir.  
  - Düzenleme sonrası liste otomatik olarak yenilenir.

- 🔍 **Akıllı Arama**
  - İsim ve soyisim birlikte aranabilir (ör. “Ali Can”).  
  - Daha önce yapılan aramalar, kullanıcı search alanına tıkladığında öneri olarak listelenir.  
  - Son aramalar `RecentSearchManager` üzerinden yönetilir.

- 🗑️ **Swipe İşlemleri**
  - Listedeki kişiler sola kaydırıldığında **sil** ve **düzenle** butonları görünür.

- 📂 **Görsel Kaydetme**
  - Fotoğraflar cihazın hafızasından seçilerek kişiye atanabilir.  
  - Görsel erişim izinleri `rememberLauncherForActivityResult` ile yönetilir.  
  - Kullanıcı izin vermediğinde toast mesajı gösterilir (izin reddedildi uyarısı).

- ⚙️ **State Yönetimi**
  - Tüm ekranlar `UiState` akışıyla reaktif olarak güncellenir.  
  - İşlemler `ContactEvent` üzerinden tetiklenir.

---

## 🧱 Teknik Mimarî

Nomi, **Clean Architecture + MVVM** yapısını takip eder.

```
com.sumeyrapolat.nomi
│
├── data/
│   ├── repository/
│   ├── local/
│   └── remote/
│
├── domain/
│   ├── model/
│   ├── usecase/
│   └── repository/
│
├── presentation/
│   ├── contacts/
│   ├── components/
│   ├── profile/
│   └── ui/theme/
│
└── util/
```

### 📚 Katmanlar

- **Domain Layer:**  
  İş kuralları ve `UseCase` yapısı (`AddContactUseCase`, `DeleteContactUseCase`, `UpdateContactUseCase`).
- **Data Layer:**  
  Local veri kaynakları, `RecentSearchManager`, repository implementasyonları.
- **Presentation Layer:**  
  Compose UI, event-state mantığı, `ContactsViewModel`.

---

## 🧪 Kullanılan Teknolojiler

| Teknoloji | Kullanım Amacı |
|------------|----------------|
| **Kotlin** | Dil |
| **Jetpack Compose** | Modern deklaratif UI |
| **Hilt (Dagger)** | Dependency Injection |
| **Coroutines & Flow** | Asenkron ve reaktif veri akışı |
| **Coil** | Görsel yükleme ve cache |
| **ActivityResult API** | Galeriden fotoğraf seçimi ve izin yönetimi |
| **Android ContactsContract** | Cihaz rehberine kişi kaydetme |
| **Material 3 Components** | Modern Compose arayüz bileşenleri |

---

## 🔄 Uygulama Akışı

1. **ContactsScreen** açıldığında `viewModel.onEvent(ContactEvent.LoadContacts)` çağrılır.  
2. Kişiler `ContactsViewModel` aracılığıyla `UiState` içinde toplanır.  
3. Kullanıcı bir kişiye tıklarsa, `ContactDetailBottomSheet` açılır.  
4. Düzenleme/silme işlemi yapıldığında liste otomatik yenilenir.  
5. Kullanıcı “Rehbere Kaydet” butonuna tıklarsa, kişi cihazın native rehberine eklenir.

---

## 🎨 UI Detayları

- Tüm ekranlar **Material3** tasarım ilkelerine göre Compose ile yazılmıştır.  
- Kişi listeleri `LazyColumn` kullanır.  
- Profil görseli için **dinamik gölge efekti** uygulanır (dominant color extraction).  
- Alt bottom sheet’ler `ModalBottomSheet` ile yönetilir.  

---

## 👩‍💻 Geliştirici

**Sümeyra Polat**  
Android Developer  
📍 Remote  
📧 [sumeyrapolaat@gmail.com](mailto:sumeyrapolaat@gmail.com)
